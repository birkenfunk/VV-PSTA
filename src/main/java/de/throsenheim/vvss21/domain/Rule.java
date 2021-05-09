package de.throsenheim.vvss21.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Rule {
    private int ruleId;
    private String ruleName;
    private Byte treshhold;
    private Actor actorByAktorId;

    @Id
    @Column(name = "RuleId")
    public int getRuleId() {
        return ruleId;
    }

    public void setRuleId(int ruleId) {
        this.ruleId = ruleId;
    }

    @Basic
    @Column(name = "RuleName")
    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    @Basic
    @Column(name = "Treshhold")
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
    @JoinColumn(name = "AktorID", referencedColumnName = "AktorId")
    public Actor getActorByAktorId() {
        return actorByAktorId;
    }

    public void setActorByAktorId(Actor actorByAktorId) {
        this.actorByAktorId = actorByAktorId;
    }
}
