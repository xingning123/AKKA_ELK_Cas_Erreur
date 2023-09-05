package fr.rades.template.infrastructure.ElasticSearchActorfile;

import akka.actor.AbstractActor;
import akka.actor.Props;
import fr.rades.template.infrastructure.CreateurLotRequetesfile.MessageDeCreateurLot;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class ElasticsearchActor extends AbstractActor {
    private final RestClient client;
    private final Elasticsearch elasticsearch;
    private final String username;
    private final String password;

    public ElasticsearchActor(String url, String username, String password) {
        // Constructeur de la classe, il crée un client RestClient basé sur l'URL fournie
        this.username = username;
        this.password = password;
        this.client = createRestClient(url, username, password);
        this.elasticsearch = new Elasticsearch(url, username, password);
    }

    private RestClient createRestClient(String url, String username, String password) {
        // Méthode privée qui crée et configure le RestClient avec l'URL Elasticsearch fournie
        return getRestClient(url, username, password);
    }

    static RestClient getRestClient(String url, String username, String password) {

        String[] hostAndPort = url.split(":");
        String hostname = hostAndPort[0];
        int port = Integer.parseInt(hostAndPort[1]);
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));

        SSLContext sslContext = null;
        try {
            sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                    return true;
                }
            }).build();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (KeyManagementException e) {
            throw new RuntimeException(e);
        } catch (KeyStoreException e) {
            throw new RuntimeException(e);
        }

        SSLContext finalSslContext = sslContext;
        RestClientBuilder builder = RestClient.builder(new HttpHost(hostname, port, "https"))
                .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder.setSSLContext(finalSslContext).setDefaultCredentialsProvider(credentialsProvider));

        return builder.build();
    }

    //    public static Props props(String url,String username,String password, RestClient restClient) {
    public static Props props(String url, String username, String password) {
        return Props.create(ElasticsearchActor.class, url, username, password);
    }

    public Response handleMessage(MessageDeCreateurLot message, RestClient client) {
        // Méthode pour traiter le message reçu par l'acteur
        return elasticsearch.handleMessage(message, client);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(MessageDeCreateurLot.class, message -> {
                    Response response = handleMessage(message, client);
                })
                .build();
    }

    @Override
    public void postStop() {
        // Méthode appelée lorsque l'acteur est arrêté, elle ferme le client RestClient
        elasticsearch.postStop();
    }
}



