package de.throsenheim.vvss21.domain;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
public class SensorData {
    private Timestamp timestamp;
    private byte currentValue;
    private int sensorDataId;

    @Basic
    @Column(name = "Timestamp")
    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Basic
    @Column(name = "CurrentValue")
    public byte getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(byte currentValue) {
        this.currentValue = currentValue;
    }

    @Id
    @Column(name = "SensorDataID")
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
        return currentValue == that.currentValue && sensorDataId == that.sensorDataId && Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, currentValue, sensorDataId);
    }
}
