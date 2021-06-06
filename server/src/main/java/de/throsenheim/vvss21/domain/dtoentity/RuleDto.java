package de.throsenheim.vvss21.domain.dtoentity;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * DTO entity for the rule
 */
public class RuleDto {
    @NotBlank
    @Schema(description = "Name of the rule", example = "Close window")
    private String ruleName;
    @NotBlank
    @Size(min = 1, max = 29)
    @Schema(description = "When the rule should do something", example = "10")
    private Byte treshhold;
    @NotBlank
    @Schema(description = "Identification number of the Actor", example = "10")
    private int aktorId;
    @Schema(description = "Actor for this rule")
    private ActorDto actorByActorID;
    @NotBlank
    @Schema(description = "Identification number of the Sensor", example = "10")
    private int sensorId;
    @Schema(description = "Sensor for this rule")
    private SensorDto sensorBySensorID;

    public RuleDto() {
    }

    public RuleDto(String ruleName, Byte treshhold, ActorDto actorDto, SensorDto sensorDto) {
        this.ruleName = ruleName;
        this.treshhold = treshhold;
        this.actorByActorID = actorDto;
        this.sensorBySensorID = sensorDto;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public Byte getTreshhold() {
        return treshhold;
    }

    public void setTreshhold(Byte treshhold) {
        this.treshhold = treshhold;
    }
    public int getAktorId() {
        return aktorId;
    }

    public void setAktorId(int aktorId) {
        this.aktorId = aktorId;
    }

    public int getSensorId() {
        return sensorId;
    }

    public void setSensorId(int sensorId) {
        this.sensorId = sensorId;
    }

    public ActorDto getActorByActorID() {
        return actorByActorID;
    }

    public void setActorByActorID(ActorDto actorByActorID) {
        this.actorByActorID = actorByActorID;
    }

    public SensorDto getSensorBySensorID() {
        return sensorBySensorID;
    }

    public void setSensorBySensorID(SensorDto sensorBySensorID) {
        this.sensorBySensorID = sensorBySensorID;
    }
}
