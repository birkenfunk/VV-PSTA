package de.throsenheim.vvss21.presentation;

import de.throsenheim.vvss21.application.RuleEngine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class ContactActor {

    private static final Logger LOGGER = LogManager.getLogger(ContactActor.class);
    private final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();

    public void sendData(String publishURL, String status){
        String post = publishURL+ "?status="+status;
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
        LOGGER.error("Something went wrong");
    }

}
