package de.throsenheim.vvss21.domain.dtoentity;

import de.throsenheim.vvss21.domain.TemperatureUnit;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

/**
 * DTO entity for the sensorData
 */
public class SensorDataDto {
    @NotBlank
    @Schema(description = "Unit of the Temperature")
    private TemperatureUnit temperatureUnit;
    @Schema(description = "When the SensorData was measured")
    private Timestamp timestamp;
    @NotBlank
    @Size(min = 0, max = 30)
    @Schema(description = "The measured temp", example = "10")
    private byte currentValue;
    @Schema(description = "The sensor that has measured the data")
    private SensorDto sensorBySensorID;

    public SensorDataDto() {
    }

    public SensorDataDto(TemperatureUnit temperatureUnit, Timestamp timestamp, byte currentValue, SensorDto sensorDto) {
        this.temperatureUnit = temperatureUnit;
        this.timestamp = timestamp;
        this.currentValue = currentValue;
        this.sensorBySensorID = sensorDto;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public byte getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(byte currentValue) {
        this.currentValue = currentValue;
    }

    public SensorDto getSensorBySensorID() {
        return sensorBySensorID;
    }

    public void setSensorBySensorID(SensorDto sensorBySensorID) {
        this.sensorBySensorID = sensorBySensorID;
    }

    public TemperatureUnit getTemperaturUnit() {
        return temperatureUnit;
    }

    public void setTemperaturUnit(TemperatureUnit temperatureUnit) {
        this.temperatureUnit = temperatureUnit;
    }
}