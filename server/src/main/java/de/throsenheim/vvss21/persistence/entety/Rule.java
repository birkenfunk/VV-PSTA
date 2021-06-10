package de.throsenheim.vvss21.persistence.entety;

import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import java.util.Objects;

/**
 * DB entity for the rule
 */
@Entity
public class Rule {
    private int ruleId;
    private String ruleName;
    private Byte threshold;
    private Sensor sensorBySensorId;
    private Actor actorByAktorId;

    public Rule(String ruleName, Byte threshold, Sensor sensorBySensorId, Actor actorByAktorId) {
        this.ruleName = ruleName;
        this.threshold = threshold;
        this.sensorBySensorId = sensorBySensorId;
        this.actorByAktorId = actorByAktorId;
    }

    public Rule() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "RuleId", nullable = false, updatable = false)
    public int getRuleId() {
        return ruleId;
    }

    public void setRuleId(int ruleId) {
        this.ruleId = ruleId;
    }

    @Basic
    @Column(name = "RuleName", nullable = false, length = 100, unique = true)
    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    @Basic
    @Range(min = 1, max = 29)
    @Column(name = "Threshold", nullable = false)
    public Byte getThreshold() {
        return threshold;
    }

    public void setThreshold(Byte threshold) {
        this.threshold = threshold;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rule rule = (Rule) o;
        return ruleId == rule.ruleId || Objects.equals(ruleName, rule.ruleName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ruleId, ruleName, threshold);
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

}
