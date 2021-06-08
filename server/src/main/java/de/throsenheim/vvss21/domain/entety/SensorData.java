package de.throsenheim.vvss21.domain.entety;

import de.throsenheim.vvss21.domain.TemperatureUnit;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * DB entity for the sensorData
 */
@Entity
public class SensorData {
    private TemperatureUnit temperatureUnit;
    private Timestamp timestamp;
    private byte currentValue;
    private int sensorDataId;
    private Sensor sensorBySensorId;

    public SensorData(TemperatureUnit temperatureUnit, Timestamp timestamp, byte currentValue, Sensor sensorBySensorId) {
        this.temperatureUnit = temperatureUnit;
        this.timestamp = timestamp;
        this.currentValue = currentValue;
        this.sensorBySensorId = sensorBySensorId;
    }

    public SensorData() {
    }

    @Basic
    @Column(name = "TemperatureUnit", nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    public TemperatureUnit getTemperatureUnit() {
        return temperatureUnit;
    }

    public void setTemperatureUnit(TemperatureUnit temperatureUnit) {
        this.temperatureUnit = temperatureUnit;
    }

    @Basic
    @Column(name = "Timestamp", nullable = false, updatable = false)
    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Basic
    @Range(min = 0, max = 30)
    @Column(name = "CurrentValue", nullable = false, updatable = false)
    public byte getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(byte currentValue) {
        this.currentValue = currentValue;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "SensorDataID", nullable = false, updatable = false)
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

    @Transient
    public int getSensorID(){
        if(sensorBySensorId!=null) {
            return sensorBySensorId.getSensorId();
        }
        return Integer.MIN_VALUE;
    }

    public void setSensorBySensorId(Sensor sensorBySensorId) {
        this.sensorBySensorId = sensorBySensorId;
    }
}
