package de.throsenheim.vvss21.presentation;

import de.throsenheim.vvss21.application.interfaces.IDatabase;
import de.throsenheim.vvss21.domain.*;
import de.throsenheim.vvss21.persistence.MySQLConnector;
import de.throsenheim.vvss21.persistence.exeptions.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.function.Function;

@RestController
public class RestControler {

    IDatabase database = MySQLConnector.getMySqlConnector();

    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("Hello %s!", name);
    }

    @GetMapping("/sensors")
    public ResponseEntity getAllSensors(){
        return ResponseEntity.ok(database.getSensors());
    }

    @GetMapping("/sensors/{id}")
    public ResponseEntity getSensor(@PathVariable int id){
        Sensor s = database.getSensor(id);
        if(s==null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(s);
    }

    @PostMapping("/sensors")
    public ResponseEntity createSensor(@RequestBody SensorDto sensor){
        if(database.getSensor(sensor.getSensorId())!=null){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }

        database.addSensor(sensorDtoToSensor.apply(sensor));
        return ResponseEntity.ok(sensor);
    }

    @DeleteMapping("/sensors/{id}")
    public ResponseEntity deleteSensor(@PathVariable int id) {
        try {
            database.removeSensor(id);
            return ResponseEntity.noContent().build();
        }catch (EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }catch (SQLIntegrityConstraintViolationException e){
            return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).build();
        }
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
            sensorDataDto.getSensorDataId(),
            database.getSensor(sensorDataDto.getSensorId()));
}
