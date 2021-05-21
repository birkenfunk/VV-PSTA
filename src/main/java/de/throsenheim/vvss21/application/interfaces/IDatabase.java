package de.throsenheim.vvss21.application.interfaces;

import de.throsenheim.vvss21.domain.Actor;
import de.throsenheim.vvss21.domain.Rule;
import de.throsenheim.vvss21.domain.Sensor;
import de.throsenheim.vvss21.domain.SensorData;
import de.throsenheim.vvss21.persistence.exeptions.EntityNotFoundException;

import java.sql.SQLIntegrityConstraintViolationException;
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

    /**
     * Removes the Sensor with a special ID from the DB
     * @param sensorID The SensorID that should be removed
     * @throws SQLIntegrityConstraintViolationException Violation of the Database integrity
     * @throws EntityNotFoundException If Entity witch should be deleted wasn't found in the database
     */
    void removeSensor(int sensorID) throws SQLIntegrityConstraintViolationException, EntityNotFoundException;

    /**
     * Updates a Sensor in the DB
     * @param toUpdate New Data of the Sensor (ID has to be the same)
     */
    void updateSensor(Sensor toUpdate);

    /**
     * Returns a list of all Sensors in the DB
     * @return List of Sensors
     */
    List<Sensor> getSensors();

    /**
     * Returns a list of all {@link Sensor} in the DB with matches the ids
     * @param iDs Specialises the Sensors that should be returned
     * @return List of sensors that match the ids
     */
    List<Sensor> getSensors(int[] iDs);

    /**
     * Returns a {@link Sensor} form the DB with matches the id
     * @param id Specialises the Sensor that should be returned
     * @return The Sensor with the special id
     */
    Sensor getSensor(int id);

    /**
     * Adds a new {@link Actor} to the Database
     * @param newActor the new Actor that should added
     * @throws IllegalArgumentException If ActorID already exists
     */
    void addActor(Actor newActor);

    /**
     * Returns a list of all {@link Actor} in the DB
     * @return List of Actors
     */
    List<Actor> getActors();

    /**
     * Returns a list of all {@link Actor} in the DB with matches the ids
     * @param iDs Specialises the Actors that should be returned
     * @return List of Actors that match the ids
     */
    List<Actor> getActors(int[] iDs);

    /**
     * Adds a new {@link Rule } to the Database
     * @param newRule the new Rule that should be added
     * @throws IllegalArgumentException If RuleID already exists
     */
    void addRule(Rule newRule);

    /**
     * Returns a list of all {@link Rule} in the DB
     * @return List of Rules
     */
    List<Rule> getRules();

    /**
     * Returns a list of all {@link Rule} in the DB with matches the ids
     * @param iDs Specialises the Rules that should be returned
     * @return List of Rules that match the ids
     */
    List<Rule> getRules(int[] iDs);

    /**
     * Adds new Sensordata to the DB
     * @param newData New data that should be added
     */
    void addSensorData(SensorData newData);

}
