package de.throsenheim.vvss21.domain;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Date;
import java.util.Objects;

@Entity
public class Actor {
    private int aktorId;
    private String aktorName;
    private Date registerDate;
    private String location;
    private String serviceUrl;
    private String status;

    public Actor(int aktorId, String aktorName, Date registerDate, String location, String serviceUrl, String status) {
        this.aktorId = aktorId;
        this.aktorName = aktorName;
        this.registerDate = registerDate;
        this.location = location;
        this.serviceUrl = serviceUrl;
        this.status = status;
    }

    public Actor() {
    }

    @Id
    @Column(name = "AktorId", nullable = false)
    public int getAktorId() {
        return aktorId;
    }

    public void setAktorId(int aktorId) {
        this.aktorId = aktorId;
    }

    @Basic
    @Column(name = "AktorName", nullable = false, length = 100)
    public String getAktorName() {
        return aktorName;
    }

    public void setAktorName(String aktorName) {
        this.aktorName = aktorName;
    }

    @Basic
    @Column(name = "RegisterDate", nullable = false)
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
    @Column(name = "ServiceURL", nullable = false, length = 100)
    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    @Basic
    @Column(name = "Status", nullable = false, length = 100)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Actor actor = (Actor) o;
        return aktorId == actor.aktorId && Objects.equals(aktorName, actor.aktorName) && Objects.equals(registerDate, actor.registerDate) && Objects.equals(location, actor.location) && Objects.equals(serviceUrl, actor.serviceUrl) && Objects.equals(status, actor.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(aktorId, aktorName, registerDate, location, serviceUrl, status);
    }
}
