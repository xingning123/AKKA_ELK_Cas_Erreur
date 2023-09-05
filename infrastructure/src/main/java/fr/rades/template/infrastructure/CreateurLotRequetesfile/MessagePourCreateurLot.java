package fr.rades.template.infrastructure.CreateurLotRequetesfile;
import java.util.ArrayList;
import fr.rades.template.infrastructure.interfaces.Etat;

public class MessagePourCreateurLot {

    private final String idCmd;
    private final  ArrayList<Etat>  etats;
    public MessagePourCreateurLot(String idCmd, ArrayList<Etat> etats) {
        this.idCmd = idCmd;
        this.etats = etats;
    }
    public String getIdCmd() {
        return idCmd;
    }
    public ArrayList<Etat> getEtats() {
        return etats;
    }

    @Override
    public String toString() {
        return "MessagePourCreateurLot{" +
                "idCmd='" + idCmd + '\'' +
                ", etats=" + etats +
                '}';
    }
}
