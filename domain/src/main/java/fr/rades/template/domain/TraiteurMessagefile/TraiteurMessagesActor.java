package fr.rades.template.domain.TraiteurMessagefile;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import fr.rades.template.infrastructure.tronqueurMessage.message.MessageTronqueurTraiteur;

public class TraiteurMessagesActor extends AbstractActor {
    private TraiteurDeMessage traiteurDeMessage;
    private ActorRef createurLotRequetes;

    public TraiteurMessagesActor(ActorRef createurLotRequetes) {

        this.traiteurDeMessage = new TraiteurDeMessage();
        this.createurLotRequetes = createurLotRequetes;
    }

    public static Props props(ActorRef createurLotRequetes) {
        return Props.create(TraiteurMessagesActor.class, () -> new TraiteurMessagesActor(createurLotRequetes));
    }


    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(MessageTronqueurTraiteur.class, message -> {
                    traiteurDeMessage.processMessage(message, getContext(), 0,createurLotRequetes );
                })
                .build();
    }

}

