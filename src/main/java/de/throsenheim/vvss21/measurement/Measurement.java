package de.throsenheim.vvss21.measurement;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Timestamp;

public class Measurement{


    private static final Logger LOGGER = LogManager.getLogger(Measurement.class);
    private int value;
    private Unit unit;
    private Type type;
    private Timestamp timestamp;

    public Measurement(@JsonProperty("value") int value, @JsonProperty("unit") String unit, @JsonProperty("type") String type, @JsonProperty("timestamp") String timestamp) {
        this.value = value;
        try {
            this.unit = Unit.valueOf(unit);
        }catch (IllegalArgumentException e){
            this.unit = null;
        }
        try {
            this.type = Type.valueOf(type);
        }catch (IllegalArgumentException e){
            this.type = null;
        }
        this.timestamp = Timestamp.valueOf(timestamp);
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
        return timestamp.toString();
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
}
