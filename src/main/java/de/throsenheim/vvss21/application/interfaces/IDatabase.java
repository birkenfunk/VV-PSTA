package de.throsenheim.vvss21.application.interfaces;

import de.throsenheim.vvss21.domain.Aktor;
import de.throsenheim.vvss21.domain.Rule;
import de.throsenheim.vvss21.domain.Sensor;
import de.throsenheim.vvss21.domain.SensorData;

import java.util.List;

/**
 * Menages interactions with a dbms.
 * Can be used to store or read data
 */
public interface IDatabase {

    /**
     * Adds a new {@link Sensor} to the Database
     * @param newSensor the new Sensor that should added
     * @throws IllegalArgumentException If SensorID already exists
     */
    void addSensor(Sensor newSensor);

    void removeSensor(int sensorID);

    void updateSensor(Sensor toUpdate);

    List<Sensor> getSensors();

    List<Sensor> getSensors(int[] iDs);

    /**
     * Adds a new {@link Aktor} to the Database
     * @param newAktor the new Aktor that should added
     * @throws IllegalArgumentException If AktorID already exists
     */
    void addAktor(Aktor newAktor);

    List<Aktor> getActors();

    List<Aktor> getActors(int[] iDs);

    /**
     * Adds a new {@link Rule } to the Database
     * @param newRule the new Rule that should be added
     * @throws IllegalArgumentException If RuleID already exists
     */
    void addRule(Rule newRule);

    List<Rule> getRules();

    List<Rule> getRules(int[] iDs);

    void addSensorData(SensorData newData);

}
