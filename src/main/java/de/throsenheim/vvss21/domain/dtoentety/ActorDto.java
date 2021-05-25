package de.throsenheim.vvss21.domain.dtoentety;

import java.sql.Date;

public class ActorDto {
    private int aktorId;
    private String aktorName;
    private Date registerDate;
    private String location;
    private String serviceUrl;
    private String status;

    public ActorDto() {
    }

    public ActorDto(int aktorId, String aktorName, Date registerDate, String location, String serviceUrl, String status) {
        this.aktorId = aktorId;
        this.aktorName = aktorName;
        this.registerDate = registerDate;
        this.location = location;
        this.serviceUrl = serviceUrl;
        this.status = status;
    }

    public int getAktorId() {
        return aktorId;
    }

    public void setAktorId(int aktorId) {
        this.aktorId = aktorId;
    }

    public String getAktorName() {
        return aktorName;
    }

    public void setAktorName(String aktorName) {
        this.aktorName = aktorName;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
