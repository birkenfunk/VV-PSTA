package de.throsenheim.vvss21.presentation.controller;

import de.throsenheim.vvss21.application.IDBConnector;
import de.throsenheim.vvss21.domain.dtoentity.RuleDto;
import de.throsenheim.vvss21.domain.exception.ActorNotFoundException;
import de.throsenheim.vvss21.domain.exception.AlreadyInDataBaseException;
import de.throsenheim.vvss21.domain.exception.SensorNotFoundException;
import de.throsenheim.vvss21.presentation.DatabaseMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Controller
@RequestMapping("/v1/rules")
public class RuleController {


    private final IDBConnector connector = DatabaseMapper.getMySQLDatabase();
    private static final Logger LOGGER = LogManager.getLogger(RuleController.class);





    /**
     * Rest get request for all Rules
     * @return List of {@link RuleDto} objects
     */
    @GetMapping(value = "" , produces = {
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
        return ResponseEntity.ok(connector.getRules());
    }

    /**
     * Rest get request for a specific Rule
     * @param id Id of the rule
     * @return An {@link RuleDto} object
     */
    @GetMapping( value = "/{id}" , produces = {
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
        RuleDto rule = connector.getRule(id);
        if(rule == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(rule);
    }

    /**
     * Rest post request for adding a Rule to the System
     * @param rule Rule that should be added
     * @return 201 Http code for success or a 400 if rule already exists
     */
    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Actor was created", content = @Content,
                    headers = {@Header(name = "Location", description = "Link to the Rule")}),
            @ApiResponse(responseCode = "400", description = "Name of sensor already exists", content = @Content),
            @ApiResponse(responseCode = "404", description = "Sensor or Actor wasn't found", content = @Content)
    })
    public ResponseEntity<RuleDto> createRule(@RequestBody RuleDto rule){
        int ruleID = 0;
        try {
            ruleID = connector.addRule(rule);
        } catch ( ActorNotFoundException | SensorNotFoundException e) {
            LOGGER.error(e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (AlreadyInDataBaseException e){
            LOGGER.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
        try {
            return ResponseEntity.created(new URI("http://localhost:8080/actors/" + ruleID)).build();
        } catch (URISyntaxException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
