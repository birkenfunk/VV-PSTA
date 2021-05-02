package de.throsenheim.vvss21.domain.enums;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.throsenheim.vvss21.domain.models.Measurement;

/**
 * Enum with the types to used in the {@link Measurement} object
 * @version 1.0.0
 * @author Alexander Asbeck
 */
public enum EType {
    @JsonProperty("TEMPERATURE") TEMPERATURE,
    @JsonProperty("HUMIDITY") HUMIDITY,
    @JsonProperty("PRESSURE") PRESSURE,
    @JsonProperty("COUNT") COUNT,
    @JsonProperty("FLOW_RATE") FLOW_RATE,
    @JsonProperty("ENERGY") ENERGY,
    @JsonProperty("NONE") NONE
}
