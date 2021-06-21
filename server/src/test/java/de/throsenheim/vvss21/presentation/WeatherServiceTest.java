package de.throsenheim.vvss21.presentation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WeatherServiceTest {

    private WeatherService weatherService;

    @BeforeEach
    void setUp() {
        weatherService = new WeatherService();
    }

    @Test
    void testGetJWTToken() {
        assertFalse(weatherService.getJWTToken().isEmpty());
    }

    @Test
    void TestContactWeatherService() {
        assertFalse(weatherService.contactWeatherService(weatherService.getJWTToken()).isEmpty());
    }
}