package de.throsenheim.vvss21.domain;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
public class SensorData {
    private TemperaturUnit temperatureUnit;
    private Timestamp timestamp;
    private byte currentValue;
    private int sensorDataId;
    private Sensor sensorBySensorId;

    public SensorData(TemperaturUnit temperatureUnit, Timestamp timestamp, byte currentValue, Sensor sensorBySensorId) {
        this.temperatureUnit = temperatureUnit;
        this.timestamp = timestamp;
        this.currentValue = currentValue;
        this.sensorBySensorId = sensorBySensorId;
    }

    public SensorData() {
    }

    @Basic
    @Column(name = "TemperatureUnit", nullable = false)
    @Enumerated(EnumType.STRING)
    public TemperaturUnit getTemperatureUnit() {
        return temperatureUnit;
    }

    public void setTemperatureUnit(TemperaturUnit temperatureUnit) {
        this.temperatureUnit = temperatureUnit;
    }

    @Basic
    @Column(name = "Timestamp", nullable = false)
    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Basic
    @Column(name = "CurrentValue", nullable = false)
    public byte getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(byte currentValue) {
        this.currentValue = currentValue;
    }

    @Id
    @GeneratedValue
    @Column(name = "SensorDataID", nullable = false)
    public int getSensorDataId() {
        return sensorDataId;
    }

    public void setSensorDataId(int sensorDataId) {
        this.sensorDataId = sensorDataId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SensorData that = (SensorData) o;
        return currentValue == that.currentValue && sensorDataId == that.sensorDataId && Objects.equals(temperatureUnit, that.temperatureUnit) && Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(temperatureUnit, timestamp, currentValue, sensorDataId);
    }

    @ManyToOne
    @JoinColumn(name = "SensorID", referencedColumnName = "SensorId")
    public Sensor getSensorBySensorId() {
        return sensorBySensorId;
    }

    public void setSensorBySensorId(Sensor sensorBySensorId) {
        this.sensorBySensorId = sensorBySensorId;
    }
}
