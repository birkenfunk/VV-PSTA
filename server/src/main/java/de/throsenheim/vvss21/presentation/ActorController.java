package de.throsenheim.vvss21.presentation;

import de.throsenheim.vvss21.domain.dtoentity.ActorDto;
import de.throsenheim.vvss21.domain.entety.Actor;
import de.throsenheim.vvss21.persistence.ActorRepo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/v1/actors")
public class ActorController {

    @Autowired
    private ActorRepo actorRepo;

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
        return ResponseEntity.ok(actorRepo.findAll().stream().map(DefaultRestController.actorToActorDto).collect(Collectors.<ActorDto>toList()));
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
        Optional<Actor> actor = actorRepo.findById(id);
        if(!actor.isPresent())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(DefaultRestController.actorToActorDto.apply(actor.get()));
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
        if(actorRepo.findById(actor.getAktorId()).isPresent()){
            return ResponseEntity.badRequest().build();
        }
        Actor add = DefaultRestController.actorDtoToActor.apply(actor);
        actorRepo.save(add);
        try {
            return ResponseEntity.created(new URI("http://localhost:8080/actors/" + add.getAktorId())).build();
        } catch (URISyntaxException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
