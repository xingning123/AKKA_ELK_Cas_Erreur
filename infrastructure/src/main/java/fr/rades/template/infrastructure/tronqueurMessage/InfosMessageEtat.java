package fr.rades.template.infrastructure.tronqueurMessage;


import java.util.Date;
import java.util.Objects;

public final class InfosMessageEtat {

    private final String nomEtat;
    private final Date date_entree;
    private final Date date_sortie;
    private final boolean presenceDateEntree;
    private final boolean presenceDateSortie;


    public InfosMessageEtat(String nomEtat, Date date_entree, Date date_sortie,
                            boolean presenceDateEntree, boolean presenceDateSortie) {
        this.nomEtat = nomEtat;
        this.date_entree = date_entree;
        this.date_sortie = date_sortie;
        this.presenceDateEntree = presenceDateEntree;
        this.presenceDateSortie = presenceDateSortie;
    }

    public String getNomEtat() {
        return nomEtat;
    }


    public Date getDate_entree() {
        return date_entree;
    }


    public Date getDate_sortie() {
        return date_sortie;
    }

    public boolean getPresenceDateEntree() {
        return presenceDateEntree;
    }

    public boolean getPresenceDateSortie() {
        return this.presenceDateSortie;
    }


    @Override
    public String toString() {
        return "InfosMessageEtat { " +
                "nomEtat='" + nomEtat + '\'' +
                ", date_entree=" + date_entree +
                ", date_sortie=" + date_sortie +
                ",  dansDateEntree =" + presenceDateEntree +
                ", dansDateSortie=" + presenceDateSortie +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InfosMessageEtat that = (InfosMessageEtat) o;
        return presenceDateEntree == that.presenceDateEntree && presenceDateSortie == that.presenceDateSortie && Objects.equals(nomEtat, that.nomEtat) && Objects.equals(date_entree, that.date_entree) && Objects.equals(date_sortie, that.date_sortie);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nomEtat, date_entree, date_sortie, presenceDateEntree, presenceDateSortie);
    }
}
