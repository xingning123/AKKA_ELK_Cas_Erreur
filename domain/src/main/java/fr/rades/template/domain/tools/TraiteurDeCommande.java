package fr.rades.template.domain.tools;

import fr.rades.template.domain.CommandeActorfile.Commande;
import fr.rades.template.infrastructure.interfaces.Etat;
import fr.rades.template.domain.TraiteurMessagefile.MessageTraiteePourCommande;

import java.util.*;

public class TraiteurDeCommande {

    public static void handleMessage(MessageTraiteePourCommande updatedMessage, Commande commande) {

        ArrayList<Etat> listeEtat = commande.getEtatList();

        boolean notExist = true;

        for (int i = 0; i < listeEtat.size(); i++) {
            Etat etatExistant = listeEtat.get(i);

            if (etatExistant.getNomEtat().equals(updatedMessage.getNomEtat())) {
                notExist = false; // etat existe
                if (updatedMessage.getDateEntree() != null) {
                    etatExistant.setDateDebut(updatedMessage.getDateEntree());
                }
                if (updatedMessage.getDateSortie() != null) {
                    etatExistant.setDateFin(updatedMessage.getDateSortie());
                }
                break;
            }
        }

        if (notExist) { // etat n'existe pas
            Etat newEtat = new Etat(updatedMessage.getNomEtat(), updatedMessage.getDateEntree(), updatedMessage.getDateSortie());
            listeEtat.add(newEtat);
            commande.setId_cmd(updatedMessage.getIdCommande());
        }
    }
}
