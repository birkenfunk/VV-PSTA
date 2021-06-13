package de.throsenheim.vvss21.actor.presentation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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

    public void sendData(){
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
            retry();
            return;
        }
        if(response.statusCode() == 201){
            LOGGER.info("SensorData was send successful");
            return;
        }
        if(response.statusCode() == 400){
            LOGGER.info("Sensor wasn't registered will try again in 10s");
            retry();
        }
        LOGGER.info(serverRegistrationURL);
    }

    private void retry(){
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
        sendData();
    }

    private String actorJsonString(){
        try {
            return "{\n" +
                    "    \"aktorId\": "+actorID+",\n" +
                    "    \"aktorName\": \"SchlafzimmerActor\",\n" +
                    "    \"location\": \"Schlafzimmer\",\n" +
                    "    \"serviceUrl\": \"http://"+ InetAddress.getLocalHost().getHostAddress()+":8080/v1/shutter\",\n" +
                    "    \"status\": \"open\"\n" +
                    "}";
        } catch (UnknownHostException e) {
            LOGGER.error(e.getMessage());
            System.exit(-1);
        }
        return null;
    }

}
