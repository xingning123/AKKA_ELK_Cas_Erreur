package fr.rades.template.infrastructure.interfaces;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public interface SeparateurStringEnDocuments {

    /**
     * CETTE FONCTION PREND EN ENTREE UNE CHAINE DE CARACTERES
     * ET RETOURNE UNE LISTE DE DOCUMENTS SEPARES SELON UNE EXPRESSION REGULIERE
     * @param texteEntree
     * @return liste de string
     */
     default ArrayList<String> separerDocuments(String texteEntree, String regex) {
        ArrayList<String> documentsSepares = new ArrayList<>();
        int lastIndex = 0;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(texteEntree);

        while (matcher.find()) {
            String documentMatchee = texteEntree.substring(lastIndex, matcher.start() +1);
            documentsSepares.add(documentMatchee);
            lastIndex = matcher.end() - 1;
        }
        if (lastIndex < texteEntree.length() - 1) {
            String dernierDocMatche =texteEntree.substring(lastIndex);
            documentsSepares.add(dernierDocMatche);
        }
        return documentsSepares;
    }

}
