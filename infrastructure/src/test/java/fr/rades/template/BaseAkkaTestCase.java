package fr.rades.template;

import akka.actor.*;
import akka.event.Logging;
import akka.japi.Creator;
import akka.testkit.TestEvent;
import akka.testkit.javadsl.EventFilter;
import akka.testkit.TestProbe;
import akka.testkit.javadsl.TestKit;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.junit.After;
import org.junit.Before;
import scala.concurrent.Await;

import java.time.Duration;
public abstract class BaseAkkaTestCase {
    protected ActorSystem system;
    @Before
    public void setUp() {
        Config config = ConfigFactory.load();
        system = ActorSystem.create("app-test", config);
    }
    public ActorRef expectActor(TestKit kit, String path) {
        final ActorRef[] actor = {null};

        kit.awaitCond(Duration.ofSeconds(10), Duration.ofMillis(150), "No actor found with " + path, () -> {

            TestProbe probe = new TestProbe(system);

            system.actorSelection(path).tell(new akka.actor.Identify(101), probe.ref());

            ActorIdentity i = probe.expectMsgClass(kit.duration("100 millis"), ActorIdentity.class);

            actor[0] = i.getActorRef().orElse(null);

            return i.getActorRef().isPresent();
        });

        return actor[0];
    }

    public <T extends AbstractActor> ActorRef createActor(Class<T> clazz, String name, Creator<T> factory) {
        Props stub = Props.create(clazz, factory);
        return system.actorOf(stub, name);
    }

    @After
    public void tearDown()  {
        TestKit.shutdownActorSystem(system);
        system = null;
    }
}

