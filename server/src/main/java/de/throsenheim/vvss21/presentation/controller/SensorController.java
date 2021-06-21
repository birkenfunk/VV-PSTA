package de.throsenheim.vvss21.presentation.controller;

import de.throsenheim.vvss21.application.IDBConnector;
import de.throsenheim.vvss21.domain.dtoentity.SensorDataDto;
import de.throsenheim.vvss21.domain.dtoentity.SensorDto;
import de.throsenheim.vvss21.domain.exception.AlreadyInDataBaseException;
import de.throsenheim.vvss21.domain.exception.SensorNotFoundException;
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
import java.util.List;

/**
 * Class for handling Rest requests for /v1/sensors
 * @version 2.0
 * @author Alexander
 */
@Controller
@RequestMapping("/v1/sensors")
public class SensorController {

    @Autowired
    private IDBConnector connector;
    private static final Logger LOGGER = LogManager.getLogger(SensorController.class);

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
        return ResponseEntity.ok(connector.getSensors());
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
        SensorDto sensor = connector.getSensor(id);
        if(sensor == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(sensor);

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
        try {
            connector.addSensor(sensor);
        } catch (AlreadyInDataBaseException e) {
            LOGGER.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
        try {
            return ResponseEntity.created(new URI("http://localhost:8080/sensors/" + sensor.getSensorId())).build();
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
        int dataId = 0;
        try {
            dataId = connector.addSensorData(sensorData, id);
        } catch (SensorNotFoundException e) {
            LOGGER.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
        try {
            return ResponseEntity.created(new URI("http://localhost:8080/sensordata/" + dataId)).build();
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
        if(connector.removeSensor(id))
            return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
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
        if(connector.updateSensor(id,toUpdate))
            return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }

}
