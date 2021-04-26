package de.throsenheim.vvss21.measurement;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

public class Measurement{

    private int value;
    private Unit unit;
    private Type type;
    private LocalDateTime timestamp;

    public Measurement(@JsonProperty("value") int value, @JsonProperty("unit") String unit, @JsonProperty("type") String type, @JsonProperty("timestamp") String timestamp) {
        unit = unit.toUpperCase();
        type = type.toUpperCase();
        this.value = value;
        try {
            this.unit = Unit.valueOf(unit);
        }catch (IllegalArgumentException e){
            this.unit = Unit.NONE;
        }
        try {
            this.type = Type.valueOf(type);
        }catch (IllegalArgumentException e){
            this.type = Type.NONE;
        }
        try {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SS");
            this.timestamp = LocalDateTime.parse(timestamp, dateTimeFormatter);
        }catch (DateTimeParseException e){
            this.timestamp = LocalDateTime.parse(timestamp);
        }

    }

    public int getValue() {
        return value;
    }

    public Unit getUnit() {
        return unit;
    }

    public Type getType() {
        return type;
    }

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
