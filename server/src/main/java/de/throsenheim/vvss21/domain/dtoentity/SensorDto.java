package de.throsenheim.vvss21.domain.dtoentity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

/**
 * DTO entity for the senor
 */
public class SensorDto {

    @NotBlank
    @Schema(description = "Identification number of the Sensor", example = "10")
    private int sensorId;
    @NotBlank
    @Schema(description = "Name of the Sensor", example = "Temperature sensor outside")
    private String sensorName;
    @NotBlank
    @Schema(description = "Location of the Sensor", example = "garden")
    private String location;
    @Schema(hidden = true)
    @JsonIgnore
    private boolean deleted;

    public SensorDto(int sensorId, String sensorName, String location, boolean deleted) {
        this.sensorId = sensorId;
        this.sensorName = sensorName;
        this.location = location;
        this.deleted = deleted;
    }

    public SensorDto() {
    }

    public int getSensorId() {
        return sensorId;
    }

    public void setSensorId(int sensorId) {
        this.sensorId = sensorId;
    }

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SensorDto sensorDto = (SensorDto) o;
        return sensorId == sensorDto.sensorId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sensorId);
    }

    @Override
    public String toString() {
        return "SensorDto{" +
                "sensorId=" + sensorId +
                ", sensorName='" + sensorName + '\'' +
                ", location='" + location + '\'' +
                ", deleted=" + deleted +
                '}';
    }
}
