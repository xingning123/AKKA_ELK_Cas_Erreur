//package fr.rades.template;
//
//import akka.actor.ActorRef;
//import akka.actor.ActorSystem;
//import akka.testkit.TestKit;
//import akka.testkit.TestProbe;
//import fr.rades.template.domain.CommandeActorfile.CommandeActor;
//import fr.rades.template.domain.tools.TraiteurDeCommande;
//import fr.rades.template.domain.TraiteurMessagefile.MessageTraiteePourCommande;
//import org.junit.AfterClass;
//import org.junit.BeforeClass;
//import org.junit.Test;
//import scala.concurrent.duration.Duration;
//import scala.concurrent.duration.FiniteDuration;
//
//import java.util.Date;
//import java.util.concurrent.TimeUnit;
//
//import static org.junit.Assert.assertNotNull;
//
//
//public class CommandeActorTest {
//
//    private static ActorSystem system;
//    private TestProbe probe;
//
//    @BeforeClass
//    public static void setup() {
//        system = ActorSystem.create();
//
//    }
//
//    @AfterClass
//    public static void teardown() {
//        FiniteDuration duration = Duration.create(10, TimeUnit.SECONDS);
//        TestKit.shutdownActorSystem(system, duration, true);
//        system = null;
//    }
//
//
//   @Test
//   public void testCommandeActor() {
//       new TestKit(system) {
//           {
//               probe = new TestProbe(system);
//
//               // 创建 TraiteurDeCommande 实例
//               TraiteurDeCommande traiteurDeCommande = new TraiteurDeCommande(probe.ref());
//
//               // 创建 commandeActor
//               ActorRef commandeActor = system.actorOf(CommandeActor.props(probe.ref(), traiteurDeCommande));
//
//               // 创建要发送给 CommandeActor 的消息
//               MessageTraiteePourCommande message = new MessageTraiteePourCommande("ABC123",
//                       "Etape2",
//                       new Date(),
//                       new Date());
//
//               // 发送消息给 commandeActor
//               commandeActor.tell(message, probe.ref());
//
//               //probe.expectMsg(FiniteDuration.create(3, TimeUnit.SECONDS),Commande.class);
//
//
//               assertNotNull(probe.ref());
//
////
//               // 等待一段时间以确保消息被处理
//               expectNoMessage(FiniteDuration.create(3, TimeUnit.SECONDS));
//
//               // 验证接收到的 Commande 对象内容是否与 message 对象相同
//
////               assertEquals(message.getIdCommande(), commande.getId_cmd());
////               assertEquals(message.getNomEtat(), commande.getEtatList().get(0).getNom_etat());
////               assertEquals(message.getDateEntree(), commande.getEtatList().get(0).getDate_entree());
////               assertEquals(message.getDateSortie(), commande.getEtatList().get(0).getDate_sortie());
//
//
//           }
//
//
//       };
//   }
//
//
//
//
//}