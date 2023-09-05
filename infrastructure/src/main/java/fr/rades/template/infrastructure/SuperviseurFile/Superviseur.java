package fr.rades.template.infrastructure.SuperviseurFile;

import akka.actor.ActorRef;
import akka.actor.Props;
import fr.rades.template.domain.TraiteurMessagefile.TraiteurMessagesActor;
import fr.rades.template.infrastructure.CreateurLotRequetesfile.CreateurLotRequetes;
import fr.rades.template.infrastructure.ElasticSearchActorfile.ElasticsearchActor;
import fr.rades.template.infrastructure.tronqueurMessage.TronqueurMessageActeur;
import fr.rades.template.infrastructure.recuperateurLogstash.RecuperateurLogstashActeur;


public class Superviseur extends SettingsActor {

    private final ActorRef elasticsearchactor = creerElasticsearchactor();

    private final ActorRef createurLotRequetes = creerCreateurLotRequetes(elasticsearchactor);

    private final ActorRef traiteurMessagesActor = creerTraiteurMessagesActor(createurLotRequetes);

    private final ActorRef tronqueurMessageActeur = creerTronqueurMessageActeur(traiteurMessagesActor);

    private final ActorRef recuperateurLogstashActeur = creerRecuperateurLogstashActeur(tronqueurMessageActeur);


    public static Props props(){
        return Props.create(Superviseur.class, () -> new Superviseur());
    }

    private ActorRef creerElasticsearchactor() {
        return context().actorOf(ElasticsearchActor.props(settings.url, settings.username, settings.password), "elasticsearchactor");
    }
    protected ActorRef creerCreateurLotRequetes(ActorRef ingecteurElasticsearch){
        return context().actorOf(CreateurLotRequetes.props(settings.nombreRequetesLimite ,  settings.lotDuration , ingecteurElasticsearch , settings.endPoint ).withDispatcher("control-aware-dispatcher"),"createurLotRequetes");
    }
    private ActorRef creerTraiteurMessagesActor(ActorRef createurLotRequetes) {
        return context().actorOf(TraiteurMessagesActor.props(createurLotRequetes),"traiteurMessagesActor");
    }

    private ActorRef creerTronqueurMessageActeur(ActorRef traiteurMessagesActor) {
        return context().actorOf(TronqueurMessageActeur.props(traiteurMessagesActor),"tronqueurMessageActeur");
    }

    private ActorRef creerRecuperateurLogstashActeur(ActorRef tronqueurMessageActeur) {
        return context().actorOf(RecuperateurLogstashActeur.props(tronqueurMessageActeur),"recuperateurLogstashActeur");
    }


    @Override
    public Receive createReceive() {
        return receiveBuilder()
        .build();
    }
}
