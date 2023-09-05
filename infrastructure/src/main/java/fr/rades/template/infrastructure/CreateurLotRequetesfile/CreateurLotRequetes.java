package fr.rades.template.infrastructure.CreateurLotRequetesfile;

import akka.actor.*;
import fr.rades.template.infrastructure.interfaces.documentEnRequete ;
import scala.concurrent.duration.FiniteDuration;


public class  CreateurLotRequetes extends AbstractLoggingActor implements documentEnRequete {

    private ActorRef ingecteurElasticsearch;
    private final String endPoint;
    private String corps = "";
    private final FiniteDuration lotDuration ;
    private final int nombreRequetesLimite ;
    private int nombreRequetes = 0;
    private Cancellable cancellable;

    public CreateurLotRequetes(int nombreRequetesLimite,  FiniteDuration lotDuration, ActorRef ingecteurElasticsearch,String endPoint) {
        this.nombreRequetesLimite = nombreRequetesLimite;
        this.lotDuration = lotDuration;
        this.ingecteurElasticsearch = ingecteurElasticsearch;
        this.endPoint = endPoint;
        cancellable = context().system().scheduler().scheduleOnce(lotDuration, self(), MessageTimeOver.Instance, context().dispatcher(), self());
    }

        public static Props props(final int nombreRequetesLimite, final  FiniteDuration lotDuration, final ActorRef ingecteurElasticsearch,final String endPoint){
        return Props.create(CreateurLotRequetes.class, () -> new CreateurLotRequetes(nombreRequetesLimite, lotDuration,ingecteurElasticsearch,endPoint));
    }

    private void regrouper(MessagePourCreateurLot o) {
        String requete = formerRequete(o);
        if (requete !="") {
            this.nombreRequetes++;
            this.corps = this.corps + requete;
        }
    }

    private void scheduleTimeOver(){
        this.cancellable.cancel();
        this.cancellable = context().system().scheduler().scheduleOnce(lotDuration, self(), MessageTimeOver.Instance, context().dispatcher(), self());
    }

    private void envoyerEtReinitialiserLot() {
        if (this.corps != "") {
            MessageDeCreateurLot messageDeCreateurLot = new MessageDeCreateurLot(this.endPoint, this.corps);
            this.ingecteurElasticsearch.tell(messageDeCreateurLot, getSelf());
            this.corps = "";
            this.nombreRequetes = 0;
            scheduleTimeOver();
        }
        else scheduleTimeOver();
    }

    public ActorRef getIngecteurElasticsearch() {
        return this.ingecteurElasticsearch;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(MessagePourCreateurLot.class, message -> this.nombreRequetes == this.nombreRequetesLimite-1, message -> {regrouper(message); envoyerEtReinitialiserLot();})
                .match(MessagePourCreateurLot.class, message -> {regrouper(message);})
                .match(MessageTimeOver.class, message -> {envoyerEtReinitialiserLot();})
                .build();
    }
}

