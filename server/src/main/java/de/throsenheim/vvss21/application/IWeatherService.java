package de.throsenheim.vvss21.application;

/**
 * Interface for contacting a Weather Service
 * @version 1.0
 * @author Alexander
 */
public interface IWeatherService {

    /**
     * Contacts the weather service
     * @return A json String with the weather data
     */
    String contactWeatherService();

}
