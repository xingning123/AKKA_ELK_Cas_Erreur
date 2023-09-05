package fr.rades.template.domain.TraiteurMessagefile;

import fr.rades.template.infrastructure.interfaces.Etat;

import java.util.ArrayList;

public class ResponseInformationParent {
    private final ArrayList<Etat> etatList;

    public ArrayList<Etat> getEtatList() {
        return etatList;
    }

    public ResponseInformationParent(ArrayList<Etat> etatList) {
        this.etatList = etatList;
    }
}
