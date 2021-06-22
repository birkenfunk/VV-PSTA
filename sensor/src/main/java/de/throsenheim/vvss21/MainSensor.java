package de.throsenheim.vvss21;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class MainSensor {

    private static final Logger LOGGER = LogManager.getLogger(MainSensor.class);
    private final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();
    private final String serverRegistrationURL;
    private String serverPublishURL;
    private final int sensorID;

    public static void main(String[] args) {
        MainSensor sensor = new MainSensor();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                sensor.sendDeleteRequest();
            } catch (IOException | InterruptedException e) {
                LOGGER.error(e);
                Thread.currentThread().interrupt();
            }
        }));
    }

    public MainSensor() {
        Map<String, String> env = System.getenv();
        this.serverRegistrationURL = env.getOrDefault("ServerRegistrationURL", "http://localhost:9000/v1/sensors");
        this.sensorID = Integer.parseInt(env.getOrDefault("SensorId", "2"));
        this.serverPublishURL = env.getOrDefault("ServerPublishURL", "http://localhost:9000/v1/sensors/")+ "/" + sensorID;
        registerSensor();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                sendData(generateSenorData());
            }
        }, 0,60000);
    }

    private void registerSensor(){
        String sensor = "{\"sensorId\":"+sensorID+",\"sensorName\":\"SensorSchlafzimmer\",\"location\":\"Schlafzimmer\"}";
        HttpRequest request = HttpRequest.newBuilder().
                uri(URI.create(serverRegistrationURL)).
                header("Content-Type", "application/json").
                POST(HttpRequest.BodyPublishers.ofString(sensor)).
                build();
        HttpResponse<String> response = null;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            LOGGER.error(e);
            retry();
            return;
        }catch (InterruptedException e){
            LOGGER.error(e);
            retry();
            Thread.currentThread().interrupt();
            return;
        }
        if(response.statusCode() == 201)
            LOGGER.info("Sensor registered");
        if(response.statusCode() == 400) {
            LOGGER.info("Sensor already exists please use other ID\nWill retry in 10s");
            LOGGER.debug(serverPublishURL);
            retry();
        }
    }

    private void retry(){
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
        registerSensor();
    }

    private void sendData(String sensorData){
        HttpRequest request = HttpRequest.newBuilder().
                uri(URI.create(serverPublishURL)).
                header("Content-Type", "application/json").
                POST(HttpRequest.BodyPublishers.ofString(sensorData)).
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
        LOGGER.info(serverPublishURL);
    }

    private String generateSenorData(){
        return "{\n" +
                "    \"temperatureUnit\":\"celsius\",\n" +
                "    \"timestamp\":\""+ LocalDateTime.now() +"\",\n" +
                "    \"currentValue\":"+ new Random().nextInt(30) + "\n" +
                "}";
    }

    public void sendDeleteRequest() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().
                uri(URI.create(serverPublishURL)).
                DELETE().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if(response.statusCode() == 204)
            LOGGER.info("Successfully deleted Sensor");
        if(response.statusCode() == 404)
            LOGGER.info("Sensor was already deleted");
    }
}
