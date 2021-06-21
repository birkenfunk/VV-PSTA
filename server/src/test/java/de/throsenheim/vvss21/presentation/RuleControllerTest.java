package de.throsenheim.vvss21.presentation;

import de.throsenheim.vvss21.FakeDatabase;
import de.throsenheim.vvss21.domain.dtoentity.ActorDto;
import de.throsenheim.vvss21.domain.dtoentity.RuleDto;
import de.throsenheim.vvss21.domain.dtoentity.SensorDto;
import de.throsenheim.vvss21.presentation.controller.RuleController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {RuleController.class, FakeDatabase.class})
class RuleControllerTest {

    @Autowired
    private RuleController controller;

    private RuleDto ruleDto;

    private ActorDto actorDto;

    private SensorDto sensor;

    @BeforeEach
    void setUp() {
        actorDto = new ActorDto(1,
                "TestActor",
                "TestRoom",
                "http://test.test:8080/conatct",
                "OPEN");
        sensor = new SensorDto(1,
                "TestSensor",
                "TestLocation",
                false);
        ruleDto = new RuleDto("Test",
                (byte) 12,
                actorDto,
                sensor);
    }

    @AfterEach
    void tearDown() {
        List<RuleDto> ruleDtos = controller.getAllRules().getBody();
        assert ruleDtos != null;
        ruleDtos.clear();
    }

    @Test
    void testGetAllRulesResponseIsNotNull(){
        ResponseEntity<List<RuleDto>> response = controller.getAllRules();
        List<RuleDto> rules = response.getBody();
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(rules);
    }

    @Test
    void addRule() {
        ResponseEntity<RuleDto> response = controller.createRule(ruleDto);
        assertEquals("Created",response.getStatusCode().getReasonPhrase());
        assertEquals(201, response.getStatusCodeValue());
        assertTrue(controller.getAllRules().getBody().contains(ruleDto));
    }

    @Test
    void getSpecificRule() {
        controller.createRule(ruleDto);
        ResponseEntity<RuleDto> response = controller.getRule(0);
        assertEquals(ruleDto, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void getNotExistingRule() {
        ResponseEntity<RuleDto> response = controller.getRule(10);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void addRuleDouble(){
        Objects.requireNonNull(controller.getAllRules().getBody()).clear();
        ResponseEntity<RuleDto> response = controller.createRule(ruleDto);
        assertEquals(201, response.getStatusCodeValue());
        assertTrue(controller.getAllRules().getBody().contains(ruleDto));
        response = controller.createRule(ruleDto);
        assertEquals(400, response.getStatusCodeValue());
    }
}