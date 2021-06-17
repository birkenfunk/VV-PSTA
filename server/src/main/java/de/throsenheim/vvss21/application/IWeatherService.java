package de.throsenheim.vvss21.application;

public interface IWeatherService {

    String getJWTToken();

    String contactWeatherService(String jWTToken);

}
