package de.throsenheim.vvss21.domain.entety;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Date;
import java.util.Objects;

/**
 * DB entity for the sensor
 */
@Entity
public class Sensor {
    private int sensorId;
    private String sensorName;
    private Date registerDate;
    private String location;
    private boolean deleted;

    public Sensor(int sensorId, String sensorName, String location) {
        this.sensorId = sensorId;
        this.sensorName = sensorName;
        this.location = location;
    }

    public Sensor() {
    }

    @Id
    @Column(name = "SensorId", nullable = false, updatable = false)
    public int getSensorId() {
        return sensorId;
    }

    public void setSensorId(int sensorId) {
        this.sensorId = sensorId;
    }

    @Basic
    @Column(name = "SensorName", nullable = false, length = 100)
    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    @Basic
    @CreationTimestamp
    @Column(name = "RegisterDate")
    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    @Basic
    @Column(name = "Location", nullable = false, length = 100)
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Basic
    @Column(name = "Deleted", columnDefinition = "boolean default false")
    public boolean isDeleted(){
        return deleted;
    }

    public void setDeleted(boolean deleted){
        this.deleted=deleted;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sensor sensor = (Sensor) o;
        return sensorId == sensor.sensorId && Objects.equals(sensorName, sensor.sensorName) && Objects.equals(registerDate, sensor.registerDate) && Objects.equals(location, sensor.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sensorId, sensorName, registerDate, location);
    }
}
