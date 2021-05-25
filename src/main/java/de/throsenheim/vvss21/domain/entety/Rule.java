package de.throsenheim.vvss21.domain.entety;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Rule {
    private int ruleId;
    private String ruleName;
    private Byte treshhold;
    private Sensor sensorBySensorId;
    private Actor actorByAktorId;

    public Rule(int ruleId, String ruleName, Byte treshhold, Sensor sensorBySensorId, Actor actorByAktorId) {
        this.ruleId = ruleId;
        this.ruleName = ruleName;
        this.treshhold = treshhold;
        this.sensorBySensorId = sensorBySensorId;
        this.actorByAktorId = actorByAktorId;
    }

    public Rule() {
    }

    @Id
    @Column(name = "RuleId", nullable = false)
    public int getRuleId() {
        return ruleId;
    }

    public void setRuleId(int ruleId) {
        this.ruleId = ruleId;
    }

    @Basic
    @Column(name = "RuleName", nullable = false, length = 100)
    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    @Basic
    @Column(name = "Treshhold", nullable = true)
    public Byte getTreshhold() {
        return treshhold;
    }

    public void setTreshhold(Byte treshhold) {
        this.treshhold = treshhold;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rule rule = (Rule) o;
        return ruleId == rule.ruleId && Objects.equals(ruleName, rule.ruleName) && Objects.equals(treshhold, rule.treshhold);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ruleId, ruleName, treshhold);
    }

    @ManyToOne
    @JoinColumn(name = "SensorID", referencedColumnName = "SensorId")
    public Sensor getSensorBySensorId() {
        return sensorBySensorId;
    }

    public void setSensorBySensorId(Sensor sensorBySensorId) {
        this.sensorBySensorId = sensorBySensorId;
    }

    @ManyToOne
    @JoinColumn(name = "AktorID", referencedColumnName = "AktorId")
    public Actor getActorByAktorId() {
        return actorByAktorId;
    }

    public void setActorByAktorId(Actor actorByAktorId) {
        this.actorByAktorId = actorByAktorId;
    }

    @Transient
    public int getActorID(){
        if(actorByAktorId!=null){
            return actorByAktorId.getAktorId();
        }
        return Integer.MIN_VALUE;
    }

    @Transient
    public int getSensorID(){
        if(actorByAktorId!=null){
            return sensorBySensorId.getSensorId();
        }
        return Integer.MIN_VALUE;
    }

}
