package de.throsenheim.vvss21.domain.enums;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Enum for handling an connection automat
 * @version 1.0.0
 * @author Alexander Asbeck
 */
public enum ESymbol {
    @JsonProperty("sensor_hello") SENSOR_HELLO,
    @JsonProperty("station_hello") STATION_HELLO,
    @JsonProperty("acknowledge") ACKNOWLEDGE,
    @JsonProperty("station_ready") STATION_READY,
    @JsonProperty("measurement") MEASUREMENT,
    @JsonProperty("terminate") TERMINATE,
    @JsonProperty("terminate_station") TERMINATE_STATION,
    @JsonProperty("error") ERROR,
    @JsonEnumDefaultValue @JsonProperty("unknown") UNKNOWN
}
