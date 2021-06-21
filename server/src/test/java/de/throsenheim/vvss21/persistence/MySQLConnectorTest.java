package de.throsenheim.vvss21.persistence;

import de.throsenheim.vvss21.FakeDatabase;
import de.throsenheim.vvss21.domain.TemperatureUnit;
import de.throsenheim.vvss21.domain.dtoentity.ActorDto;
import de.throsenheim.vvss21.domain.dtoentity.RuleDto;
import de.throsenheim.vvss21.domain.dtoentity.SensorDataDto;
import de.throsenheim.vvss21.domain.dtoentity.SensorDto;
import de.throsenheim.vvss21.domain.exception.ActorNotFoundException;
import de.throsenheim.vvss21.domain.exception.AlreadyInDataBaseException;
import de.throsenheim.vvss21.domain.exception.SensorNotFoundException;
import de.throsenheim.vvss21.persistence.DTOMapper;
import de.throsenheim.vvss21.persistence.MySQLConnector;
import de.throsenheim.vvss21.persistence.entety.Actor;
import de.throsenheim.vvss21.persistence.entety.Rule;
import de.throsenheim.vvss21.persistence.entety.Sensor;
import de.throsenheim.vvss21.persistence.entety.SensorData;
import de.throsenheim.vvss21.persistence.repos.ActorRepo;
import de.throsenheim.vvss21.persistence.repos.RuleRepo;
import de.throsenheim.vvss21.persistence.repos.SensorDataRepo;
import de.throsenheim.vvss21.persistence.repos.SensorRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {DTOMapper.class, FakeDatabase.class})
class MySQLConnectorTest {

    @InjectMocks
    MySQLConnector mySQLConnector;

    @Mock(name = "actorRepo")
    ActorRepo actorRepo;

    @Mock(name = "ruleRepo")
    RuleRepo ruleRepo;

    @Mock(name = "sensorDataRepo")
    SensorDataRepo sensorDataRepo;

    @Mock(name = "sensorRepo")
    SensorRepo sensorRepo;

    @Autowired
    private DTOMapper dtoMapper;

    @Autowired
    private FakeDatabase database;

    private RuleDto ruleDto;

    private ActorDto actorDto;

    private SensorDto sensorDto;

    private SensorDataDto sensorDataDto;

    private Sensor sensor;

    private Actor actor;

    private Rule rule;

    private SensorData sensorData;


