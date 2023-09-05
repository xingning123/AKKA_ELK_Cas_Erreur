package fr.rades.template.domain.TraiteurMessagefile;

import java.util.Date;


public class MessageTraiteePourCommande {
    private String idCommande;
    private String nomEtat;
    private Date dateEntree;
    private Date dateSortie;

    public MessageTraiteePourCommande(String idCommande, String nomEtat, Date dateEntree, Date dateSortie) {
        this.idCommande = idCommande;
        this.nomEtat = nomEtat;
        this.dateEntree = dateEntree;
        this.dateSortie = dateSortie;
    }

    public String getIdCommande() {
        return idCommande;
    }

    public void setIdCommande(String idCommande) {
        this.idCommande = idCommande;
    }

    public String getNomEtat() {
        return nomEtat;
    }

    public void setNomEtat(String nomEtat) {
        this.nomEtat = nomEtat;
    }

    public Date getDateEntree() {
        return dateEntree;
    }

    public void setDateEntree(Date dateEntree) {
        this.dateEntree = dateEntree;
    }

    public Date getDateSortie() {
        return dateSortie;
    }

    public void setDateSortie(Date dateSortie) {
        this.dateSortie = dateSortie;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MessageTraiteePourCommande{")
                .append("idCommande='").append(idCommande).append('\'')
                .append(", nomEtat='").append(nomEtat).append('\'');

        if (dateEntree != null) {
            sb.append(", dateEntree=").append(dateEntree);
        }

        if (dateSortie != null) {
            sb.append(", dateSortie=").append(dateSortie);
        }

        sb.append('}');

        return sb.toString();
    }
}

