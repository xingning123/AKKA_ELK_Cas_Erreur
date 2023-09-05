package fr.rades.template.infrastructure.tronqueurMessage.message;

import fr.rades.template.infrastructure.tronqueurMessage.InfosMessageEtat;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public final class MessageTronqueurTraiteur {

    private final String id_commande;
    private final Date timestamp;
    private final ArrayList<InfosMessageEtat> listeInfosEtats;


    public MessageTronqueurTraiteur(String id_commande, Date timestamp, ArrayList<InfosMessageEtat> listeInfosEtats) {
        this.id_commande = id_commande;
        this.timestamp = timestamp;
        this.listeInfosEtats = listeInfosEtats;
    }

    public String getId_commande() {
        return id_commande;
    }


    public Date getTimestamp() {
        return timestamp;
    }


    public ArrayList<InfosMessageEtat> getListeInfosEtats() {
        return listeInfosEtats;
    }


    @Override
    public String toString() {

        String messageTronqueur =  "MessageTronqueurTraiteur{" +
                "id_commande='" + id_commande + '\'' +
                ", timestamp=" + timestamp + "\n";

        for (InfosMessageEtat infosMessageEtat : this.listeInfosEtats) {
            messageTronqueur +=  infosMessageEtat.toString() + "\n";
        }
        messageTronqueur += "\n\n";
        return messageTronqueur;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageTronqueurTraiteur that = (MessageTronqueurTraiteur) o;
        return Objects.equals(id_commande, that.id_commande) && Objects.equals(timestamp, that.timestamp) && Objects.equals(listeInfosEtats, that.listeInfosEtats);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_commande, timestamp, listeInfosEtats);
    }
}
