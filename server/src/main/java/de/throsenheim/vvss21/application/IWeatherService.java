package de.throsenheim.vvss21.application;

/**
 * Interface for contacting a Weather Service
 * @version 1.0
 * @author Alexander
 */
public interface IWeatherService {

    /**
     * For getting a JWTToken for contacting the weather service api
     * @return JWTToken
     */
    String getJWTToken();

    /**
     * Contacts the weather service
     * @param jWTToken JWTToken form {@link IWeatherService#getJWTToken()}
     * @return A json String with the weather data
     */
    String contactWeatherService(String jWTToken);

}
