package de.throsenheim.vvss21.domain.dtoentity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;

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
}
