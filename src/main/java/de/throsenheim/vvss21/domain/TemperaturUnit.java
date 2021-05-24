package de.throsenheim.vvss21.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum TemperaturUnit {
    @JsonProperty("kelvin") KELVIN,
    @JsonProperty("celsius") CELSIUS,
    @JsonProperty("fahrenheit") FAHRENHEIT
}
