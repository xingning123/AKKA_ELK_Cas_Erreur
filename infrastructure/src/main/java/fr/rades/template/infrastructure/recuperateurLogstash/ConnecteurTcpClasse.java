package fr.rades.template.infrastructure.recuperateurLogstash;

import java.net.InetSocketAddress;

public class ConnecteurTcpClasse {
    private InetSocketAddress adresse_tcp = new InetSocketAddress("127.0.0.1",5000);
    private int nbreMessagesMaxEnQueue = 10000;

    public ConnecteurTcpClasse() {

    }

    public InetSocketAddress getAdresse_tcp() {
        return adresse_tcp;
    }

    public void setAdresse_tcp(InetSocketAddress adresse_tcp) {
        this.adresse_tcp = adresse_tcp;
    }

    public int getNbreMessagesMaxEnQueue() {
        return nbreMessagesMaxEnQueue;
    }

    public void setNbreMessagesMaxEnQueue(int nbreMessagesMaxEnQueue) {
        this.nbreMessagesMaxEnQueue = nbreMessagesMaxEnQueue;
    }

    @Override
    public String toString() {
        return "Adresse de connexion TCP {" +
                adresse_tcp.getAddress() +
                ", Port =" + adresse_tcp.getPort() +
                "}\n";
    }
}
