//package fr.rades.template;
//
//import akka.actor.ActorContext;
//import akka.actor.Props;
//import akka.testkit.TestProbe;
//import akka.testkit.javadsl.TestKit;
//import fr.rades.template.domain.*;
//import fr.rades.template.domain.CommandeActorfile.CommandeActor;
//import fr.rades.template.domain.TraiteurMessagefile.MessageTraiteePourCommande;
//import fr.rades.template.domain.TraiteurMessagefile.TraiteurDeMessage;
//import fr.rades.template.domain.TraiteurMessagefile.TraiteurMessagesActor;
//import fr.rades.template.domain.tools.TraiteurDeCommande;
//import fr.rades.template.infrastructure.tronqueurMessage.InfosMessageEtat;
//import fr.rades.template.infrastructure.tronqueurMessage.message.MessageTronqueurTraiteur;
//import org.junit.AfterClass;
//import org.junit.BeforeClass;
//import org.junit.Test;
//
//import java.time.Duration;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import akka.actor.ActorRef;
//import akka.actor.ActorSystem;
//
//import static org.junit.Assert.*;
//import static org.junit.Assert.assertNotNull;
//
//public class TraiteurMessageActorTest {
//    private static ActorSystem system;
//    private static TestProbe probe;
//    private ActorRef createurLotRequetes;
//    private TraiteurDeCommande traiteurDeCommande;
//
//    @BeforeClass
//    public static void setup() {
//        system = ActorSystem.create();
//        probe = new TestProbe(system);
//    }
//
//    @AfterClass
//    public static void teardown() {
//        TestKit.shutdownActorSystem(system);
//        system = null;
//    }
//
//    // Message venant de MessageTronqueurTraiteur
//    public MessageTronqueurTraiteur creationMessageTronqueurTraiteur() {
//        String idCommande = "ABC123";
//        Date timestamp = new Date();
//        ArrayList<InfosMessageEtat> listeInfosEtats = new ArrayList<>();
//
//        InfosMessageEtat infosMessageEtat1 = new InfosMessageEtat("Etat 1", new Date(), new Date(),true, true);
//        listeInfosEtats.add(infosMessageEtat1);
//
//        InfosMessageEtat infosMessageEtat2 = new InfosMessageEtat("Etat 2", null, new Date(), false, true);
//        listeInfosEtats.add(infosMessageEtat2);
//
//        return new MessageTronqueurTraiteur(idCommande, timestamp, listeInfosEtats);
//    }
//
//    public MessageTronqueurTraiteur creationMessageTronqueurTraiteur2() {
//        String idCommande = "ABC123";
//        Date timestamp = new Date();
//        ArrayList<InfosMessageEtat> listeInfosEtats = new ArrayList<>();
//
//        InfosMessageEtat infosMessageEtat1 = new InfosMessageEtat("Etat 3", new Date(), null, true, false);
//        listeInfosEtats.add(infosMessageEtat1);
//
//        InfosMessageEtat infosMessageEtat2 = new InfosMessageEtat("Etat 4", new Date(), new Date(), true, true);
//        listeInfosEtats.add(infosMessageEtat2);
//
//        return new MessageTronqueurTraiteur(idCommande, timestamp, listeInfosEtats);
//    }
//
//    public MessageTronqueurTraiteur creationMessageTronqueurTraiteur3() {
//        String idCommande = "AAA";
//        Date timestamp = new Date();
//        ArrayList<InfosMessageEtat> listeInfosEtats = new ArrayList<>();
//
//        InfosMessageEtat infosMessageEtat1 = new InfosMessageEtat("Etat 1", null, new Date(), false, true);
//        listeInfosEtats.add(infosMessageEtat1);
//
//        InfosMessageEtat infosMessageEtat2 = new InfosMessageEtat("Etat 2", new Date(), null, true, false);
//        listeInfosEtats.add(infosMessageEtat2);
//
//        return new MessageTronqueurTraiteur(idCommande, timestamp, listeInfosEtats);
//    }
//
//    // Test vérifiant que le commandeActor est bien créé
//    @Test
//    public void testCommandeActorCreation() {
//        new TestKit(system) {{
//            TraiteurDeMessage traiteurDeMessage = new TraiteurDeMessage(getRef(), system, createurLotRequetes, traiteurDeCommande) {
//                @Override
//                public ActorRef createCommandeActor(String idCommande) {
//                    idCommandeMap.put(idCommande, getRef());
//                    return getRef();
//                }
//                public ActorRef createCommandeActor(String idCommande, ActorRef createurLotRequetes, ActorContext context) {
//                    idCommandeMap.put(idCommande, getRef());
//                    return getRef();
//                }
//            };
//
//            ActorRef logReaderActor = system.actorOf(TraiteurMessagesActor.props(createurLotRequetes));
//            MessageTronqueurTraiteur message = creationMessageTronqueurTraiteur();
//            logReaderActor.tell(message, probe.ref());
//            expectMsgClass(TraiteurDeMessage.UpdateMessagePourCommandeActor.class);
//
//        }};
//    }
//
//
//    // Test vérifiant si l'id exist
//    @Test
//    public void testCommandeActorExist() {
//        new TestKit(system) {{
//            TraiteurDeMessage traiteurDeMessage = new TraiteurDeMessage(getRef(), system, createurLotRequetes, traiteurDeCommande) {
//                @Override
//                public ActorRef createCommandeActor(String idCommande) {
//                    idCommandeMap.put(idCommande, getRef());
//                    return getRef();
//                }
//
//                @Override
//                public boolean isIdCommandeExists(String idCommande) {
//                    assert idCommande == "ABC123";
//                    System.out.println("the id is : " + idCommande);
//                    return idCommandeMap.containsKey(idCommande);
//                }
//            };
//
//            ActorRef logReaderActor = system.actorOf(TraiteurMessagesActor.props(createurLotRequetes));
//            MessageTronqueurTraiteur message = creationMessageTronqueurTraiteur();
//            logReaderActor.tell(message, probe.ref());
//            expectMsgClass(TraiteurDeMessage.UpdateMessagePourCommandeActor.class);
//        }};
//    }
//
//
//    // Test de la methode IsPreExistingMessageAdded ; permet de s'assurer que le 1er message reçu dans LogReaderActor est bien traité
//    @Test
//    public void testIsPreExistingMessageAdded() {
//        new TestKit(system) {{
//            TraiteurDeMessage traiteurDeMessage = new TraiteurDeMessage(getRef(), system, createurLotRequetes, traiteurDeCommande) {
//                @Override
//                public ActorRef createCommandeActor(String idCommande) {
//                    idCommandeMap.put(idCommande, getRef());
//                    return getRef();
//                }
//            };
//
//            ActorRef logReaderActor = system.actorOf(TraiteurMessagesActor.props(createurLotRequetes));
//            MessageTronqueurTraiteur message = creationMessageTronqueurTraiteur();
//            logReaderActor.tell(message, probe.ref());
//
//            boolean isPreExistingMessageAdded = traiteurDeMessage.isPreExistingMessageAdded();
//            assertFalse(isPreExistingMessageAdded);
//        }};
//    }
//
//
//    // Test verifiant que les valeurs dans UpdateMessagePourCommandeActor ne sont pas nulle
//    @Test
//    public void testVerificationDesValeursDansUpdateMessagePourCommandeActor() {
//        new TestKit(system) {{
//            TraiteurDeMessage traiteurDeMessage = new TraiteurDeMessage(getRef(), system, createurLotRequetes, traiteurDeCommande) {
//                @Override
//                public ActorRef createCommandeActor(String idCommande) {
//                    idCommandeMap.put(idCommande, getRef());
//                    return getRef();
//                }
//            };
//
//            ActorRef logReaderActor = system.actorOf(TraiteurMessagesActor.props(createurLotRequetes));
//            MessageTronqueurTraiteur message = creationMessageTronqueurTraiteur();
//            logReaderActor.tell(message, probe.ref());
//
//            TraiteurDeMessage.UpdateMessagePourCommandeActor updateMessage = expectMsgClass(TraiteurDeMessage.UpdateMessagePourCommandeActor.class);
//            List<MessageTraiteePourCommande> messagesReceived = updateMessage.getMessagesPourCommandeActor();
//
//            assertNotNull(messagesReceived);
//            assertFalse(messagesReceived.isEmpty());
//
//        }};
//    }
//
//    // Test vérifiant si l'idCommande existant reçoit les nouvelles valeurs
//    @Test
//    public void testCommandeActorUpdate() {
//        new TestKit(system) {{
//            TraiteurDeMessage traiteurDeMessage = new TraiteurDeMessage(getRef(), system, createurLotRequetes, traiteurDeCommande) {
//                @Override
//                public ActorRef createCommandeActor(String idCommande) {
//                    idCommandeMap.put(idCommande, getRef());
//                    return getRef();
//                }
//                @Override
//                public boolean isIdCommandeExists(String idCommande) {
//                    assert idCommande == "ABC123";
//                    System.out.println("the id is : " + idCommande);
//                    return idCommandeMap.containsKey(idCommande);
//                }
//                @Override
//                public void updateCommandeActor(ActorRef actorRef, List<MessageTraiteePourCommande> messages, ActorContext context) {
//                    idCommandeMap.put(actorRef.path().name(), actorRef);
//                    actorRef.tell(new UpdateMessagePourCommandeActor(messages), context.self());
//                }
//            };
//
//            ActorRef logReaderActor = system.actorOf(TraiteurMessagesActor.props(createurLotRequetes));
//            MessageTronqueurTraiteur message = creationMessageTronqueurTraiteur();
//            MessageTronqueurTraiteur message2 = creationMessageTronqueurTraiteur2();
//            logReaderActor.tell(message, probe.ref());
//            logReaderActor.tell(message2, probe.ref());
//            boolean isPreExistingMessageAdded = traiteurDeMessage.isPreExistingMessageAdded();
//            assertFalse(isPreExistingMessageAdded);
//            expectMsgClass(TraiteurDeMessage.UpdateMessagePourCommandeActor.class);
//        }};
//    }
//
//}
