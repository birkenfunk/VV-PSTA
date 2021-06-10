package de.throsenheim.vvss21.presentation.controller;

import de.throsenheim.vvss21.application.IDBConnector;
import de.throsenheim.vvss21.domain.dtoentity.SensorDataDto;
import de.throsenheim.vvss21.presentation.DatabaseMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/v1/sensordata")
public class SensorDataController {

    private IDBConnector connector = DatabaseMapper.getMySQLDatabase();

    /**
     * Rest get request for all SensorData
     * @return List of {@link SensorDataDto} objects
     */
    @GetMapping(value = "" , produces = {
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
                    })
    })
    public ResponseEntity<List<SensorDataDto>> getAllSensordata(){
        return ResponseEntity.ok(connector.getSensorData());
    }

    /**
     * Rest get request for a specific SensorData
     * @param id Id of the SensorData
     * @return An {@link SensorDataDto} object
     */
    @GetMapping(value = "/{id}", produces = {
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
        SensorDataDto sensorData = connector.getSensorData(id);
        if(sensorData == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(sensorData);
    }

}
