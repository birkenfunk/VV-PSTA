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
import de.throsenheim.vvss21.persistence.entety.Actor;
import de.throsenheim.vvss21.persistence.entety.Rule;
import de.throsenheim.vvss21.persistence.entety.Sensor;
import de.throsenheim.vvss21.persistence.entety.SensorData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {DTOMapper.class, FakeDatabase.class})
class DTOMapperTest {

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

        actor = new Actor();
        actor.setStatus("OPEN");
        actor.setAktorId(1);
        actor.setAktorName("TestActor");
        actor.setLocation("TestRoom");
        actor.setServiceUrl("http://test.test:8080/conatct");

        sensor = new Sensor();

        sensor.setSensorId(1);
        sensor.setLocation("TestLocation");
        sensor.setDeleted(false);
        sensor.setSensorName("TestSensor");

        rule = new Rule();

        rule.setRuleName("Test");
        rule.setActorByAktorId(actor);
        rule.setThreshold((byte) 12);
        rule.setSensorBySensorId(sensor);

        sensorData = new SensorData();
        sensorData.setSensorBySensorId(sensor);
        sensorData.setCurrentValue((byte) 12);
        sensorData.setTimestamp(Timestamp.valueOf(LocalDateTime.of(2021,6,19,11,11,11)));
        sensorData.setTemperatureUnit(TemperatureUnit.CELSIUS);

        database.getActors().clear();
        database.getSensors().clear();
        database.getRules().clear();
        database.getSensorData().clear();
        database.addSensor(sensorDto);
        database.addActor(actorDto);
        database.addRule(ruleDto);
        database.addSensorData(sensorDataDto,1);
    }

    @Test
    void testSensorDtoToSensor() {
        Sensor sensor1 = dtoMapper.sensorDtoToSensor.apply(sensorDto);
        assertEquals(sensor, sensor1);
    }

    @Test
    void testSensorToSensorDto() {
        SensorDto sensorDto1 = dtoMapper.sensorToSensorDto.apply(sensor);
        assertEquals(sensorDto, sensorDto1);
    }

    @Test
    void testActorDtoToActor() {
        Actor actor1 = dtoMapper.actorDtoToActor.apply(actorDto);
        assertEquals(actor, actor1);
    }

    @Test
    void testActorToActorDto() {
        ActorDto actorDto1 = dtoMapper.actorToActorDto.apply(actor);
        assertEquals(actorDto, actorDto1);
    }

    @Test
    void testRuleDtoToRule() {
        Rule rule1 = dtoMapper.ruleDtoToRule.apply(ruleDto);
        assertEquals(rule, rule1);
    }

    @Test
    void testRuleToRuleDto() {
        RuleDto ruleDto1 = dtoMapper.ruleToRuleDto.apply(rule);
        assertEquals(ruleDto, ruleDto1);
    }

    @Test
    void testSensorDataDtoToSensorData() {
        SensorData sensorData1 = dtoMapper.sensorDataDtoSensorToData.apply(sensorDataDto);
        assertEquals(sensorData, sensorData1);
    }

    @Test
    void testSensorDataToSensorDataDto() {
        SensorDataDto sensorDataDto1 = dtoMapper.sensorDataToSensorDataDto.apply(sensorData);
        assertEquals(sensorDataDto, sensorDataDto1);
    }
}