package de.throsenheim.vvss21.measurement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Timestamp;

public class Measurement{


    private static final Logger LOGGER = LogManager.getLogger(Measurement.class);
    private final int value;
    private final Unit unit;
    private final Type type;
    private final Timestamp timestamp;


    public Measurement(int value, Unit unit, Type type, Timestamp timestamp) {
        this.value = value;
        this.unit = unit;
        this.type = type;
        this.timestamp = timestamp;
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
