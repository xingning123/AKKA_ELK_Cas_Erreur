package fr.rades.template.infrastructure.recuperateurLogstash.message;

import java.util.Objects;

public final class MessageRecuperateurTronqueur {

    private final String messageAEnvoyer;

    public MessageRecuperateurTronqueur(String messageEnvoye) {
        this.messageAEnvoyer = messageEnvoye;
    }

    public String getMessageEnvoyer() {
        return messageAEnvoyer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageRecuperateurTronqueur that = (MessageRecuperateurTronqueur) o;
        return Objects.equals(messageAEnvoyer, that.messageAEnvoyer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageAEnvoyer);
    }
}
