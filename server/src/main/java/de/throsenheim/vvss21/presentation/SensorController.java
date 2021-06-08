package de.throsenheim.vvss21.presentation;

import de.throsenheim.vvss21.domain.dtoentity.SensorDataDto;
import de.throsenheim.vvss21.domain.dtoentity.SensorDto;
import de.throsenheim.vvss21.domain.entety.Sensor;
import de.throsenheim.vvss21.domain.entety.SensorData;
import de.throsenheim.vvss21.persistence.SensorDataRepo;
import de.throsenheim.vvss21.persistence.SensorRepo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/v1/sensors")
public class SensorController {

    @Autowired
    private SensorRepo sensorRepo;

    @Autowired
    private SensorDataRepo sensorDataRepo;

    private static Logger LOGGER = LogManager.getLogger(SensorController.class);

    /**
     * Rest get request for all Sensors
     * @return List of {@link SensorDto} objects
     */
    @GetMapping(value = "", produces = {
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
                map(DefaultRestController.sensorToSensorDto).
                collect(Collectors.<SensorDto> toList()));
    }

    /**
     * Rest get request for a specific Sensor
     * @param id Id of the Sensor
     * @return A {@link SensorDto} objects
     */
    @GetMapping(value = "/{id}", produces = {
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
            return ResponseEntity.ok(DefaultRestController.sensorToSensorDto.apply(res.get()));
        return ResponseEntity.notFound().build();

    }

    /**
     * Rest post request for adding a Sensor to the System
     * @param sensor Sensor that should be added
     * @return 201 Http code for success or a 400 if sensor already exists
     */
    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sensor was created", content =  @Content,
                    headers = {@Header(name = "Location", description = "Link to the Sensor")}),
            @ApiResponse(responseCode = "400", description = "Sensor already exists", content = @Content)
    })
    public ResponseEntity<SensorDto> createSensor(@RequestBody SensorDto sensor){
        String debugmsg = "Trying to add: " + sensor.toString();
        LOGGER.debug(debugmsg);
        Sensor add = DefaultRestController.sensorDtoToSensor.apply(sensor);
        add.setRegisterDate(Date.valueOf(LocalDate.now()));
        Optional<Sensor> temp = sensorRepo.findById(add.getSensorId());
        if(temp.isPresent() && !temp.get().isDeleted()){
            return ResponseEntity.badRequest().build();
        }
        sensorRepo.save(add);
        try {
            return ResponseEntity.created(new URI("http://localhost:8080/sensors/" + add.getSensorId())).build();
        } catch (URISyntaxException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Rest post request for adding a SensorData to the System
     * @param sensorData SensorData that should be added
     * @param id Id of the Sensor
     * @return 201 Http code for success or a 400 if sensor for the SensorData wasn't found
     */
    @PostMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sensordata was created", content =  @Content,
                    headers = {@Header(name = "Location", description = "Link to the Sensordata")}),
            @ApiResponse(responseCode = "400", description = "Sensor with the SensorId wasn't found", content = @Content)
    })
    public ResponseEntity<SensorDataDto> addSensorData(@PathVariable("id") int id, @RequestBody SensorDataDto sensorData){
        String debugmsg = "Trying to add: " + sensorData.toString() + " to Sensor "+ id;
        LOGGER.debug(debugmsg);
        Optional<Sensor> sensor = sensorRepo.findById(id);
        if(!sensor.isPresent())
            return ResponseEntity.badRequest().build();
        sensorData.setSensorBySensorID(DefaultRestController.sensorToSensorDto.apply(sensor.get()));
        SensorData data = DefaultRestController.sensorDataDtoSensorToData.apply(sensorData);
        if(data.getTimestamp() == null)
            data.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
        sensorDataRepo.save(data);
        try {
            return ResponseEntity.created(new URI("http://localhost:8080/sensordata/" + data.getSensorDataId())).build();
        } catch (URISyntaxException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Rest delete request for deleting a Sensor from the System
     * <p>Will only set deleted to true on the Sensor will not remove the Sensor completely from the DB</p>
     * @param id the id of the Sensor
     * @return 204 Http code for success or a 404 if the sensor wasn't found
     */
    @DeleteMapping(value = "/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Sensor was deleted", content =  @Content),
            @ApiResponse(responseCode = "404", description = "Sensor wasn't found", content = @Content)
    })
    public ResponseEntity<SensorDto> deleteSensor(@PathVariable int id) {
        String debugmsg = "Trying to delete: " + id;
        LOGGER.debug(debugmsg);
        Optional<Sensor> toDelete = sensorRepo.findById(id);
        if(!toDelete.isPresent()){
            return ResponseEntity.notFound().build();
        }
        toDelete.get().setDeleted(true);
        sensorRepo.save(toDelete.get());
        return ResponseEntity.noContent().build();
    }

    /**
     * Rest put request for updating a Sensor from the System
     * @param id the id of the Sensor
     * @param toUpdate the new data of the sensor
     * @return 204 Http code for success or a 404 if the sensor wasn't found
     */
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Sensor was updatet", content =  @Content),
            @ApiResponse(responseCode = "404", description = "Sensor wasn't found", content = @Content)
    })
    public ResponseEntity<SensorDto> updateSensor(@PathVariable("id") int id, @RequestBody SensorDto toUpdate){
        String debugmsg = "Trying to update Sensor: " + id+ " to " + toUpdate;
        LOGGER.debug(debugmsg);
        Optional<Sensor> sensor = sensorRepo.findById(id);
        if(!sensor.isPresent())
            return ResponseEntity.notFound().build();
        sensor.get().setSensorName(toUpdate.getSensorName());
        sensor.get().setLocation(toUpdate.getLocation());
        sensorRepo.save(sensor.get());
        return ResponseEntity.noContent().build();
    }

}
