package fr.rades.template.infrastructure.recuperateurLogstash;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.io.Tcp;
import akka.io.TcpMessage;
import akka.util.ByteString;
import fr.rades.template.infrastructure.recuperateurLogstash.message.MessageRecuperateurTronqueur;

public class RecuperateurLogstashActeur extends AbstractActor {

    private ActorRef managerTcp;
    private ConnecteurTcpClasse connecteurTcpClasse;
    private ActorRef tronqueurMessageActeur;

    public static Props props(ActorRef tronqueurMessageActeur) {

        return  Props.create(RecuperateurLogstashActeur.class, tronqueurMessageActeur);

    }

    public RecuperateurLogstashActeur(ActorRef tronqueurMessageActeur) {
        this.connecteurTcpClasse = new ConnecteurTcpClasse();
        this.tronqueurMessageActeur = tronqueurMessageActeur;
        this.managerTcp = Tcp.get(getContext().getSystem()).manager();
        this.managerTcp.tell(TcpMessage.bind(getSelf(), this.connecteurTcpClasse.getAdresse_tcp(),
                this.connecteurTcpClasse.getNbreMessagesMaxEnQueue()),getSelf());
    }

    public MessageRecuperateurTronqueur donneesLogstashEnMessageTronqueur(Tcp.Received messageLogstash) {
        ByteString donneerecue = messageLogstash.data();
        String donnerecueEnString = donneerecue.utf8String();
       MessageRecuperateurTronqueur messageRecuperateurTronqueur = new MessageRecuperateurTronqueur(donnerecueEnString);
        return messageRecuperateurTronqueur;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Tcp.Bound.class, bound -> {
                    System.out.println(this.connecteurTcpClasse.toString());
                })
                .match(Tcp.Connected.class, connected -> {
                    getSender().tell(TcpMessage.register(getSelf()),getSelf());
                    System.out.println("CONNEXION TCP REUSSIE \n");
                })
                .match(Tcp.Received.class , received -> {
                    MessageRecuperateurTronqueur messageRecuperateurTronqueur =
                            donneesLogstashEnMessageTronqueur(received);
                    this.tronqueurMessageActeur.tell(messageRecuperateurTronqueur, getSelf());
                })
                .build();
    }
}
