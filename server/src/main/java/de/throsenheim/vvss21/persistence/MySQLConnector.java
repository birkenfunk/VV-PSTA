package de.throsenheim.vvss21.persistence;

import de.throsenheim.vvss21.application.IDBConnector;
import de.throsenheim.vvss21.application.RuleEngine;
import de.throsenheim.vvss21.domain.dtoentity.ActorDto;
import de.throsenheim.vvss21.domain.dtoentity.RuleDto;
import de.throsenheim.vvss21.domain.dtoentity.SensorDataDto;
import de.throsenheim.vvss21.domain.dtoentity.SensorDto;
import de.throsenheim.vvss21.persistence.entety.*;
import de.throsenheim.vvss21.domain.exception.ActorNotFoundException;
import de.throsenheim.vvss21.domain.exception.AlreadyInDataBaseException;
import de.throsenheim.vvss21.domain.exception.SensorNotFoundException;
import de.throsenheim.vvss21.persistence.repos.ActorRepo;
import de.throsenheim.vvss21.persistence.repos.RuleRepo;
import de.throsenheim.vvss21.persistence.repos.SensorDataRepo;
import de.throsenheim.vvss21.persistence.repos.SensorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Timer;
import java.util.stream.Collectors;

@Service
public class MySQLConnector implements IDBConnector {

    @Autowired
    private SensorRepo sensorRepo;
    @Autowired
    private ActorRepo actorRepo;
    @Autowired
    private SensorDataRepo sensorDataRepo;
    @Autowired
    private RuleRepo ruleRepo;
    @Autowired
    private DTOMapper dtoMapper;
    @Autowired
    private RuleEngine ruleEngine;


    /**
     * Returns a sensor for a specific ID
     *
     * @param id ID of the sensor
     * @return Sensor for a special ID
     */
    @Override
    public SensorDto getSensor(int id) {
        Optional<Sensor> res = sensorRepo.findById(id);
        if(res.isPresent())
            return dtoMapper.sensorToSensorDto.apply(res.get());
        return null;
    }

    /**
     * Returns a list of all Sensors
     *
     * @return A List of all Sensors
     */
    @Override
    public List<SensorDto> getSensors() {
        return sensorRepo.findAll().stream().
                map(dtoMapper.sensorToSensorDto).
                collect(Collectors.<SensorDto> toList());
    }

    /**
     * Adds a sensor to the DB
     *
     * @param toAdd Sensor to add to the DB
     * @throws AlreadyInDataBaseException in case if the Objet is already in the DB
     */
    @Override
    public void addSensor(SensorDto toAdd) throws AlreadyInDataBaseException {
        Optional<Sensor> temp = sensorRepo.findById(toAdd.getSensorId());
        if(temp.isPresent() && !temp.get().isDeleted()){
            throw new AlreadyInDataBaseException("Sensor already registered in DB");
        }
        Sensor add = dtoMapper.sensorDtoToSensor.apply(toAdd);
        add.setDeleted(false);
        add.setRegisterDate(Date.valueOf(LocalDate.now()));
        sensorRepo.save(add);
    }

    /**
     * Sensor that should be removed form the DB
     * @param id ID of the Sensor
     * @return True if the Sensor id deleted false if the sensor wasn't found
     */
    @Override
    public boolean removeSensor(int id) {
        Optional<Sensor> toDelete = sensorRepo.findById(id);
        if(!toDelete.isPresent())
            return false;
        toDelete.get().setDeleted(true);
        sensorRepo.save(toDelete.get());
        return true;
    }

    /**
     * Sensor that should be updated
     * @param id ID of the Sensor
     * @return True if the Sensor id updated false if the sensor wasn't found
     */
    @Override
    public boolean updateSensor(int id, SensorDto toUpdate) {
        Optional<Sensor> sensor = sensorRepo.findById(id);
        if(!sensor.isPresent())
            return false;
        sensor.get().setSensorName(toUpdate.getSensorName());
        sensor.get().setLocation(toUpdate.getLocation());
        sensorRepo.save(sensor.get());
        return true;
    }

    /**
     * Returns an actor for a specific ID
     *
     * @param id ID of the actor
     * @return Actor for a special ID
     */
    @Override
    public ActorDto getActor(int id) {
        Optional<Actor> actor = actorRepo.findById(id);
        if(!actor.isPresent())
            return null;
        return dtoMapper.actorToActorDto.apply(actor.get());
    }

    /**
     * Returns a list of all actors
     *
     * @return A List of all actors
     */
    @Override
    public List<ActorDto> getActors() {
        return actorRepo.findAll()
                .stream()
                .map(dtoMapper.actorToActorDto)
                .collect(Collectors.<ActorDto>toList());
    }

