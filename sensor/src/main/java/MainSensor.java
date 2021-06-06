import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class MainSensor {

    private static final Logger LOGGER = LogManager.getLogger(MainSensor.class);
    private final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();

    public static void main(String[] args) throws IOException, InterruptedException {
        MainSensor mainSensor = new MainSensor();
        mainSensor.sendGetRequest();
        mainSensor.sendPostRequest();
    }

    public void sendGetRequest() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().
                GET().
                uri(URI.create("http://localhost:9000/v1/sensors")).build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        LOGGER.info(response.body());
    }

    public void sendPostRequest() throws IOException, InterruptedException {
        SensorDto sensorDto = new SensorDto(2,"Test2","Kueche");
        String s = Json.stringify(Json.toJson(sensorDto));
        HttpRequest request = HttpRequest.newBuilder().
                uri(URI.create("http://localhost:9000/v1/sensors")).
                header("Content-Type", "application/json").
                POST(HttpRequest.BodyPublishers.ofString(s)).
                build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        LOGGER.info(response.headers().map().toString());
        LOGGER.info(response.statusCode());
    }
}
