package fr.rades.template.infrastructure.ElasticSearchActorfile;

import fr.rades.template.infrastructure.CreateurLotRequetesfile.MessageDeCreateurLot;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

import java.io.IOException;

import static fr.rades.template.infrastructure.ElasticSearchActorfile.ElasticsearchActor.getRestClient;

public class Elasticsearch {
    private final RestClient client;

    private static final Logger log = LogManager.getLogger(Elasticsearch.class);


    public Elasticsearch(String url,String username,String password) {
        // Constructeur de la classe, il crée un client RestClient basé sur l'URL fournie
        this.client = createRestClient(url,username,password);
    }

    public RestClient createRestClient(String url,String username,String password) {
        // Méthode qui crée et configure le RestClient avec l'URL Elasticsearch fournie
        return getRestClient(url,username,password);
    }


    public Response handleMessage(MessageDeCreateurLot message, RestClient client) {
        // Méthode pour traiter le message reçu
        String requestBody = message.getCorps();

        HttpEntity entity = new NStringEntity(requestBody, ContentType.APPLICATION_JSON);

        Request request = new Request("POST", message.getEndPoint());
        request.setEntity(entity);
        try {
            Response response = client.performRequest(request);
            System.out.println(response);
            return response;
        } catch (IOException e) {
            log.info("Une exception s'est produite lors de la requête Elasticsearch. ", e );
            return null;
        }
    }

    public void postStop() {
        // Méthode appelée lorsque l'objet est arrêté, elle ferme le client RestClient
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
