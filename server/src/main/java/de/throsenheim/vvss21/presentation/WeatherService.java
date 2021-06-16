package de.throsenheim.vvss21.presentation;

import de.throsenheim.vvss21.application.IWeatherService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class WeatherService implements IWeatherService {

    private static final Logger LOGGER = LogManager.getLogger(WeatherService.class);
    private final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();

    @Override
    public String getJWTToken() {
        String postURL = "https://ss21vv-externalweatherservice.azurewebsites.net/api/v1/authenticate";
        String body = "{\"username\":\"asbeckalexander\",\"password\":\"vvSS21\"}";
        HttpRequest request = HttpRequest.newBuilder().
                uri(URI.create(postURL)).
                header("Content-Type", "application/json").
                POST(HttpRequest.BodyPublishers.ofString(body)).
                build();
        return getResponse(request);
    }

    @Override
    public String contactWeatherService(String jWTToken) {
        String postURL = "https://ss21vv-externalweatherservice.azurewebsites.net/api/WeatherForecast";
        HttpRequest request = HttpRequest.newBuilder().
                uri(URI.create(postURL)).
                GET().
                header("Authorization","Bearer "+ jWTToken).
                build();
        return getResponse(request);
    }

    private String getResponse(HttpRequest request){
        HttpResponse<String> response = null;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            LOGGER.error(e);
            Thread.currentThread().interrupt();
            return null;
        }
        if(response.statusCode() == 200){
            LOGGER.debug("Successfully sent data");
            return response.body();
        }
        LOGGER.error("Something went wrong");
        return null;
    }
}