    /**
     * Adds an actor to the DB
     *
     * @param toAdd Actor to add to the DB
     * @throws AlreadyInDataBaseException in case if the Objet is already in the DB
     */
    @Override
    public void addActor(ActorDto toAdd) throws AlreadyInDataBaseException {
        if(actorRepo.findById(toAdd.getAktorId()).isPresent()){
            throw new AlreadyInDataBaseException("Actor already registered in DB");
        }
        Actor add = dtoMapper.actorDtoToActor.apply(toAdd);
        actorRepo.save(add);
    }

    /**
     * Sets a new status to an actor
     *
     * @param id     Id of the actor
     * @param status New status of the Actor
     */
    @Override
    public void setActorStatus(int id, String status) throws ActorNotFoundException {
        Optional<Actor> actor = actorRepo.findById(id);
        if(actor.isEmpty())
            throw new ActorNotFoundException("Actor not in BD");
        actor.get().setStatus(status);
        actorRepo.save(actor.get());
    }

    /**
     * Returns a rule for a specific ID
     *
     * @param id ID of the rule
     * @return Rule for a special ID
     */
    @Override
    public RuleDto getRule(int id) {
        Optional<Rule> rule = ruleRepo.findById(id);
        if(!rule.isPresent())
            return null;
        return dtoMapper.ruleToRuleDto.apply(rule.get());
    }

    /**
     * Returns a list of all rules
     *
     * @return A List of all rules
     */
    @Override
    public List<RuleDto> getRules() {
        return ruleRepo.findAll()
                .stream()
                .map(dtoMapper.ruleToRuleDto)
                .collect(Collectors.<RuleDto>toList());
    }

    /**
     * Adds a rule to the DB
     *
     * @param toAdd Rule to add to the DB
     * @throws AlreadyInDataBaseException in case if the Objet is already in the DB
     * @return ID of the Rule
     */
    @Override
    public int addRule(RuleDto toAdd) throws AlreadyInDataBaseException, ActorNotFoundException, SensorNotFoundException {
        if(actorRepo.findById(toAdd.getActorId()).isEmpty()){
            throw new ActorNotFoundException("Actor wasn't found");
        }
        if(sensorRepo.findById(toAdd.getSensorId()).isEmpty())
            throw new SensorNotFoundException("Sensor wasn't found");
        Rule add = dtoMapper.ruleDtoToRule.apply(toAdd);
        if(ruleRepo.findAll().contains(add)){
            throw new AlreadyInDataBaseException("Rule Name is already registered in DB");
        }
        ruleRepo.save(add);
        return add.getRuleId();
    }

    /**
     * Returns a sensorData for a specific id
     *
     * @param id Id of the sensorData
     * @return SensorData for a special id
     */
    @Override
    public SensorDataDto getSensorData(int id) {
        Optional<SensorData> sensorData= sensorDataRepo.findById(id);
        if(!sensorData.isPresent())
            return null;
        return dtoMapper.sensorDataToSensorDataDto.apply(sensorData.get());
    }

    /**
     * Returns a list of all sensorData
     *
     * @return A List of all sensorData
     */
    @Override
    public List<SensorDataDto> getSensorData() {
        return sensorDataRepo.findAll().stream().map(dtoMapper.sensorDataToSensorDataDto).collect(Collectors.<SensorDataDto>toList());
    }

    /**
     * Adds a SensorData to the DB
     *
     * @param toAdd SensorData to add to the DB
     * @param sensorID for the Sensor
     * @return Id of the created SensorData
     * @throws SensorNotFoundException If the Sensor for the given ID wasn't found or is deleted
     */
    @Override
    public int addSensorData(SensorDataDto toAdd, int sensorID) throws SensorNotFoundException {
        SensorDto sensor = getSensor(sensorID);
        if(sensor == null || sensor.isDeleted())
            throw new SensorNotFoundException("Sensor wasn't found");
        ruleEngine.addSensorData(toAdd);
        toAdd.setSensorBySensorID(sensor);
        SensorData data = dtoMapper.sensorDataDtoSensorToData.apply(toAdd);
        if(data.getTimestamp() == null)
            data.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
        sensorDataRepo.save(data);
        return data.getSensorDataId();
    }

    @Override
    public List<RuleDto> getRulesForSensor(int id) {
        List <RuleDto> rules = getRules();
        List<RuleDto> result = new LinkedList<>();
        for (RuleDto rule: rules) {
            if(rule.getSensorId() == id)
                result.add(rule);
        }
        return rules;
    }

}
