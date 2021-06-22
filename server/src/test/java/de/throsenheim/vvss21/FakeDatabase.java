package de.throsenheim.vvss21;

import de.throsenheim.vvss21.application.IDBConnector;
import de.throsenheim.vvss21.domain.dtoentity.ActorDto;
import de.throsenheim.vvss21.domain.dtoentity.RuleDto;
import de.throsenheim.vvss21.domain.dtoentity.SensorDataDto;
import de.throsenheim.vvss21.domain.dtoentity.SensorDto;
import de.throsenheim.vvss21.domain.exception.ActorNotFoundException;
import de.throsenheim.vvss21.domain.exception.AlreadyInDataBaseException;
import de.throsenheim.vvss21.domain.exception.SensorNotFoundException;

import java.util.LinkedList;
import java.util.List;

public class FakeDatabase implements IDBConnector {

    List<SensorDto> sensorDtos = new LinkedList<>();
    List<SensorDataDto> sensorDataDtos = new LinkedList<>();
    List<ActorDto> actorDtos = new LinkedList<>();
    List<RuleDto> ruleDtos = new LinkedList<>();


    /**
     * Returns a sensor for a specific id
     *
     * @param id id of the sensor
     * @return Sensor for a special id
     */
    @Override
    public SensorDto getSensor(int id) {
        for (SensorDto sensor: sensorDtos ) {
            if(sensor.getSensorId() == id)
                return sensor;
        }
        return null;
    }

    /**
     * Returns a list of all Sensors
     *
     * @return A List of all Sensors
     */
    @Override
    public List<SensorDto> getSensors() {
        return sensorDtos;
    }

    /**
     * Adds a sensor to the DB
     *
     * @param toAdd Sensor to add to the DB
     * @throws AlreadyInDataBaseException in case if the Objet is already in the DB
     */
    @Override
    public void addSensor(SensorDto toAdd) throws AlreadyInDataBaseException {
        if(sensorDtos.contains(toAdd))
            throw new AlreadyInDataBaseException("Sensor already in DB");
        sensorDtos.add(toAdd);
    }

    /**
     * Sensor that should be removed form the DB
     *
     * @param id id of the Sensor
     * @return True if the Sensor id deleted false if the sensor wasn't found
     */
    @Override
    public boolean removeSensor(int id) {
        for (SensorDto sensor: sensorDtos) {
            if(sensor.getSensorId() == id) {
                sensor.setDeleted(true);
                return true;
            }
        }
        return false;
    }

    /**
     * Sensor that should be updated
     *
     * @param id       id of the Sensor
     * @param toUpdate
     * @return True if the Sensor id updated false if the sensor wasn't found
     */
    @Override
    public boolean updateSensor(int id, SensorDto toUpdate) {
        SensorDto sensorDto = getSensor(id);
        if(sensorDto == null)
            return false;
        sensorDto.setSensorName(toUpdate.getSensorName());
        sensorDto.setLocation(toUpdate.getLocation());
        return true;
    }

    /**
     * Returns an actor for a specific id
     *
     * @param id id of the actor
     * @return Actor for a special id
     */
    @Override
    public ActorDto getActor(int id) {
        for (ActorDto actor: actorDtos ) {
            if(actor.getAktorId() == id)
                return actor;
        }
        return null;
    }

    /**
     * Returns a list of all actors
     *
     * @return A List of all actors
     */
    @Override
    public List<ActorDto> getActors() {
        return actorDtos;
    }

    /**
     * Adds an actor to the DB
     *
     * @param toAdd Actor to add to the DB
     * @throws AlreadyInDataBaseException in case if the Objet is already in the DB
     */
    @Override
    public void addActor(ActorDto toAdd) throws AlreadyInDataBaseException {
        if(actorDtos.contains(toAdd))
            throw new AlreadyInDataBaseException("Actor already in DB");
        actorDtos.add(toAdd);
    }

    /**
     * Sets a new status to an actor
     *
     * @param id     Id of the actor
     * @param status New status of the Actor
     */
    @Override
    public void setActorStatus(int id, String status) throws ActorNotFoundException {
        ActorDto actor = getActor(id);
        if(actor == null)
            throw new ActorNotFoundException("Actor wasn't found");
        actor.setStatus(status);
    }

    /**
     * Returns a rule for a specific id
     *
     * @param id id of the rule
     * @return Rule for a special id
     */
    @Override
    public RuleDto getRule(int id) {
        if(id >= ruleDtos.size())
            return null;
        return ruleDtos.get(id);
    }

    /**
     * Returns a list of all rules
     *
     * @return A List of all rules
     */
    @Override
    public List<RuleDto> getRules() {
        return ruleDtos;
    }

    /**
     * Adds a rule to the DB
     *
     * @param toAdd Rule to add to the DB
     * @return ID of the Rule
     * @throws AlreadyInDataBaseException in case if the Objet is already in the DB
     */
    @Override
    public int addRule(RuleDto toAdd) throws AlreadyInDataBaseException, ActorNotFoundException, SensorNotFoundException {
        if(ruleDtos.contains(toAdd))
            throw new AlreadyInDataBaseException("Rule already in DB");
        ruleDtos.add(toAdd);
        return 1;
    }

    /**
     * Returns a sensorData for a specific id
     *
     * @param id id of the sensorData
     * @return SensorData for a special id
     */
    @Override
    public SensorDataDto getSensorData(int id) {
        if(id >= sensorDataDtos.size())
            return null;
        return sensorDataDtos.get(id);
    }

    /**
     * Returns a list of all sensorData
     *
     * @return A List of all sensorData
     */
    @Override
    public List<SensorDataDto> getSensorData() {
        return sensorDataDtos;
    }

    /**
     * Adds a SensorData to the DB
     *
     * @param toAdd    SensorData to add to the DB
     * @param sensorID Id for the Sensor
     * @return Id of the created SensorData
     * @throws SensorNotFoundException If the Sensor for the given ID wasn't found or is deleted
     */
    @Override
    public int addSensorData(SensorDataDto toAdd, int sensorID) throws SensorNotFoundException {
        SensorDto sensor = getSensor(sensorID);
        if(sensor == null)
            throw new SensorNotFoundException("Sensor wasn't found");
        toAdd.setSensorBySensorID(sensor);
        sensorDataDtos.add(toAdd);
        return sensorDataDtos.size()-1;
    }

    @Override
    public List<RuleDto> getRulesForSensor(int id) {
        return null;
    }

}
