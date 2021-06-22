package de.throsenheim.vvss21.presentation;

import de.throsenheim.vvss21.FakeDatabase;
import de.throsenheim.vvss21.application.IDBConnector;
import de.throsenheim.vvss21.domain.dtoentity.ActorDto;
import de.throsenheim.vvss21.domain.dtoentity.SensorDto;
import de.throsenheim.vvss21.domain.exception.AlreadyInDataBaseException;
import de.throsenheim.vvss21.presentation.controller.ActorController;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

@SpringBootTest(classes = {ActorController.class, FakeDatabase.class})
class ActorControllerTest {

    @Autowired
    ActorController controller;

    ActorDto actorDto;


    @BeforeEach
    void setUp() {
        actorDto = new ActorDto(1,
                "TestActor",
                "TestRoom",
                "http://test.test:8080/conatct",
                "OPEN");
    }

    @AfterEach
    void tearDown() {
        List<ActorDto> actorDtos = controller.getAllActors().getBody();
        assert actorDtos != null;
        actorDtos.clear();
    }

    @Test
    void testGetAllActorsResponseIsNotNull(){
        ResponseEntity<List<ActorDto>> response = controller.getAllActors();
        List<ActorDto> actors = response.getBody();
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(actors);
    }

    @Test
    void addActor() {
        ResponseEntity<ActorDto> response = controller.createActors(actorDto);
        assertEquals("Created",response.getStatusCode().getReasonPhrase());
        assertEquals(201, response.getStatusCodeValue());
        assertTrue(controller.getAllActors().getBody().contains(actorDto));
    }

    @Test
    void getSpecificActor() {
        controller.createActors(actorDto);
        ResponseEntity<ActorDto> response = controller.getActor(actorDto.getAktorId());
        assertEquals(actorDto, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void getNotExistingActor() {
        ResponseEntity<ActorDto> response = controller.getActor(10);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void addActorDouble(){
        Objects.requireNonNull(controller.getAllActors().getBody()).clear();
        ResponseEntity<ActorDto> response = controller.createActors(actorDto);
        assertEquals(201, response.getStatusCodeValue());
        assertTrue(controller.getAllActors().getBody().contains(actorDto));
        response = controller.createActors(actorDto);
        assertEquals(400, response.getStatusCodeValue());
    }
}
