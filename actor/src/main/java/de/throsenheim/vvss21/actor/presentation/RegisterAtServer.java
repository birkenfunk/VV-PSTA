package de.throsenheim.vvss21.actor.presentation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class RegisterAtServer {

    private static final Logger LOGGER = LogManager.getLogger(RegisterAtServer.class);
    private final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();
    private final String serverRegistrationURL;
    private final int actorID;

    public RegisterAtServer() {
        Map<String, String> env = System.getenv();
        this.serverRegistrationURL = env.getOrDefault("ServerRegistrationURL", "http://localhost:9000/v1/actors");
        this.actorID = Integer.parseInt(env.getOrDefault("ActorID", "2"));
    }

    private void sendData(){
        HttpRequest request = HttpRequest.newBuilder().
                uri(URI.create(serverRegistrationURL)).
                header("Content-Type", "application/json").
                POST(HttpRequest.BodyPublishers.ofString(actorJsonString())).
                build();
        HttpResponse<String> response = null;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            LOGGER.error(e);
            Thread.currentThread().interrupt();
            System.exit(-1);
        }
        if(response.statusCode() == 201){
            LOGGER.info("SensorData was send successful");
            return;
        }
        if(response.statusCode() == 400){
            LOGGER.info("Sensor wasn't registered");
            return;
        }
        LOGGER.info(serverRegistrationURL);
    }

    private String actorJsonString(){
        return null;
    }

}
