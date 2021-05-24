package de.throsenheim.vvss21.domain;

import java.sql.Timestamp;

public class SensorDataDto {
    private TemperaturUnit temperaturUnit;
    private Timestamp timestamp;
    private byte currentValue;
    private int sensorId;

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public byte getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(byte currentValue) {
        this.currentValue = currentValue;
    }

    public int getSensorId() {
        return sensorId;
    }

    public void setSensorId(int sensorId) {
        this.sensorId = sensorId;
    }

    public TemperaturUnit getTemperaturUnit() {
        return temperaturUnit;
    }

    public void setTemperaturUnit(TemperaturUnit temperaturUnit) {
        this.temperaturUnit = temperaturUnit;
    }
}
