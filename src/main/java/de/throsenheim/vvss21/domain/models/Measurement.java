package de.throsenheim.vvss21.domain.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import de.throsenheim.vvss21.domain.enums.EType;
import de.throsenheim.vvss21.domain.enums.EUnit;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Object for storing a measurement data
 * @version 1.0.0
 * @author Alexander Asbeck
 */
@JsonPropertyOrder(value = {"value", "unit", "type", "timestamp"})

public class Measurement {

    private int value;//Value of the measurement
    private EUnit unit;//Unit of the measurement eg. CELSIUS
    private EType type;//Type of the measurement eg. TEMPERATURE
    private LocalDateTime timestamp;//Time when the measurement happened

    /**
     * Constructor for {@link Measurement}
     * @param value Value of the measurement
     * @param unit Unit of the measurement from {@link EUnit}
     * @param type Type of the measurement from {@link EType}
     * @param timestamp Time when the measurement happened in the format 'yyyy-MM-dd HH:mm:ss.SS'
     */
    public Measurement(@JsonProperty("value") int value, @JsonProperty("unit") EUnit unit, @JsonProperty("type") EType type, @JsonProperty("timestamp") LocalDateTime timestamp) {
        this.unit = unit;
        this.type = type;
        this.value = value;

        this.timestamp = timestamp;

    }

    /**
     * Getter of value
     * @return Integer with the value of the measurement
     */
    public int getValue() {
        return value;
    }

    /**
     * Getter of Unit
     * @return String with the Unit of the measurement
     */
    public EUnit getUnit() {
        return unit;
    }

    /**
     * Getter of Type
     * @return String with the Type of the measurement
     */
    public EType getType() {
        return type;
    }

    /**
     * Getter of Timestamp
     * @return String with the Timestamp of the measurement with the pattern: 'yyyy-MM-dd HH:mm:ss.SS'
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Measurement{" +
                "value=" + value +
                ", unit=" + unit +
                ", type=" + type +
                ", timestamp=" + timestamp +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Measurement that = (Measurement) o;
        return value == that.value && unit == that.unit && type == that.type && timestamp.equals(that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, unit, type, timestamp);
    }
}