    @BeforeEach
    void setUp() throws SensorNotFoundException, ActorNotFoundException, AlreadyInDataBaseException {
        actorDto = new ActorDto(1,
                "TestActor",
                "TestRoom",
                "http://test.test:8080/conatct",
                "OPEN");

        sensorDto = new SensorDto(1,
                "TestSensor",
                "TestLocation",
                false);

        ruleDto = new RuleDto("Test",
                (byte) 12,
                actorDto,
                sensorDto);

        sensorDataDto = new SensorDataDto(TemperatureUnit.CELSIUS,
                Timestamp.valueOf(LocalDateTime.of(2021,6,19,11,11,11)),
                (byte) 12,sensorDto);


        actor = getActor();

        sensor = getSensor();

        rule = getRule();

        sensorData = getSensorData();


        database.getActors().clear();
        database.getSensors().clear();
        database.getRules().clear();
        database.getSensorData().clear();
        database.addSensor(sensorDto);
        database.addActor(actorDto);
        database.addRule(ruleDto);
        database.addSensorData(sensorDataDto,1);

        MockitoAnnotations.openMocks(this);
        try {
            Field dto = mySQLConnector.getClass().getDeclaredField("dtoMapper");
            dto.setAccessible(true);
            dto.set(mySQLConnector, dtoMapper);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private Rule getRule() {
        Rule rule = new Rule();
        rule.setRuleId(1);
        rule.setRuleName("Test");
        rule.setActorByAktorId(actor);
        rule.setThreshold((byte) 12);
        rule.setSensorBySensorId(sensor);
        return rule;
    }

    private SensorData getSensorData() {
        SensorData sensorData= new SensorData();
        sensorData.setSensorBySensorId(sensor);
        sensorData.setCurrentValue((byte) 12);
        sensorData.setTimestamp(Timestamp.valueOf(LocalDateTime.of(2021,6,19,11,11,11)));
        sensorData.setTemperatureUnit(TemperatureUnit.CELSIUS);
        return sensorData;
    }

    private Sensor getSensor() {
        Sensor sensor = new Sensor();
        sensor.setSensorId(1);
        sensor.setLocation("TestLocation");
        sensor.setDeleted(false);
        sensor.setSensorName("TestSensor");
        return sensor;
    }

    private Actor getActor(){
        Actor actor = new Actor();
        actor.setStatus("OPEN");
        actor.setAktorId(1);
        actor.setAktorName("TestActor");
        actor.setLocation("TestRoom");
        actor.setServiceUrl("http://test.test:8080/conatct");
        return actor;
    }

    @Test
    void testGetSensor() {
        when(sensorRepo.findById(any())).thenReturn(Optional.of(sensor));
        SensorDto ret = mySQLConnector.getSensor(sensor.getSensorId());
        assertEquals(sensorDto, ret);
        verify(sensorRepo, times(1)).findById(any());
    }

    @Test
    void testGetSensorOnNonExisting() {
        when(sensorRepo.findById(any())).thenReturn(Optional.empty());
        SensorDto ret = mySQLConnector.getSensor(1);
        assertNull(ret);
        verify(sensorRepo, times(1)).findById(any());
    }

    @Test
    void testGetSensors() {
        List<Sensor> sensors = new LinkedList<>();
        sensors.add(sensor);
        when(sensorRepo.findAll()).thenReturn(sensors);
        List<SensorDto> sensorDtos = mySQLConnector.getSensors();
        assertEquals(1, sensorDtos.size());
        assertTrue(sensorDtos.contains(sensorDto));
        verify(sensorRepo, times(1)).findAll();
    }

    @Test
    void testAddSensor() throws AlreadyInDataBaseException {
        when(sensorRepo.save(any())).thenReturn(sensor);
        when(sensorRepo.findById(any())).thenReturn(Optional.empty());

        mySQLConnector.addSensor(sensorDto);

        verify(sensorRepo, times(1)).findById(any());
        verify(sensorRepo, times(1)).save(any());
    }

    @Test
    void testAddExistingSensor() {
        when(sensorRepo.findById(any())).thenReturn(Optional.of(sensor));
        assertThrows(AlreadyInDataBaseException.class, () -> mySQLConnector.addSensor(sensorDto));
        verify(sensorRepo, times(1)).findById(any());
    }

    @Test
    void testAddAlreadyDeletedSensor() throws AlreadyInDataBaseException {
        sensor.setDeleted(true);
        when(sensorRepo.save(any())).thenReturn(sensor);
        when(sensorRepo.findById(any())).thenReturn(Optional.of(sensor));

        mySQLConnector.addSensor(sensorDto);

        verify(sensorRepo, times(1)).findById(any());
        verify(sensorRepo, times(1)).save(any());
    }

    @Test
    void testDeleteSensor() {
        when(sensorRepo.save(any())).thenReturn(sensor);
        when(sensorRepo.findById(any())).thenReturn(Optional.of(sensor));
        assertTrue(mySQLConnector.removeSensor(1));
        assertTrue(sensor.isDeleted());
        verify(sensorRepo, times(1)).findById(any());
        verify(sensorRepo, times(1)).save(any());
    }

    @Test
    void testDeleteNonExistingSensor() {
        when(sensorRepo.findById(any())).thenReturn(Optional.empty());
        assertFalse(mySQLConnector.removeSensor(1));
        verify(sensorRepo, times(1)).findById(any());
    }

    @Test
    void updateSensor() {
        sensorDto.setSensorName("Hello World!");
        when(sensorRepo.save(any())).thenReturn(sensor);
        when(sensorRepo.findById(any())).thenReturn(Optional.of(sensor));
        assertTrue(mySQLConnector.updateSensor(1, sensorDto));
        assertEquals("Hello World!", sensor.getSensorName());
        verify(sensorRepo, times(1)).findById(any());
        verify(sensorRepo, times(1)).save(any());
    }

    @Test
    void updateNonExistingSensor() {
        when(sensorRepo.findById(any())).thenReturn(Optional.empty());
        assertFalse(mySQLConnector.updateSensor(1, sensorDto));
        verify(sensorRepo, times(1)).findById(any());
    }

    @Test
    void testAddActor() throws AlreadyInDataBaseException {
        actorDto.setStatus(null);
        when(actorRepo.save(any())).thenReturn(actor);
        when(actorRepo.findById(any())).thenReturn(Optional.empty());

        mySQLConnector.addActor(actorDto);

        verify(actorRepo, times(1)).findById(any());
        verify(actorRepo, times(1)).save(any());
    }

    @Test
    void testAddExistingActor(){
        when(actorRepo.findById(any())).thenReturn(Optional.of(actor));
        assertThrows(AlreadyInDataBaseException.class, () -> mySQLConnector.addActor(actorDto));
        verify(actorRepo, times(1)).findById(any());
    }

    @Test
    void testSetActorStatus() throws ActorNotFoundException {
        when(actorRepo.save(any())).thenReturn(actor);
        when(actorRepo.findById(any())).thenReturn(Optional.of(actor));

        mySQLConnector.setActorStatus(actor.getAktorId(), "Test");

        assertEquals("Test", actor.getStatus());

        verify(actorRepo, times(1)).findById(any());
        verify(actorRepo, times(1)).save(any());
    }

    @Test
    void testSetActorStatusOnNonExisingActor() {
        when(actorRepo.findById(any())).thenReturn(Optional.empty());
        assertThrows(ActorNotFoundException.class,
                () -> mySQLConnector.setActorStatus(actor.getAktorId(), "Test"));
        verify(actorRepo, times(1)).findById(any());
    }

    @Test
    void testGetActor() {
        when(actorRepo.findById(any())).thenReturn(Optional.of(actor));
        ActorDto ret = mySQLConnector.getActor(1);
        assertEquals(actorDto, ret);
        verify(actorRepo, times(1)).findById(any());
    }

    @Test
    void testGetActorOnNonExisting() {
        when(actorRepo.findById(any())).thenReturn(Optional.empty());
        ActorDto ret = mySQLConnector.getActor(1);
        assertNull(ret);
        verify(actorRepo, times(1)).findById(any());
    }

    @Test
    void testGetActors() {
        List<Actor> actors = new LinkedList<>();
        actors.add(actor);
        when(actorRepo.findAll()).thenReturn(actors);
        List<ActorDto> actorDtos = mySQLConnector.getActors();
        assertEquals(1, actorDtos.size());
        assertTrue(actorDtos.contains(actorDto));
        verify(actorRepo, times(1)).findAll();
    }

    @Test
    void testGetRule() {
        when(ruleRepo.findById(any())).thenReturn(Optional.of(rule));
        RuleDto ret = mySQLConnector.getRule(1);
        assertEquals(ruleDto, ret);
        verify(ruleRepo, times(1)).findById(any());
    }

    @Test
    void testGetNonExistingRule() {
        when(ruleRepo.findById(any())).thenReturn(Optional.empty());
        RuleDto ret = mySQLConnector.getRule(1);
        assertNull(ret);
        verify(ruleRepo, times(1)).findById(any());
    }

    @Test
    void testGetRules() {
        List<Rule> rules = new LinkedList<>();
        rules.add(rule);
        when(ruleRepo.findAll()).thenReturn(rules);
        List<RuleDto> ruleDtos = mySQLConnector.getRules();
        assertEquals(1, ruleDtos.size());
        assertTrue(ruleDtos.contains(ruleDto));
        verify(ruleRepo, times(1)).findAll();
    }

    @Test
    void testAddRule() throws SensorNotFoundException, ActorNotFoundException, AlreadyInDataBaseException {
        when(actorRepo.findById(any())).thenReturn(Optional.of(actor));
        when(sensorRepo.findById(any())).thenReturn(Optional.of(sensor));
        when(ruleRepo.findAll()).thenReturn(new LinkedList<>());
        when(ruleRepo.save(any())).thenReturn(rule);
        assertEquals(1, mySQLConnector.addRule(ruleDto));
        verify(actorRepo, times(1)).findById(any());
        verify(sensorRepo, times(1)).findById(any());
        verify(ruleRepo, times(1)).findAll();
        verify(ruleRepo, times(1)).save(any());
    }

    @Test
    void testAddRuleWithEmptyActor() {
        when(actorRepo.findById(any())).thenReturn(Optional.empty());
        when(sensorRepo.findById(any())).thenReturn(Optional.of(sensor));
        when(ruleRepo.findAll()).thenReturn(new LinkedList<>());
        when(ruleRepo.save(any())).thenReturn(rule);
        assertThrows(ActorNotFoundException.class,
                ()-> mySQLConnector.addRule(ruleDto));
        verify(actorRepo, times(1)).findById(any());
        verify(sensorRepo, times(0)).findById(any());
        verify(ruleRepo, times(0)).findAll();
        verify(ruleRepo, times(0)).save(any());
    }

    @Test
    void testAddRuleWithEmptySensor() {
        when(actorRepo.findById(any())).thenReturn(Optional.of(actor));
        when(sensorRepo.findById(any())).thenReturn(Optional.empty());
        when(ruleRepo.findAll()).thenReturn(new LinkedList<>());
        when(ruleRepo.save(any())).thenReturn(rule);
        assertThrows(SensorNotFoundException.class,
                ()-> mySQLConnector.addRule(ruleDto));
        verify(actorRepo, times(1)).findById(any());
        verify(sensorRepo, times(1)).findById(any());
        verify(ruleRepo, times(0)).findAll();
        verify(ruleRepo, times(0)).save(any());
    }

    @Test
    void testAddRuleWithExistingRule() {
        List<Rule> rules = new LinkedList<>();
        rules.add(rule);
        when(actorRepo.findById(any())).thenReturn(Optional.of(actor));
        when(sensorRepo.findById(any())).thenReturn(Optional.of(sensor));
        when(ruleRepo.findAll()).thenReturn(rules);
        when(ruleRepo.save(any())).thenReturn(rule);
        assertThrows(AlreadyInDataBaseException.class,
                ()-> mySQLConnector.addRule(ruleDto));
        verify(actorRepo, times(1)).findById(any());
        verify(sensorRepo, times(1)).findById(any());
        verify(ruleRepo, times(1)).findAll();
        verify(ruleRepo, times(0)).save(any());
    }

    @Test
    void testGetSensorData() {
        List<SensorData> sensorData = new LinkedList<>();
        sensorData.add(this.sensorData);
        when(sensorDataRepo.findAll()).thenReturn(sensorData);
        List<SensorDataDto> sensorDataDtos = mySQLConnector.getSensorData();
        assertEquals(1, sensorDataDtos.size());
        assertTrue(sensorDataDtos.contains(sensorDataDto));
        verify(sensorDataRepo, times(1)).findAll();
    }

    @Test
    void testGetSensorDataByID() {
        when(sensorDataRepo.findById(any())).thenReturn(Optional.of(sensorData));
        assertEquals(sensorDataDto, mySQLConnector.getSensorData(1));
        verify(sensorDataRepo, times(1)).findById(any());
    }

    @Test
    void testGetSensorDataByIDForNonExistingData() {
        when(sensorDataRepo.findById(any())).thenReturn(Optional.empty());
        assertNull(mySQLConnector.getSensorData(1));
        verify(sensorDataRepo, times(1)).findById(any());
    }

    @Test
    void testGetRulesForSensor() {
        List<Rule> rules = new LinkedList<>();
        rules.add(rule);
        Rule tempRule = getRule();
        Sensor tempSensor = getSensor();
        tempSensor.setSensorId(2);
        tempRule.setRuleName("123");
        tempRule.setSensorBySensorId(tempSensor);
        rules.add(tempRule);
        when(ruleRepo.findAll()).thenReturn(rules);
        List<RuleDto> ruleDtos = mySQLConnector.getRulesForSensor(1);
        assertEquals(1, ruleDtos.size());
        verify(ruleRepo, times(1)).findAll();
    }
}