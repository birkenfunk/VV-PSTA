package de.throsenheim.vvss21.domain;

public class RuleDto {
    private int ruleId;
    private String ruleName;
    private Byte treshhold;
    private int aktorId;
    private int sensorId;

    public int getRuleId() {
        return ruleId;
    }

    public void setRuleId(int ruleId) {
        this.ruleId = ruleId;
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
