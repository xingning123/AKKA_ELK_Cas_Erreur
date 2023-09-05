package fr.rades.template;

import akka.actor.ActorRef;
import akka.testkit.javadsl.TestKit;
import fr.rades.template.infrastructure.CreateurLotRequetesfile.CreateurLotRequetes;
import fr.rades.template.infrastructure.CreateurLotRequetesfile.MessagePourCreateurLot;
import fr.rades.template.infrastructure.CreateurLotRequetesfile.MessageDeCreateurLot;
import fr.rades.template.infrastructure.CreateurLotRequetesfile.MessageTimeOver;
import fr.rades.template.infrastructure.interfaces.Etat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


import org.junit.Test;
import scala.concurrent.duration.FiniteDuration;

import java.sql.Date;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

public class CreateurLotRequetesTest extends BaseAkkaTestCase {

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void envoyerRequeteApresLotDurationSansAtteindreNombreRequetesLimite() {
        new TestKit(system) {{
            int dureeLot = 6;
            int nombreRequetesLimite = 3;
            ActorRef createurLotRequetes = createCreateurLotRequetes(this,nombreRequetesLimite, this.duration("6 seconds"));

            within(Duration.ofSeconds(dureeLot), Duration.ofSeconds(dureeLot+1), () -> {

                // Envoyer 2 messages (moins que la limite 3) a l'actor createurLotRequetes et attendre qu'il envoie juste apres la fin de la duree de lot.
                createurLotRequetes.tell(exempleMessagePourCreateurLot(this), system.deadLetters());
                createurLotRequetes.tell(exempleMessagePourCreateurLot(this), system.deadLetters());
                final MessageDeCreateurLot m = expectMsgClass(MessageDeCreateurLot.class);
                try {
                    String jsonString = objectMapper.writeValueAsString(m);
                    System.out.println(jsonString);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                return null;
            });
        }};
    }

    @Test
    public void nePasEnvoyerRequeteAvantLotDurationSansAtteindreNombreRequetesLimite() {
        new TestKit(system) {{
            int dureeLot = 6;
            int nombreRequetesLimite = 3;
            ActorRef createurLotRequetes = createCreateurLotRequetes(this,nombreRequetesLimite, this.duration("6 seconds"));

            // Envoyer 2 messages (moins que la limite) a l'actor createurLotRequetes et attendre qu'il n'en envoie plus avant la fin de la duree de lot.
            createurLotRequetes.tell(exempleMessagePourCreateurLot(this), system.deadLetters());
            createurLotRequetes.tell(exempleMessagePourCreateurLot(this), system.deadLetters());
            expectNoMessage(Duration.ofSeconds(dureeLot));
        }};
    }

    @Test
    public void envoyerRequeteAvantLotDurationApresAtteindreNombreRequetesLimite() {
        new TestKit(system) {{
            int dureeLot = 6;
            int nombreRequetesLimite = 3;
            ActorRef createurLotRequetes = createCreateurLotRequetes(this,nombreRequetesLimite, this.duration("6 seconds"));

            // Envoyer 3 messages et attendre qu'il envoie un message à l'injecteur elascisearch avant que la durée du lot ne soit atteinte.
            createurLotRequetes.tell(exempleMessagePourCreateurLot(this), system.deadLetters());
            createurLotRequetes.tell(exempleMessagePourCreateurLot(this), system.deadLetters());
            createurLotRequetes.tell(exempleMessagePourCreateurLot(this), system.deadLetters());
            expectMsgClass(Duration.ofSeconds(dureeLot-1), MessageDeCreateurLot.class);
        }};
    }

    private MessagePourCreateurLot exempleMessagePourCreateurLot(TestKit kit) {
        Etat etat = new Etat("step1");
        etat.setDateDebut(Date.from(Instant.ofEpochSecond(System.currentTimeMillis())));
        Etat etaat = new Etat("step1");
        etaat.setDateFin(Date.from(Instant.ofEpochSecond(System.currentTimeMillis())));
        ArrayList<Etat> etats = new ArrayList<Etat>();
        etats.add(etat);
        etats.add(etaat);
        MessagePourCreateurLot exemple = new MessagePourCreateurLot("Cmd1", etats);
        return exemple;
    }

    @Test
    public void prioriserMessageTimeOver() {
        new TestKit(system) {{
            ActorRef createurLotRequetes = createCreateurLotRequetes(this,3, this.duration("6 seconds"));

            createurLotRequetes.tell(exempleMessagePourCreateurLot(this), getRef());
            createurLotRequetes.tell(new MessageTimeOver(), getRef());

            expectNoMessage(Duration.ofSeconds(6));
            createurLotRequetes.tell(new MessageTimeOver(), getRef());
            expectMsgClass(MessageDeCreateurLot.class);

        }};
    }


    private ActorRef createCreateurLotRequetes(TestKit kit, int nombreRequetesLimite, FiniteDuration dureeLot) {
        ActorRef guest = system.actorOf(CreateurLotRequetes.props(nombreRequetesLimite, dureeLot , kit.getRef(),"/_bulk").withDispatcher("control-aware-dispatcher"));
        return guest;
    }

    }
