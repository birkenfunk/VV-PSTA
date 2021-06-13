package de.throsenheim.vvss21.persistence;

import de.throsenheim.vvss21.application.IDBConnector;
import de.throsenheim.vvss21.domain.dtoentity.ActorDto;
import de.throsenheim.vvss21.domain.dtoentity.RuleDto;
import de.throsenheim.vvss21.domain.dtoentity.SensorDataDto;
import de.throsenheim.vvss21.domain.dtoentity.SensorDto;
import de.throsenheim.vvss21.persistence.entety.Actor;
import de.throsenheim.vvss21.persistence.entety.Rule;
import de.throsenheim.vvss21.persistence.entety.Sensor;
import de.throsenheim.vvss21.persistence.entety.SensorData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class DTOMapper {


    @Autowired
    private IDBConnector connector;

    /**
     * Function for transforming a {@link SensorDto} Object to a {@link Sensor} object
     */
    public final Function<SensorDto, Sensor> sensorDtoToSensor = sensorDto -> new Sensor(sensorDto.getSensorId(),
            sensorDto.getSensorName(),
            sensorDto.getLocation()
    );

    /**
     * Function for transforming a {@link Sensor} Object to a {@link SensorDto} object
     */
    public final Function<Sensor, SensorDto> sensorToSensorDto = sensor -> new SensorDto(sensor.getSensorId(),
            sensor.getSensorName(),
            sensor.getLocation(),
            sensor.isDeleted()
    );

    /**
     * Function for transforming a {@link SensorDataDto} Object to a {@link SensorData} object
     */
    public final Function<SensorDataDto, SensorData> sensorDataDtoSensorToData = sensorDataDto -> new SensorData(sensorDataDto.getTemperatureUnit(),
            sensorDataDto.getTimestamp(),
            sensorDataDto.getCurrentValue(),
            sensorDtoToSensor.apply(sensorDataDto.getSensorBySensorID()));

    /**
     * Function for transforming a {@link SensorData} Object to a {@link SensorDataDto} object
     */
    public final Function<SensorData, SensorDataDto> sensorDataToSensorDataDto = sensorData -> new SensorDataDto(sensorData.getTemperatureUnit(),
            sensorData.getTimestamp(),
            sensorData.getCurrentValue(),
            sensorToSensorDto.apply(sensorData.getSensorBySensorId())
    );

    /**
     * Function for transforming an {@link ActorDto} Object to a {@link Actor} object
     */
    public final Function<ActorDto, Actor> actorDtoToActor = actorDto -> new Actor(actorDto.getAktorId(),
            actorDto.getAktorName(),
            actorDto.getLocation(),
            actorDto.getServiceUrl(),
            actorDto.getStatus());

    /**
     * Function for transforming an {@link ActorDto} Object to an {@link Actor} object
     */
    public final Function<Actor, ActorDto> actorToActorDto = actor -> new ActorDto(actor.getAktorId(),
            actor.getAktorName(),
            actor.getLocation(),
            actor.getServiceUrl(),
            actor.getStatus()
    );

    /**
     * Function for transforming a {@link RuleDto} Object to a {@link Rule} object
     */
    public final Function<RuleDto, Rule> ruleDtoToRule = ruleDto ->  new Rule(
                    ruleDto.getRuleName(),
                    ruleDto.getThreshold(),
                    sensorDtoToSensor.apply(connector.getSensor(ruleDto.getSensorId())),
                    actorDtoToActor.apply(connector.getActor(ruleDto.getActorId()))
    );

    /**
     * Function for transforming a {@link Rule} Object to a {@link RuleDto} object
     */
    public final Function<Rule, RuleDto> ruleToRuleDto = rule -> new RuleDto(
            rule.getRuleName(),
            rule.getThreshold(),
            actorToActorDto.apply(rule.getActorByAktorId()),
            sensorToSensorDto.apply(rule.getSensorBySensorId())
    );

}
