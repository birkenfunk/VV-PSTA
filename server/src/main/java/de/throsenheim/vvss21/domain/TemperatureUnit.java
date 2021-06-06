package de.throsenheim.vvss21.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum TemperatureUnit {
    @JsonProperty("kelvin") KELVIN,
    @JsonProperty("celsius") CELSIUS,
    @JsonProperty("fahrenheit") FAHRENHEIT
}
