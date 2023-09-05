package fr.rades.template;


import akka.testkit.javadsl.TestKit;
import fr.rades.template.infrastructure.SuperviseurFile.Superviseur;
import org.junit.Test;

public class SuperviseurTest extends BaseAkkaTestCase {

    @Test
    public void shouldCreateChildActorCalledIngecteurElasticsearchWhenCreated() {
        new TestKit(system) {{
            system.actorOf(Superviseur.props(), "create-actor");
            expectActor(this, "/user/create-actor/ingecteurElasticsearch");
        }};
    }

    @Test
    public void shouldCreateChildActorCalledCreateurLotRequetesWhenCreated() {
        new TestKit(system) {{
            system.actorOf(Superviseur.props(), "create-actor");
            expectActor(this, "/user/create-actor/createurLotRequetes");
        }};
    }
}
