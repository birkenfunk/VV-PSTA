package de.throsenheim.vvss21.domain.dtoentety;

import de.throsenheim.vvss21.domain.TemperaturUnit;

import java.sql.Timestamp;

public class SensorDataDto {
    private TemperaturUnit temperaturUnit;
    private Timestamp timestamp;
    private byte currentValue;
    private SensorDto sensorBySensorID;

    public SensorDataDto() {
    }

    public SensorDataDto(TemperaturUnit temperaturUnit, Timestamp timestamp, byte currentValue, SensorDto sensorDto) {
        this.temperaturUnit = temperaturUnit;
        this.timestamp = timestamp;
        this.currentValue = currentValue;
        this.sensorBySensorID = sensorDto;
    }

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

    public SensorDto getSensorBySensorID() {
        return sensorBySensorID;
    }

    public void setSensorBySensorID(SensorDto sensorBySensorID) {
        this.sensorBySensorID = sensorBySensorID;
    }

    public TemperaturUnit getTemperaturUnit() {
        return temperaturUnit;
    }

    public void setTemperaturUnit(TemperaturUnit temperaturUnit) {
        this.temperaturUnit = temperaturUnit;
    }
}
