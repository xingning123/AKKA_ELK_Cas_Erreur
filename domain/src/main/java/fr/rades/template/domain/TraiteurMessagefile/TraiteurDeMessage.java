package fr.rades.template.domain.TraiteurMessagefile;

import akka.actor.ActorContext;
import akka.actor.ActorRef;
import fr.rades.template.domain.CommandeActorfile.CommandeActor;
import fr.rades.template.infrastructure.tronqueurMessage.InfosMessageEtat;
import fr.rades.template.infrastructure.tronqueurMessage.message.MessageTronqueurTraiteur;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;


public class TraiteurDeMessage {
    public Map<String, ActorRef> idCommandeMap;
    private List<MessageTraiteePourCommande> processedMessages;
    private boolean isPreExistingMessageAdded = false;
    int i = 1;
    private final int MAX_RETRY_COUNT = 5;

    public TraiteurDeMessage() {
        idCommandeMap = new HashMap<>();
        this.processedMessages = new ArrayList<>();

        isPreExistingMessageAdded(); // si on ne le met pas, le premier message reçu n'est pas traité
    }

    public void processMessage(MessageTronqueurTraiteur message, ActorContext context, int retryCount,ActorRef createurLotRequetes) {
        String idCommande = message.getId_commande();
        List<MessageTraiteePourCommande> processedMessages = new ArrayList<>();

        ActorRef latestCommandeActor;

        try {
            if (isIdCommandeExists(idCommande)) {
                latestCommandeActor = idCommandeMap.get(idCommande);
                updateCommandeActor(latestCommandeActor, processedMessages, context);
            } else {
                latestCommandeActor = createCommandeActor(idCommande,createurLotRequetes,context);
                latestCommandeActor.tell(new UpdateMessagePourCommandeActor(processedMessages), context.self());
            }

            for (InfosMessageEtat info : message.getListeInfosEtats()) {
                MessageTraiteePourCommande messageTraitee = new MessageTraiteePourCommande(
                        message.getId_commande(),
                        info.getNomEtat(),
                        info.getDate_entree(),
                        info.getDate_sortie()
                );
                messageTraitee.setIdCommande(idCommande);
                messageTraitee.setNomEtat(info.getNomEtat());
                if (info.getDate_entree() != null) {
                    messageTraitee.setDateEntree(info.getDate_entree());
                }
                if (info.getDate_sortie() != null) {
                    messageTraitee.setDateSortie(info.getDate_sortie());
                }

                processedMessages.add(messageTraitee);
            }
        } catch (Exception e) {
            System.out.println("Error processing message: " + e.getMessage());
            e.printStackTrace();

            if (retryCount < MAX_RETRY_COUNT) {
                System.out.println("Retrying to process the message...");
                // Essaie de retraiter le message un certain nombre de fois
                processMessage(message, context, retryCount + 1, createurLotRequetes);
            } else {
                System.out.println("Max retry count reached. Unable to process the message.");
            }
        }

    }

    public boolean isIdCommandeExists(String idCommande) {
        return idCommandeMap.containsKey(idCommande);
    }

    public List<MessageTraiteePourCommande> getProcessedMessages() {
        return processedMessages;
    }

    // Pour transmettre la liste de messages mise à jour par l'acteur TraiteurDeMessage à l'acteur CommandeActor.
    public static class UpdateMessagePourCommandeActor {
        private List<MessageTraiteePourCommande> messagesPourCommandeActor;

        public UpdateMessagePourCommandeActor(List<MessageTraiteePourCommande> messagesPourCommandeActor) {
            this.messagesPourCommandeActor = messagesPourCommandeActor;
        }

        public List<MessageTraiteePourCommande> getMessagesPourCommandeActor() {
            return messagesPourCommandeActor;
        }

        @Override
        public String toString() {
            if (messagesPourCommandeActor == null || messagesPourCommandeActor.isEmpty()) {
                return "No messages processed yet.";
            } else {
                StringBuilder builder = new StringBuilder();
                for (MessageTraiteePourCommande message : messagesPourCommandeActor) {
                    builder.append(message.toString()).append("\n");
                }
                return builder.toString();
            }
        }
    }

    public void updateCommandeActor(ActorRef actorRef, List<MessageTraiteePourCommande> messages, ActorContext context) {
        idCommandeMap.put(actorRef.path().name(), actorRef);
        processedMessages.addAll(messages);
        actorRef.tell(new UpdateMessagePourCommandeActor(messages), context.self());
    }

    public boolean isPreExistingMessageAdded() {
        if (i == 1) {
            MessageTraiteePourCommande preExistingMessage = new MessageTraiteePourCommande("IdCommandeTest", "NomEtatTest", null, null);
            i++;
            return isPreExistingMessageAdded = true;
        } else return false;
    }

    public ActorRef createCommandeActor(String idCommande, ActorRef createurLotRequetes, ActorContext context) {
        ActorRef latestCommandeActor = context.actorOf(CommandeActor.props(createurLotRequetes), idCommande);
        idCommandeMap.put(idCommande, latestCommandeActor);
        return latestCommandeActor;
    }

}
