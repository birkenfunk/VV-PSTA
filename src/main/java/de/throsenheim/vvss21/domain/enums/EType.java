package de.throsenheim.vvss21.domain.enums;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.throsenheim.vvss21.domain.models.Measurement;

/**
 * Enum with the types to used in the {@link Measurement} object
 * @version 1.0.0
 * @author Alexander Asbeck
 */
public enum EType {
    @JsonProperty("temperature") TEMPERATURE,
    @JsonProperty("humidity") HUMIDITY,
    @JsonProperty("pressure") PRESSURE,
    @JsonProperty("count") COUNT,
    @JsonProperty("flow_rate") FLOW_RATE,
    @JsonProperty("energy") ENERGY,
    @JsonProperty("none") NONE
}
