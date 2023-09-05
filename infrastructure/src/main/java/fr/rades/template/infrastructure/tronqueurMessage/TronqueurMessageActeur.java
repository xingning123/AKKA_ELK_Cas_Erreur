package fr.rades.template.infrastructure.tronqueurMessage;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.Pair;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import fr.rades.template.infrastructure.recuperateurLogstash.message.MessageRecuperateurTronqueur;
import fr.rades.template.infrastructure.tronqueurMessage.message.MessageTronqueurTraiteur;
import fr.rades.template.infrastructure.interfaces.SeparateurStringEnDocuments;
import fr.rades.template.infrastructure.interfaces.TraiteurDate;
import fr.rades.template.infrastructure.interfaces.TraiteurString;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;


public class TronqueurMessageActeur extends AbstractActor implements TraiteurString, TraiteurDate, SeparateurStringEnDocuments {

  private final Gson gson = new Gson();
    private ActorRef traiteurMessagesActor;
    private final String regexJson = "\\}\\{" ;

    public static Props props(ActorRef traiteurMessagesActor) {
        return (Props.create(TronqueurMessageActeur.class, () -> new TronqueurMessageActeur(traiteurMessagesActor)));
    }

    public TronqueurMessageActeur(ActorRef traiteurMessagesActor) {
        this.traiteurMessagesActor = traiteurMessagesActor;
    }

    private ArrayList<JsonObject> donneeRecuEnJson(String donneeRecue) {

        ArrayList<String> listeDocuments = this.separerDocuments(donneeRecue, this.regexJson);
        ArrayList<JsonObject> listeDocumentJson = new ArrayList<>();

        for (String document : listeDocuments) {
            try {
                listeDocumentJson.add(this.gson.fromJson(document, JsonObject.class));

            }
            catch (JsonSyntaxException jsonSyntaxException) {
                // A VOIR QUE FAIRE POUR CE CAS
                System.out.println(" PROBLEME AVEC LE MESSAGE \n" + document) ;
            }

        }
        return listeDocumentJson;
    }


    private MessageTronqueurTraiteur jsonEnMessagePourTraiteur(JsonObject documentJson) throws ParseException {

            String id_commande = supprimerBordureMots(documentJson.get("id_commande").getAsString(),'"');
            String timestampString = supprimerBordureMots(documentJson.
                    get("@timestamp").getAsString(),'"');
            Date timestamp = stringEnDate(timestampString);

            //RECUPERATION DU CHAMP ETATS
            JsonObject etatsDocument = documentJson.getAsJsonObject("etats");

            ArrayList<InfosMessageEtat> listeInfosMessageEtats = processEtatsDocument(etatsDocument);
            MessageTronqueurTraiteur messageTronqueurTraiteur = new MessageTronqueurTraiteur(id_commande, timestamp, listeInfosMessageEtats);
            return messageTronqueurTraiteur;
    }



    /*
        CETTE FONCTION PARCOURT LES ETATS ET RENVOIE
        UNE LISTE DE INFOS MESSAGEETAT (nomEtat, date_entree, date_sortie et leurs booléens
     */
    private ArrayList<InfosMessageEtat> processEtatsDocument(JsonObject etatsDocument)
            throws ParseException
    {
        ArrayList<InfosMessageEtat> listeInfosMessagesEtats = new ArrayList<>();

        for (String cleNomEtat : etatsDocument.keySet()) {
            JsonObject jsonUnEtat = etatsDocument.get(cleNomEtat).getAsJsonObject();

            Pair<Date, Boolean> dateEntreeEtBool = extraireDateEntreeOuSortie(jsonUnEtat, "date_entree");
            Pair<Date, Boolean> dateSortieEtBool = extraireDateEntreeOuSortie(jsonUnEtat , "date_sortie");
            InfosMessageEtat infosMessageEtat = new InfosMessageEtat(cleNomEtat,
                    dateEntreeEtBool.first(), dateSortieEtBool.first(),
                    dateEntreeEtBool.second(), dateSortieEtBool.second());

            listeInfosMessagesEtats.add(infosMessageEtat);
        }
        return listeInfosMessagesEtats;
    }


    /*
        CETTE FONCTION PREND EN ENTEEE UN JSON ET UNE CLE PONTENTIELLE ET
        RETOURNE LA DATE DE LA CLE + TRUE OU NULL + FALSE
        exemple1:   Jsonobject("date_entree" : "09:06:23"), cleDate "date_entree" ----> Pair(09:06:23, true)
        exemple2: Jsonobject("date_entree" : "09:06:23"), cleDate "date_sortie" ---> Pair(null,false)
     */
    private Pair<Date, Boolean> extraireDateEntreeOuSortie(JsonObject jsonObject, String cleDate)
            throws ParseException {

        if (jsonObject.has(cleDate)) {
            String dateEnString = supprimerBordureMots(jsonObject.get(cleDate).getAsString(), '"');
            Date date = stringEnDate(dateEnString);
            return new Pair<>(date, true);
        }
        else {
            return new Pair<>(null, false);
        }
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(MessageRecuperateurTronqueur.class , messageRecuperateurTronqueur -> {
                    String documentRecu = messageRecuperateurTronqueur.getMessageEnvoyer();
                    ArrayList<JsonObject> documentsJsonSepares = this.donneeRecuEnJson(documentRecu);
                    for (JsonObject jsonObject : documentsJsonSepares) {
                        System.out.println(jsonEnMessagePourTraiteur(jsonObject));
                        MessageTronqueurTraiteur messageTronqueurTraiteur = jsonEnMessagePourTraiteur(jsonObject);
                        traiteurMessagesActor.tell(messageTronqueurTraiteur, getSelf());
                    }
                })
                .build();
    }

}
