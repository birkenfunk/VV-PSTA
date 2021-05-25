package de.throsenheim.vvss21.domain.dtoentety;

public class RuleDto {
    private String ruleName;
    private Byte treshhold;
    private int aktorId;
    private int sensorId;

    public RuleDto() {
    }

    public RuleDto(String ruleName, Byte treshhold, int aktorId, int sensorId) {
        this.ruleName = ruleName;
        this.treshhold = treshhold;
        this.aktorId = aktorId;
        this.sensorId = sensorId;
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
}
