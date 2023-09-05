package fr.rades.template.domain.CommandeActorfile;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.Props;
import fr.rades.template.domain.TraiteurMessagefile.*;
import fr.rades.template.domain.tools.TraiteurDeCommande;
import fr.rades.template.infrastructure.CreateurLotRequetesfile.MessagePourCreateurLot;
import fr.rades.template.infrastructure.interfaces.Etat;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class CommandeActor extends AbstractActor {

    private ActorRef createurLotRequetes;
    private Commande commande;
    private List<ActorRef> listeEnfant; //listeEnfant实际上就是当前actor的所有子actor的引用的列表，
    // 它可以用来向这些子actor发送消息

    public CommandeActor(ActorRef createurLotRequetes) {
        this.createurLotRequetes = createurLotRequetes;
        String id_commande = getSelf().path().name();  //获取当前actor的名称，并把它作为命令的ID

        this.commande = new Commande(id_commande, new ArrayList<>());
        listeEnfant = new ArrayList();

        // Si l'id est un enfant avec un underscore, va chercher le parent
        if (id_commande.contains("_")) {
            ActorSelection parent = getContext().actorSelection("../" + id_commande.substring(0, id_commande.indexOf("_")));
            parent.tell(GetInformationParent.Instance, getSelf());
        }
        // Si la liste d'enfant n'est pas vide et que l'id n'a pas d'underscore, va chercher les enfants
        if (!listeEnfant.isEmpty() && !id_commande.contains("_")) {
            ActorSelection enfant = getContext().actorSelection("../" + id_commande + "_*");
            enfant.tell(ParentCreer.Instance, getSelf());
        }
    }

    //ActorSelection是一个可以引用一个或多个Actor的对象。我们可以向其发送消息，消息会被分派给由ActorSelection引用的所有Actor。
    //
    //getContext().actorSelection()方法是用于从当前actor的上下文中获取一个ActorSelection对象。
    // 参数是一个路径表达式，它描述了如何从当前actor的上下文中找到其他actor。

    public static Props props(ActorRef createurLotRequetes) {
        return Props.create(CommandeActor.class, createurLotRequetes);
    }

    private void sendToCreateurLotRequete() {
        //System.out.println("commande.getEtatList() :"+ commande.getEtatList() +", name: "+ getSelf().path().name());
        MessagePourCreateurLot msg = new MessagePourCreateurLot(commande.getId_cmd(), commande.getEtatList());
        this.createurLotRequetes.tell(msg, getSelf());
    }

    @Override
    public Receive createReceive() {

        return receiveBuilder()
                .match(TraiteurDeMessage.UpdateMessagePourCommandeActor.class, message -> {
                    List<MessageTraiteePourCommande> updatedMessages = message.getMessagesPourCommandeActor();
                    for (MessageTraiteePourCommande updatedMessage : updatedMessages) {
                        TraiteurDeCommande.handleMessage(updatedMessage, this.commande);

                    }
                    sendToCreateurLotRequete();
                    // Si un enfant existe envoie les nouvelles valeurs
                    if (listeEnfant != null) {
                        ResponseInformationParent responseInformationParent = new ResponseInformationParent(commande.getEtatList());
                        for (ActorRef refEnfant : listeEnfant) {
                            refEnfant.tell(responseInformationParent, getSelf());
                        }
                    }
                })

                // 如果发送方是子actor
                .matchEquals(GetInformationParent.Instance, message -> {
                    // Ajoute les enfants à la liste
                    getSender().tell(new ResponseInformationParent(commande.getEtatList()), getSelf());
                    // 是回复发送 GetInformationParent.Instance 请求的 actor（getSender()）一条 ResponseInformationParent 消息，
                    // 这条消息包含了当前 actor 的命令状态列表。

                    listeEnfant.add(getSender());
                    //将发送 GetInformationParent.Instance 请求的 actor 添加到子actor列表 listeEnfant 中。
                })

                // 如果发送方是父actor
                //同步父actor的状态到当前命令的状态中。如果父actor的某个状态在当前命令的状态列表中不存在，就添加进去。
                // 这个操作完成之后，再把命令的状态列表发送给createurLotRequetes。
                .match(ResponseInformationParent.class, message -> {
                    // 当接收到类型为ResponseInformationParent的消息时，
                    // 这段代码首先从消息中获取父actor的状态列表（etatsParent），
                    // 并获取当前命令的状态列表（etatListCommande）
                    ArrayList<Etat> etatsParent = message.getEtatList();
                    ArrayList<Etat> etatListCommande = commande.getEtatList();

                    // Extrait les etats de la liste etatListCommande

                    //然后，使用Java的Stream API，从etatListCommande中抽取出每个Etat对象的nomEtat属性，
                    // 并将它们收集到一个新的List<String>对象listeNomEtat中。
                    List<String> listeNomEtat = etatListCommande.stream()
                            .map(etat -> etat.getNomEtat())
                            .collect(Collectors.toList());

                    // Pour chaque Etat de la liste etatsParent

                    //接着，代码遍历etatsParent列表，对于每一个父actor的状态，
                    // 如果它的名称nomEtatParent不在listeNomEtat中，
                    // 那么就将它复制（copieCommande）并添加到etatListCommande列表中。
                    for (Etat etatParent : etatsParent) {
                        String nomEtatParent = etatParent.getNomEtat();
                        //System.out.println("nomEtatParent : "+ nomEtatParent + ", name : "+ getSelf().path().name()+ ",listeNomEtat.contains(nomEtatParent) :"+listeNomEtat.contains(nomEtatParent) + ", listeNomEtat :" +listeNomEtat);

//                        if ("flux_vers_step2".equals(nomEtatParent)){
//                            System.out.println("date debut flux : "+ etatParent.getDateDebut() + ", name : "+ getSelf().path().name()+ ",listeNomEtat.contains(nomEtatParent) :"+listeNomEtat.contains(nomEtatParent) + ", listeNomEtat :" +listeNomEtat);
//                        }


                        if (!listeNomEtat.contains(nomEtatParent)) {

                            //correct
//                            etatListCommande.add(etatParent.copieCommande());

                            //à faire pour duplicate
                            commande.addEtat(etatParent.copieCommande());
                        }else{
                            Etat etatEnfant= findEtat(etatListCommande, nomEtatParent);
                            if(etatEnfant.getDateDebut()==null && etatEnfant.getDateFin()==null){

                            }else if(etatEnfant.getDateDebut()!=null && etatEnfant.getDateFin()!=null){

                            }
                            else if (etatEnfant.getDateDebut()==null && etatEnfant.getDateFin()!=null) {
                                if (etatParent.getDateDebut()!=null){
                                    etatEnfant.setDateDebut(etatParent.getDateDebut());
                                }
                            } else if (etatEnfant.getDateFin()==null && etatEnfant.getDateDebut()!=null){
                                if (etatParent.getDateFin()!=null && etatEnfant.getDateDebut().before(etatParent.getDateFin())){
                                    etatEnfant.setDateFin(etatParent.getDateFin());
                                }
                            }

                        }
                    }
                    //最后，调用sendToCreateurLotRequete方法，向createurLotRequetes发送一条MessagePourCreateurLot类型的消息。
                    sendToCreateurLotRequete();
                })


                /*// pour tester l'erreur de duplicate （替代 match(ResponseInformationParent.class, message）
                .match(ResponseInformationParent.class, message -> {
                    ArrayList<Etat> etatsParent = message.getEtatList();
                    for (Etat etat : etatsParent) {
                        if (!commande.getEtatList().contains(etat.getNomEtat())) {
                            if (commande.getEtatList() != null) {
                                    commande.addEtat(etat.copieCommande());
                            sendToCreateurLotRequete();
                            }


                        }
                    }
                })*/


                // Pour dire aux enfants que le parent est cree

                //这种设计的目的可能是为了在父actor创建后，让父actor能通知其所有子actor，父actor已经被创建。
                // 这样子actor在需要与父actor交互时，就知道父actor已经准备好了，可以向其发送消息或者请求。

                .matchEquals(ParentCreer.Instance, message -> {
                    // 当CommandeActor收到一个ParentCreer.Instance消息时，
                    // 它会向消息的发送者（子actor）回复一个GetInformationParent.Instance消息。
                    getSender().tell(GetInformationParent.Instance, getSelf());

                })

                .build();
    }

    private static Etat findEtat(ArrayList<Etat> etatListCommande, String nomEtatParent) {
        for(Etat etatEnfant : etatListCommande){
            if(etatEnfant.getNomEtat().equals(nomEtatParent)) {
                return etatEnfant;
            }
        }
        return null;
    }

}




