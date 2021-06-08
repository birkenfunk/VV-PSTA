package de.throsenheim.vvss21.presentation;

import de.throsenheim.vvss21.domain.dtoentity.ActorDto;
import de.throsenheim.vvss21.domain.dtoentity.RuleDto;
import de.throsenheim.vvss21.domain.dtoentity.SensorDataDto;
import de.throsenheim.vvss21.domain.dtoentity.SensorDto;
import de.throsenheim.vvss21.domain.entety.Actor;
import de.throsenheim.vvss21.domain.entety.Rule;
import de.throsenheim.vvss21.domain.entety.Sensor;
import de.throsenheim.vvss21.domain.entety.SensorData;
import de.throsenheim.vvss21.persistence.ActorRepo;
import de.throsenheim.vvss21.persistence.SensorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.function.Function;

@RestController
public class DefaultRestController {

    @Autowired
    private static SensorRepo sensorRepo;
    @Autowired
    private static ActorRepo actorRepo;

    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("Hello %s!", name);
    }

    /**
     * Function for transforming a {@link SensorDto} Object to a {@link Sensor} object
     */
    static Function<SensorDto, Sensor> sensorDtoToSensor = sensorDto -> new Sensor(sensorDto.getSensorId(),
            sensorDto.getSensorName(),
            sensorDto.getLocation()
    );

    /**
     * Function for transforming a {@link Sensor} Object to a {@link SensorDto} object
     */
    static Function<Sensor, SensorDto> sensorToSensorDto = sensor -> new SensorDto(sensor.getSensorId(),
            sensor.getSensorName(),
            sensor.getLocation()
    );

    /**
     * Function for transforming a {@link SensorDataDto} Object to a {@link SensorData} object
     */
    static Function<SensorDataDto, SensorData> sensorDataDtoSensorToData = sensorDataDto -> new SensorData(sensorDataDto.getTemperatureUnit(),
            sensorDataDto.getTimestamp(),
            sensorDataDto.getCurrentValue(),
            sensorDtoToSensor.apply(sensorDataDto.getSensorBySensorID()));

    /**
     * Function for transforming a {@link SensorData} Object to a {@link SensorDataDto} object
     */
    static Function<SensorData, SensorDataDto> sensorDataToSensorDataDto = sensorData -> new SensorDataDto(sensorData.getTemperatureUnit(),
            sensorData.getTimestamp(),
            sensorData.getCurrentValue(),
            sensorToSensorDto.apply(sensorData.getSensorBySensorId())
    );

    /**
     * Function for transforming an {@link ActorDto} Object to a {@link Actor} object
     */
    static Function<ActorDto, Actor> actorDtoToActor = actorDto -> new Actor(actorDto.getAktorId(),
            actorDto.getAktorName(),
            actorDto.getLocation(),
            actorDto.getServiceUrl(),
            actorDto.getStatus());

    /**
     * Function for transforming an {@link ActorDto} Object to an {@link Actor} object
     */
    static Function<Actor, ActorDto> actorToActorDto = actor -> new ActorDto(actor.getAktorId(),
            actor.getAktorName(),
            actor.getLocation(),
            actor.getServiceUrl(),
            actor.getStatus()
    );

    /**
     * Function for transforming a {@link RuleDto} Object to a {@link Rule} object
     */
    static Function<RuleDto, Rule> ruleDtoToRule = ruleDto -> new Rule(
            ruleDto.getRuleName(),
            ruleDto.getTreshhold(),
            sensorRepo.getById(ruleDto.getSensorId()),
            actorRepo.getById(ruleDto.getAktorId())
    );

    /**
     * Function for transforming a {@link Rule} Object to a {@link RuleDto} object
     */
    static Function<Rule, RuleDto> ruleToRuleDto = rule -> new RuleDto(
            rule.getRuleName(),
            rule.getThreshold(),
            actorToActorDto.apply(rule.getActorByAktorId()),
            sensorToSensorDto.apply(rule.getSensorBySensorId())
    );
}
