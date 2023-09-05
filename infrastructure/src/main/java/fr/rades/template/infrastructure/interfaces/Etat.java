package fr.rades.template.infrastructure.interfaces;

import java.util.Date;

public class Etat {
    private final String nomEtat;
    private Date dateDebut ;
    private Date dateFin;

    public Etat(String nomEtat) {
        this.nomEtat = nomEtat;
    }

    public Etat(String nomEtat, Date dateDebut, Date dateFin) {

        this.nomEtat = nomEtat;
        this.dateDebut = dateDebut;
        this.dateFin =dateFin;

    }

    public String getNomEtat() {
        return nomEtat;
    }
    public Date getDateDebut() {
        return dateDebut;
    }
    public void setDateDebut(Date date_entree) {
        this.dateDebut = date_entree;
    }
    public Date getDateFin() {
        return dateFin; }
        
    public void setDateFin(Date date_sortie) {
        this.dateFin = date_sortie;
    }


    public Etat copieCommande() {
        return new Etat(this.nomEtat, this.dateDebut, this.dateFin);
    }

    @Override
    public String toString() {
        return
                "nomEtat : " + nomEtat +
                "dateDebut : " +dateDebut+
                "dateFin : " +dateFin;
    }

}
