package de.throsenheim.vvss21.presentation;

import de.throsenheim.vvss21.application.IContactActor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Class for contacting an actor.
 * @version 1.0
 * @author Alexander
 */
@Service
public class ContactActor implements IContactActor {

    private static final Logger LOGGER = LogManager.getLogger(ContactActor.class);
    private final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();


    /**
     * Contacts an Actor to change it's status
     *
     * @param url    URL of the actor
     * @param status New status of the actor
     */
    @Override
    public void contact(String url, String status) {
        String post = url+ "?status="+status;
        HttpRequest request = HttpRequest.newBuilder().
                uri(URI.create(post)).
                POST(HttpRequest.BodyPublishers.noBody()).
                build();
        HttpResponse<String> response = null;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            LOGGER.error(e);
            Thread.currentThread().interrupt();
            return;
        }
        if(response.statusCode() == 200){
            LOGGER.debug("Successfully sent data");
            return;
        }
        LOGGER.error("Something went wrong");
    }
}
