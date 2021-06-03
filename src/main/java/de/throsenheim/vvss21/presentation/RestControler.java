package de.throsenheim.vvss21.presentation;

import de.throsenheim.vvss21.domain.dtoentety.*;
import de.throsenheim.vvss21.domain.entety.*;
import de.throsenheim.vvss21.persistence.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
@ComponentScan("de.throsenheim.vvss21.persistence")
@RestController
public class RestControler {

    @Autowired
    SensorRepo sensorRepo;
    @Autowired
    SensorDataRepo sensorDataRepo;
    @Autowired
    RuleRepo ruleRepo;
    @Autowired
    ActorRepo actorRepo;

    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("Hello %s!", name);
    }

    @GetMapping(value = "/v1/sensors", produces = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE
    })
    @Operation(description = "Return all Sensors from the DB")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Sensors found",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SensorDto.class))
                    }),
    })
    public ResponseEntity<List<SensorDto>> getAllSensors(){
        return ResponseEntity.ok(sensorRepo.findAll().stream().
                map(sensorToSensorDto).
                collect(Collectors.<SensorDto> toList()));
    }

    @GetMapping(value = "/v1/sensors/{id}", produces = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE
    })
    @Operation(description = "Return a Sensor with a specific ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Sensor was found",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SensorDto.class))
                    }),
            @ApiResponse(responseCode = "404", description = "No Project with the id was found", content = @Content)
    })
    public ResponseEntity<SensorDto> getSensor(@PathVariable int id){
        Optional<Sensor> res = sensorRepo.findById(id);
        if(res.isPresent())
            return ResponseEntity.ok(sensorToSensorDto.apply(res.get()));
        return ResponseEntity.notFound().build();

    }

    @PostMapping(value = "/v1/sensors", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sensor was created", content =  @Content,
                    headers = {@Header(name = "Location", description = "Link to the Sensor")}),
            @ApiResponse(responseCode = "400", description = "Sensor already exists", content = @Content)
    })
    public ResponseEntity<SensorDto> createSensor(@RequestBody SensorDto sensor){
        Sensor add = sensorDtoToSensor.apply(sensor);
        if(sensorRepo.findById(add.getSensorId()).isPresent()){
            return ResponseEntity.badRequest().build();
        }
        sensorRepo.save(add);
        try {
            return ResponseEntity.created(new URI("http://localhost:8080/sensors/" + add.getSensorId())).build();
        } catch (URISyntaxException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping(value = "/v1/sensors/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sensordata was created", content =  @Content,
                    headers = {@Header(name = "Location", description = "Link to the Sensordata")}),
            @ApiResponse(responseCode = "400", description = "Sensor with the SensorId wasn't found", content = @Content)
    })
    public ResponseEntity<SensorDataDto> addSensorData(@PathVariable("id") int id, @RequestBody SensorDataDto sensorData){
        Optional<Sensor> sensor = sensorRepo.findById(id);
        if(!sensor.isPresent())
            return ResponseEntity.badRequest().build();
        sensorData.setSensorBySensorID(sensorToSensorDto.apply(sensor.get()));
        SensorData data = sensorDataDtoSensorToData.apply(sensorData);
        sensorDataRepo.save(data);
        try {
            return ResponseEntity.created(new URI("http://localhost:8080/sensordata/" + data.getSensorDataId())).build();
        } catch (URISyntaxException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping(value = "/v1/sensors/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Sensor was deleted", content =  @Content),
            @ApiResponse(responseCode = "404", description = "Sensor wasn't found", content = @Content)
    })
    public ResponseEntity<SensorDto> deleteSensor(@PathVariable int id) {
        Optional<Sensor> toDelete = sensorRepo.findById(id);
        if(!toDelete.isPresent()){
            return ResponseEntity.notFound().build();
        }
        toDelete.get().setDeleted(true);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/v1/sensors/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Sensor was updatet", content =  @Content),
            @ApiResponse(responseCode = "404", description = "Sensor wasn't found", content = @Content)
    })
    public ResponseEntity<SensorDto> updateSensor(@PathVariable("id") int id, @RequestBody SensorDto toUpdate){
        Optional<Sensor> sensor = sensorRepo.findById(id);
        if(!sensor.isPresent())
            return ResponseEntity.notFound().build();
        sensor.get().setSensorName(toUpdate.getSensorName());
        sensor.get().setLocation(toUpdate.getLocation());
        sensorRepo.save(sensor.get());
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/v1/actors", produces = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE
    })
    @Operation(description = "Return all Actors from the DB")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Project was found",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ActorDto.class))
                    }),
            @ApiResponse(responseCode = "404", description = "No Project with the id was found", content = @Content)
    })
    public ResponseEntity<List<ActorDto>> getAllActors(){
        return ResponseEntity.ok(actorRepo.findAll().stream().map(actorToActorDto).collect(Collectors.<ActorDto>toList()));
    }

    @GetMapping(value = "/v1/actors/{id}", produces = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE
    })
    @Operation(description = "Return an Actor with a specific ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Project was found",
                    content = {
                        @Content(mediaType = "application/json",
                                schema = @Schema(implementation = ActorDto.class))
            }),
            @ApiResponse(responseCode = "404", description = "No Project with the id was found", content = @Content)
    })
    public ResponseEntity<ActorDto> getActor(@PathVariable int id){
        Optional<Actor> actor = actorRepo.findById(id);
        if(!actor.isPresent())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(actorToActorDto.apply(actor.get()));
    }

    @PostMapping(value = "/v1/actors", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Actor was created", content =  @Content,
                    headers = {@Header(name = "Location", description = "Link to the Actor")}),
            @ApiResponse(responseCode = "400", description = "Actor already exists", content = @Content)
    })
    public ResponseEntity<ActorDto> createActors(@RequestBody ActorDto actor){
        if(actorRepo.findById(actor.getAktorId()).isPresent()){
            return ResponseEntity.badRequest().build();
        }
        Actor add = actorDtoToActor.apply(actor);
        actorRepo.save(add);
        try {
            return ResponseEntity.created(new URI("http://localhost:8080/actors/" + add.getAktorId())).build();
        } catch (URISyntaxException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value = "/v1/rules" , produces = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE
    })
    @Operation(description = "Return all Rules from the DB")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Project was found",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = RuleDto.class))
                    }),
            @ApiResponse(responseCode = "404", description = "No Project with the id was found", content = @Content)
    })
    public ResponseEntity<List<RuleDto>> getAllRules(){
        return ResponseEntity.ok(ruleRepo.findAll().stream().map(ruleToRuleDto).collect(Collectors.<RuleDto>toList()));
    }

    @GetMapping( value = "/v1/rules/{id}" , produces = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE
    })
    @Operation(description = "Return a Rule with a specific ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Project was found",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = RuleDto.class))
                    }),
            @ApiResponse(responseCode = "404", description = "No Project with the id was found", content = @Content)
    })
    public ResponseEntity<RuleDto> getRule(@PathVariable int id){
        Optional<Rule> rule = ruleRepo.findById(id);
        if(!rule.isPresent())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(ruleToRuleDto.apply(rule.get()));
    }

    @PostMapping(value = "/v1/rules", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Actor was created", content = @Content,
                    headers = {@Header(name = "Location", description = "Link to the Rule")}),
            @ApiResponse(responseCode = "400", description = "Sensor or Actor wasn't found", content = @Content)
    })
    public ResponseEntity<RuleDto> createRule(@RequestBody RuleDto rule){
        if(ruleRepo.findById(rule.getAktorId()).isPresent()){
            return ResponseEntity.badRequest().build();
        }
        Rule add = ruleDtoToRule.apply(rule);
        ruleRepo.save(add);
        try {
            return ResponseEntity.created(new URI("http://localhost:8080/actors/" + add.getRuleId())).build();
        } catch (URISyntaxException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value = "/v1/sensordata" , produces = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE
    })
    @Operation(description = "Return all Senor Data form the DB")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Project was found",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SensorDataDto.class))
                    }),
            @ApiResponse(responseCode = "404", description = "No Project with the id was found", content = @Content)
    })
    public ResponseEntity<List<SensorDataDto>> getAllSensordata(){
        return ResponseEntity.ok(sensorDataRepo.findAll().stream().map(sensorDataToSensorDataDto).collect(Collectors.<SensorDataDto>toList()));
    }

    @GetMapping(value = "/v1/sensordata/{id}", produces = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE
    })
    @Operation(description = "Return a Sensor Data with a specific ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Project was found",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SensorDataDto.class))
                    }),
            @ApiResponse(responseCode = "404", description = "No Project with the id was found", content = @Content)
    })
    public ResponseEntity<SensorDataDto> getAllSensordata(@PathVariable int id){
        Optional<SensorData> sensorData= sensorDataRepo.findById(id);
        if(!sensorData.isPresent())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(sensorDataToSensorDataDto.apply(sensorData.get()));
    }

    Function<SensorDto, Sensor> sensorDtoToSensor = sensorDto -> new Sensor(sensorDto.getSensorId(),
            sensorDto.getSensorName(),
            sensorDto.getLocation()
    );

    Function<ActorDto, Actor> actorDtoToActor = actorDto -> new Actor(actorDto.getAktorId(),
            actorDto.getAktorName(),
            actorDto.getLocation(),
            actorDto.getServiceUrl(),
            actorDto.getStatus());

    Function<RuleDto, Rule> ruleDtoToRule = ruleDto -> new Rule(
            ruleDto.getRuleName(),
            ruleDto.getTreshhold(),
            sensorRepo.getById(ruleDto.getSensorId()),
            actorRepo.getById(ruleDto.getAktorId()));

    Function<SensorDataDto, SensorData> sensorDataDtoSensorToData = sensorDataDto -> new SensorData(sensorDataDto.getTemperaturUnit(),
            sensorDataDto.getTimestamp(),
            sensorDataDto.getCurrentValue(),
            sensorDtoToSensor.apply(sensorDataDto.getSensorBySensorID()));

    Function<Sensor, SensorDto> sensorToSensorDto = sensor -> new SensorDto(sensor.getSensorId(),
            sensor.getSensorName(),
            sensor.getLocation()
    );

    Function<Actor, ActorDto> actorToActorDto = actor -> new ActorDto(actor.getAktorId(),
            actor.getAktorName(),
            actor.getLocation(),
            actor.getServiceUrl(),
            actor.getStatus());

    Function<Rule, RuleDto> ruleToRuleDto = rule -> new RuleDto(
            rule.getRuleName(),
            rule.getThreshold(),
            rule.getSensorID(),
            rule.getActorID());

    Function<SensorData, SensorDataDto> sensorDataToSensorDataDto = sensorData -> new SensorDataDto(sensorData.getTemperatureUnit(),
            sensorData.getTimestamp(),
            sensorData.getCurrentValue(),
            sensorToSensorDto.apply(sensorData.getSensorBySensorId()));
}
