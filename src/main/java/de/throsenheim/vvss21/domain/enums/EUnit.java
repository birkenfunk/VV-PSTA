package de.throsenheim.vvss21.domain.enums;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.throsenheim.vvss21.domain.models.Measurement;

/**
 * Enum with the units used in the {@link Measurement} object
 * @version 1.0.0
 * @author Alexander Asbeck
 */
public enum EUnit {
    @JsonProperty("CELSIUS") CELSIUS,
    @JsonProperty("KELVIN") KELVIN,
    @JsonProperty("PERCENT") PERCENT,
    @JsonProperty("FAHRENHEIT") FAHRENHEIT,
    @JsonProperty("HECTOPASCAL") HECTOPASCAL,
    @JsonProperty("UNITS") UNITS,
    @JsonProperty("CMS2") CMS2,
    @JsonProperty("KWH3") KWH3,
    @JsonProperty("NONE")  NONE
}
