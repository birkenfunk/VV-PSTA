package de.throsenheim.vvss21.domain.enums;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.throsenheim.vvss21.domain.models.Measurement;

/**
 * Enum with the units used in the {@link Measurement} object
 * @version 1.0.0
 * @author Alexander Asbeck
 */
public enum EUnit {
    @JsonProperty("celsius") CELSIUS,
    @JsonProperty("kelvin") KELVIN,
    @JsonProperty("percent") PERCENT,
    @JsonProperty("fahrenheit") FAHRENHEIT,
    @JsonProperty("hectopascal") HECTOPASCAL,
    @JsonProperty("units") UNITS,
    @JsonProperty("cms2") CMS2,
    @JsonProperty("kwh3") KWH3,
    @JsonProperty("none")  NONE
}
