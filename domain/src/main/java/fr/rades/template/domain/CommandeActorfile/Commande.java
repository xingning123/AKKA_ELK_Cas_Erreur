package fr.rades.template.domain.CommandeActorfile;

import fr.rades.template.infrastructure.interfaces.Etat;

import java.util.ArrayList;
import java.util.List;

public class Commande {
    private String id_cmd;
    private ArrayList<Etat> etatList;

    public void setId_cmd(String id_cmd) {
        this.id_cmd = id_cmd;
    }

    public void setEtatList(ArrayList<Etat> etatList) {
        this.etatList = etatList;
    }

    public String getId_cmd() {
        return id_cmd;
    }

    public ArrayList<Etat> getEtatList() {
        return etatList;
    }

    public Commande(String id_cmd, ArrayList<Etat> etatList) {
        this.id_cmd = id_cmd;
        this.etatList = etatList;
        if (this.etatList == null) {
            this.etatList = new ArrayList<>();
        }
    }


    @Override
    public String toString() {
        return "id " + id_cmd + "\n" + "Etat List: " + etatList;
    }

    public void addEtat(Etat etat) {  // 为了防止duplicate问题
        if (etat != null && !etat.getNomEtat().isEmpty()) {
            // Check if the etat with the same name already exists in the list
            boolean exists = false;
            for (Etat existingEtat : etatList) {
                if (existingEtat.getNomEtat().equals(etat.getNomEtat())) {
                    exists = true;
                    break;
                }
            }

            if (!exists) {
                etatList.add(etat);
            }
        }
    }
}


