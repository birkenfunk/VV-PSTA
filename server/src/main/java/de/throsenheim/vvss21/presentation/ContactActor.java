package de.throsenheim.vvss21.presentation;

import de.throsenheim.vvss21.application.IContactActor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.net.http.HttpClient;

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
        LOGGER.info("Posting Status {} to {}",status, url);
        /*String post = publishURL+ "?status="+status;
        HttpRequest request = HttpRequest.newBuilder().
                uri(URI.create(post)).
                GET().
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
        LOGGER.error("Something went wrong");*/
    }
}
