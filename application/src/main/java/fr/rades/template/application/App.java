package fr.rades.template.application;

import fr.rades.template.infrastructure.SuperviseurFile.Superviseur;
import akka.actor.ActorSystem;
import akka.actor.ActorRef;


public class App {

    private final ActorSystem system;
    private final ActorRef superviseur;

    public App(final ActorSystem system){
        this.system = system;
        this.superviseur = system.actorOf(Superviseur.props());
    }

    public static void main(String[] args) {

        final ActorSystem actorSystem = ActorSystem.create("actorSystem");

        final App app = new App(actorSystem);

    }
}
