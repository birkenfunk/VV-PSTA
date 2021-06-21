package de.throsenheim.vvss21.presentation.controller;

import de.throsenheim.vvss21.application.IDBConnector;
import de.throsenheim.vvss21.domain.dtoentity.ActorDto;
import de.throsenheim.vvss21.domain.exception.AlreadyInDataBaseException;
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
 * Class for handling Rest requests for /v1/actors
 * @version 2.0
 * @author Alexander
 */
@Controller
@RequestMapping("/v1/actors")
public class ActorController {

    @Autowired
    private IDBConnector connector;
    private static final Logger LOGGER = LogManager.getLogger(ActorController.class);

    /**
     * Rest get request for all Actors
     * @return List of {@link ActorDto} objects
     */
    @GetMapping(value = "", produces = {
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
        return ResponseEntity.ok(connector.getActors());
    }

    /**
     * Rest get request for a specific Actor
     * @param id Id of the actor
     * @return An {@link ActorDto} object
     */
    @GetMapping(value = "/{id}", produces = {
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
        ActorDto actor = connector.getActor(id);
        if(actor == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(actor);
    }

    /**
     * Rest post request for adding an Actor to the System
     * @param actor Actor that should be added
     * @return 201 Http code for success or a 400 if actor already exists
     */
    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Actor was created", content =  @Content,
                    headers = {@Header(name = "Location", description = "Link to the Actor")}),
            @ApiResponse(responseCode = "400", description = "Actor already exists", content = @Content)
    })
    public ResponseEntity<ActorDto> createActors(@RequestBody ActorDto actor){
        try {
            connector.addActor(actor);
        } catch (AlreadyInDataBaseException e) {
            LOGGER.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
        try {
            return ResponseEntity.created(new URI("http://localhost:8080/actors/" + actor.getAktorId())).build();
        } catch (URISyntaxException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
