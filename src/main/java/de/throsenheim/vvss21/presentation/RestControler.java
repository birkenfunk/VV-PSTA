package de.throsenheim.vvss21.presentation;

import de.throsenheim.vvss21.application.interfaces.IDatabase;
import de.throsenheim.vvss21.domain.dtoentety.ActorDto;
import de.throsenheim.vvss21.domain.dtoentety.RuleDto;
import de.throsenheim.vvss21.domain.dtoentety.SensorDataDto;
import de.throsenheim.vvss21.domain.dtoentety.SensorDto;
import de.throsenheim.vvss21.domain.entety.Actor;
import de.throsenheim.vvss21.domain.entety.Rule;
import de.throsenheim.vvss21.domain.entety.Sensor;
import de.throsenheim.vvss21.domain.entety.SensorData;
import de.throsenheim.vvss21.persistence.MySQLConnector;
import de.throsenheim.vvss21.persistence.exeptions.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
public class RestControler {

    IDatabase database = MySQLConnector.getMySqlConnector();

    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("Hello %s!", name);
    }

    @GetMapping("/sensors")
    public ResponseEntity<List<SensorDto>> getAllSensors(){
        return ResponseEntity.ok(database.getSensors().stream().
                map(sensorToSensorDto).
                collect(Collectors.<SensorDto> toList()));
    }

    @GetMapping("/sensors/{id}")
    public ResponseEntity<SensorDto> getSensor(@PathVariable int id){
        Sensor res = database.getSensor(id);
        if(res==null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(sensorToSensorDto.apply(res));
    }

    @PostMapping("/sensors")
    public ResponseEntity<SensorDto> createSensor(@RequestBody SensorDto sensor){
        if(database.getSensor(sensor.getSensorId())!=null){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
        Sensor data = sensorDtoToSensor.apply(sensor);
        database.addSensor(data);
        return ResponseEntity.ok(sensorToSensorDto.apply(data));
    }

    @PostMapping("/sensors/{id}")
    public ResponseEntity<SensorDataDto> addSensorData(@RequestBody SensorDataDto sensorData){
        if(database.getSensor(sensorData.getSensorId())!=null){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
        SensorData data = sensorDataDtoSensorToData.apply(sensorData);
        database.addSensorData(data);
        return ResponseEntity.ok(sensorDataToSensorDataDto.apply(data));
    }

    @DeleteMapping("/sensors/{id}")
    public ResponseEntity<SensorDto> deleteSensor(@PathVariable int id) {
        try {
            database.removeSensor(id);
            return ResponseEntity.noContent().build();
        }catch (EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }catch (SQLIntegrityConstraintViolationException e){
            return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).build();
        }
    }

    @GetMapping("/actors")
    public ResponseEntity<List<ActorDto>> getAllActors(){
        return ResponseEntity.ok(database.getActors().stream().map(actorToActorDto).collect(Collectors.<ActorDto>toList()));
    }

    @GetMapping("/actors/{id}")
    public ResponseEntity<ActorDto> getActor(@PathVariable int id){
        Actor res = database.getActor(id);
        if(res==null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(actorToActorDto.apply(res));
    }

    @PostMapping("/actors")
    public ResponseEntity<ActorDto> createActors(@RequestBody ActorDto actor){
        if(database.getSensor(actor.getAktorId())!=null){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
        Actor data = actorDtoToActor.apply(actor);
        database.addActor(data);
        return ResponseEntity.ok(actorToActorDto.apply(data));
    }

    @GetMapping("/rules")
    public ResponseEntity<List<RuleDto>> getAllRules(){
        return ResponseEntity.ok(database.getRules().
                stream().map(ruleToRuleDto).
                collect(Collectors.<RuleDto>toList()));
    }

    @GetMapping("/rules/{id}")
    public ResponseEntity<RuleDto> getRule(@PathVariable int id){
        Rule res = database.getRule(id);
        if(res==null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(ruleToRuleDto.apply(res));
    }

    @PostMapping("/rules")
    public ResponseEntity<RuleDto> createRule(@RequestBody RuleDto rule){
        if(database.getSensor(rule.getAktorId())!=null){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
        Rule data = ruleDtoToRule.apply(rule);
        database.addRule(data);
        return ResponseEntity.ok(ruleToRuleDto.apply(data));
    }


    Function<SensorDto, Sensor> sensorDtoToSensor = sensorDto -> new Sensor(sensorDto.getSensorId(),
            sensorDto.getSensorName(),
            sensorDto.getRegisterDate(),
            sensorDto.getLocation()
    );

    Function<ActorDto, Actor> actorDtoToActor = actorDto -> new Actor(actorDto.getAktorId(),
            actorDto.getAktorName(),
            actorDto.getRegisterDate(),
            actorDto.getLocation(),
            actorDto.getServiceUrl(),
            actorDto.getStatus());

    Function<RuleDto, Rule> ruleDtoToRule = ruleDto -> new Rule(ruleDto.getRuleId(),
            ruleDto.getRuleName(),
            ruleDto.getTreshhold(),
            database.getSensor(ruleDto.getSensorId()),
            database.getActor(ruleDto.getAktorId()));

    Function<SensorDataDto, SensorData> sensorDataDtoSensorToData = sensorDataDto -> new SensorData(sensorDataDto.getTemperaturUnit(),
            sensorDataDto.getTimestamp(),
            sensorDataDto.getCurrentValue(),
            database.getSensor(sensorDataDto.getSensorId()));

    Function<Sensor, SensorDto> sensorToSensorDto = sensor -> new SensorDto(sensor.getSensorId(),
            sensor.getSensorName(),
            sensor.getRegisterDate(),
            sensor.getLocation()
    );

    Function<Actor, ActorDto> actorToActorDto = actor -> new ActorDto(actor.getAktorId(),
            actor.getAktorName(),
            actor.getRegisterDate(),
            actor.getLocation(),
            actor.getServiceUrl(),
            actor.getStatus());

    Function<Rule, RuleDto> ruleToRuleDto = rule -> new RuleDto(rule.getRuleId(),
            rule.getRuleName(),
            rule.getTreshhold(),
            rule.getSensorID(),
            rule.getActorID());

    Function<SensorData, SensorDataDto> sensorDataToSensorDataDto = sensorData -> new SensorDataDto(sensorData.getTemperatureUnit(),
            sensorData.getTimestamp(),
            sensorData.getCurrentValue(),
            sensorData.getSensorID());
}
