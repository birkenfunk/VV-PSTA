package de.throsenheim.vvss21.application;

import de.throsenheim.vvss21.domain.dtoentity.ActorDto;
import de.throsenheim.vvss21.domain.dtoentity.RuleDto;
import de.throsenheim.vvss21.domain.dtoentity.SensorDataDto;
import de.throsenheim.vvss21.domain.dtoentity.SensorDto;
import de.throsenheim.vvss21.domain.exception.ActorNotFoundException;
import de.throsenheim.vvss21.domain.exception.AlreadyInDataBaseException;
import de.throsenheim.vvss21.domain.exception.SensorNotFoundException;

import java.util.List;

/**
 * Interface for the DB
 */
public interface IDBConnector {

    /**
     * Returns a sensor for a specific ID
     * @param ID ID of the sensor
     * @return  Sensor for a special ID
     */
    SensorDto getSensor(int ID);

    /**
     * Returns a list of all Sensors
     * @return A List of all Sensors
     */
    List<SensorDto> getSensors();

    /**
     * Adds a sensor to the DB
     * @param toAdd Sensor to add to the DB
     * @throws AlreadyInDataBaseException in case if the Objet is already in the DB
     */
    void addSensor(SensorDto toAdd) throws AlreadyInDataBaseException;

    /**
     * Sensor that should be removed form the DB
     * @param ID ID of the Sensor
     * @return True if the Sensor id deleted false if the sensor wasn't found
     */
    boolean removeSensor(int ID);

    /**
     * Sensor that should be updated
     * @param ID ID of the Sensor
     * @return True if the Sensor id updated false if the sensor wasn't found
     */
    boolean updateSensor(int ID, SensorDto toUpdate);

    /**
     * Returns an actor for a specific ID
     * @param ID ID of the actor
     * @return  Actor for a special ID
     */
    ActorDto getActor(int ID);

    /**
     * Returns a list of all actors
     * @return A List of all actors
     */
    List<ActorDto> getActors();

    /**
     * Adds an actor to the DB
     * @param toAdd Actor to add to the DB
     * @throws AlreadyInDataBaseException in case if the Objet is already in the DB
     */
    void addActor(ActorDto toAdd) throws AlreadyInDataBaseException;

    /**
     * Returns a rule for a specific ID
     * @param ID ID of the rule
     * @return  Rule for a special ID
     */
    RuleDto getRule(int ID);

    /**
     * Returns a list of all rules
     * @return A List of all rules
     */
    List<RuleDto> getRules();

    /**
     * Adds a rule to the DB
     * @param toAdd Rule to add to the DB
     * @throws AlreadyInDataBaseException in case if the Objet is already in the DB
     * @return ID of the Rule
     */
    int addRule(RuleDto toAdd) throws AlreadyInDataBaseException, ActorNotFoundException, SensorNotFoundException;

    /**
     * Returns a sensorData for a specific ID
     * @param ID ID of the sensorData
     * @return  SensorData for a special ID
     */
    SensorDataDto getSensorData(int ID);

    /**
     * Returns a list of all sensorData
     * @return A List of all sensorData
     */
    List<SensorDataDto> getSensorData();

    /**
     * Adds a SensorData to the DB
     * @param toAdd SensorData to add to the DB
     * @param sensorID Id for the Sensor
     * @return Id of the created SensorData
     * @throws SensorNotFoundException If the Sensor for the given ID wasn't found or is deleted
     */
    int addSensorData(SensorDataDto toAdd, int sensorID) throws SensorNotFoundException;

    List<RuleDto> getRulesForSensor(int ID);
}
