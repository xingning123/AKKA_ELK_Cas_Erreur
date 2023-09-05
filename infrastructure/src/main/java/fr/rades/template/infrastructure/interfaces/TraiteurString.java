package fr.rades.template.infrastructure.interfaces;

public interface TraiteurString {


    /**
     * CETTE FONCTION PREND UN STRING EN ENTREE
     * ET RETIRE S'IL EXISTE LES DEUX MOTS EN BORD
     * @param mot
     * @return mot sans guillemets à la fin
     * Exemple :  param  "koko"   or later 'koko'   ---> return =  koko
     */
    public default String supprimerBordureMots(String mot , char bordure) {
        int debut = 0;
        if(mot.charAt(debut)== bordure && mot.charAt(mot.length() -1) == bordure ) {
            return mot.substring(debut,mot.length());
        }
        else
            return mot;
    }
}
