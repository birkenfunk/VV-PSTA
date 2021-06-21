package de.throsenheim.vvss21.domain.dtoentity;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

/**
 * DTO entity for the Actor
 */
public class ActorDto {
    @NotBlank
    @Schema(description = "Identification number of the Actor", example = "10")
    private int aktorId;
    @NotBlank
    @Schema(description = "Name of the Actor", example = "ActorXY")
    private String aktorName;
    @NotBlank
    @Schema(description = "Name of the Location of the Actor", example = "Kitchen")
    private String location;
    @NotBlank
    @Schema(description = "Service URL of the Actor", example = "http://192.168.178.14/")
    private String serviceUrl;
    @NotBlank
    @Schema(description = "Status of the Actor", example = "Open")
    private String status;

    public ActorDto() {
    }

    public ActorDto(int aktorId, String aktorName, String location, String serviceUrl, String status) {
        this.aktorId = aktorId;
        this.aktorName = aktorName;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActorDto actorDto = (ActorDto) o;
        return aktorId == actorDto.aktorId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(aktorId);
    }

    @Override
    public String toString() {
        return "ActorDto{" +
                "aktorId=" + aktorId +
                ", aktorName='" + aktorName + '\'' +
                ", location='" + location + '\'' +
                ", serviceUrl='" + serviceUrl + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
