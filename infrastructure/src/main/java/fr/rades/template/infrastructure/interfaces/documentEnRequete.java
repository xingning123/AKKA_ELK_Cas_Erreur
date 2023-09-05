package fr.rades.template.infrastructure.interfaces;
import fr.rades.template.infrastructure.CreateurLotRequetesfile.MessagePourCreateurLot;

import java.util.ArrayList;

public interface documentEnRequete {

    public default String formerRequete(MessagePourCreateurLot document) {

        if (document.getIdCmd() == null && document.getEtats() == null) {
            return "";
        }

        ArrayList<Etat> etats = document.getEtats();
        String idCommande = document.getIdCmd();
        String requete = "{ \"update\": {\"_id\": \"" + idCommande + "\", \"_index\": \"commande\"}}\n" + "{ \"doc\": {\"etats\": {" ;
        for (int i = 0; i < etats.size()-1; i++) {
            Etat etat = etats.get(i);
            requete = requete + "\"" + etat.getNomEtat() + "\": {\"date_debut\": \"" + etat.getDateDebut() + "\", \"date_sortie\" : \"" + etat.getDateFin() + "\"},";}
        Etat etat = etats.get(etats.size()-1);
        requete = requete + "\"" + etat.getNomEtat() + "\": {\"date_debut\": \"" + etat.getDateDebut() + "\", \"date_sortie\" : \"" + etat.getDateFin() + "\"}";
        return requete + "}}, \"doc_as_upsert\" : true }\n";
    }

}
