package de.throsenheim.vvss21.measurement;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.throsenheim.vvss21.application.enums.EType;
import de.throsenheim.vvss21.application.enums.EUnit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

/**
 * Object for storing a measurement data
 * @version 1.0.0
 * @author Alexander Asbeck
 */
public class Measurement{

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
    public Measurement(@JsonProperty("value") int value, @JsonProperty("unit") String unit, @JsonProperty("type") String type, @JsonProperty("timestamp") String timestamp) {
        unit = unit.toUpperCase();
        type = type.toUpperCase();
        this.value = value;
        try {
            this.unit = EUnit.valueOf(unit);
        }catch (IllegalArgumentException e){
            this.unit = EUnit.NONE;
        }
        try {
            this.type = EType.valueOf(type);
        }catch (IllegalArgumentException e){
            this.type = EType.NONE;
        }
        try {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SS");
            this.timestamp = LocalDateTime.parse(timestamp, dateTimeFormatter);
        }catch (DateTimeParseException e){
            try {
                this.timestamp = LocalDateTime.parse(timestamp);
            }catch (DateTimeParseException dateTimeParseException){
                this.timestamp = LocalDateTime.now();
            }

        }

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
    public String getTimestamp() {
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SS");
        return timestamp.format(myFormatObj);
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
