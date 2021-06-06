package de.throsenheim.vvss21.domain.dtoentety;

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

    public SensorDto(int sensorId, String sensorName, String location) {
        this.sensorId = sensorId;
        this.sensorName = sensorName;
        this.location = location;
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
}
