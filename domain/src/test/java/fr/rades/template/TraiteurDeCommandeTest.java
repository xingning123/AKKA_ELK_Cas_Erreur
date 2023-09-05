//package fr.rades.template;
//
//
//import akka.actor.ActorRef;
//import akka.actor.ActorSystem;
//import akka.testkit.javadsl.TestKit;
//import fr.rades.template.domain.CommandeActorfile.Commande;
//import fr.rades.template.domain.CommandeActorfile.Etats;
//import fr.rades.template.domain.TraiteurMessagefile.MessageTraiteePourCommande;
//import fr.rades.template.domain.tools.TraiteurDeCommande;
//import org.junit.AfterClass;
//import org.junit.BeforeClass;
//import org.junit.Test;
//
//import java.util.ArrayList;
//import java.util.Date;
//
//
//public class TraiteurDeCommandeTest {
//
//    private static ActorSystem system;
//
//    @BeforeClass
//    public static void setup() {
//        system = ActorSystem.create();
//    }
//
//    @AfterClass
//    public static void teardown() {
//        TestKit.shutdownActorSystem(system);
//        system = null;
//    }
//
//    @Test
//    public void handleMessage_AddsNewEtatIfNotExist() {
//        new TestKit(system) {
//            {
//                final ActorRef createurLotRequetes = getRef();
//                TraiteurDeCommande traiteur = new TraiteurDeCommande(createurLotRequetes);
//
//                ArrayList<Etats> etatList = new ArrayList<>();
//                etatList.add(new Etats("Etat1", new Date(), new Date()));
//                Commande commande = new Commande("123", etatList);
//
//                MessageTraiteePourCommande message = new MessageTraiteePourCommande("123", "Etat2", new Date(), new Date());
//                traiteur.handleMessage(message);
//
//                // Vérifie si un nouvel état a été ajouté à la commande
//                assert traiteur.getCommande().getEtatList().size() == 1;
//            }
//        };
//    }
//
//    @Test
//    public void handleMessage_UpdatesExistingEtatIfExist() {
//        new TestKit(system) {
//            {
//                final ActorRef createurLotRequetes = getRef();
//                TraiteurDeCommande traiteur = new TraiteurDeCommande(createurLotRequetes);
//
//                ArrayList<Etats> etatList = new ArrayList<>();
//                etatList.add(new Etats("Etat1", new Date(), new Date()));
//                Commande commande = new Commande("123", etatList);
//
//                MessageTraiteePourCommande message = new MessageTraiteePourCommande("123", "Etat1", new Date(), new Date());
//                traiteur.handleMessage(message);
//
//                // Vérifie si l'état existant a été mis à jour dans la commande
//                assert traiteur.getCommande().getEtatList().get(0).getDate_entree() != null;
//                assert traiteur.getCommande().getEtatList().get(0).getDate_sortie() != null;
//            }
//        };
//    }
//
//}
