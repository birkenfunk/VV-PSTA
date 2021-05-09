package de.throsenheim.vvss21.domain;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Date;
import java.util.Objects;

@Entity
public class Sensor {
    private int sensorId;
    private String sensorName;
    private Date registerDate;
    private String location;

    @Id
    @Column(name = "SensorId")
    public int getSensorId() {
        return sensorId;
    }

    public void setSensorId(int sensorId) {
        this.sensorId = sensorId;
    }

    @Basic
    @Column(name = "SensorName")
    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    @Basic
    @Column(name = "RegisterDate")
    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    @Basic
    @Column(name = "Location")
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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
