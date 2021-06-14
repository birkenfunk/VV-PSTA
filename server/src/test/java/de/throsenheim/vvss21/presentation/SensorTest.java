package de.throsenheim.vvss21.presentation;

import de.throsenheim.vvss21.FakeDatabase;
import de.throsenheim.vvss21.domain.dtoentity.ActorDto;
import de.throsenheim.vvss21.domain.dtoentity.SensorDto;
import de.throsenheim.vvss21.domain.exception.AlreadyInDataBaseException;
import de.throsenheim.vvss21.presentation.controller.SensorController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {SensorController.class, FakeDatabase.class})
class SensorTest {

    @Autowired
    SensorController controller;

    SensorDto sensor;

    @BeforeEach
    void setUp() {
        sensor = new SensorDto(1,
                "TestSensor",
                "TestLocation",
                false);
    }

    @AfterEach
    void tearDown() {
        List<SensorDto> sensorDtos = controller.getAllSensors().getBody();
        assert sensorDtos != null;
        sensorDtos.clear();
    }

    @Test
    void testGetAllSensorsResponseIsNotNull(){
        ResponseEntity<List<SensorDto>> response = controller.getAllSensors();
        List<SensorDto> sensors = response.getBody();
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(sensors);
    }

    @Test
    void addSensor() {
        ResponseEntity<SensorDto> response = controller.createSensor(sensor);
        assertEquals("Created",response.getStatusCode().getReasonPhrase());
        assertEquals(201, response.getStatusCodeValue());
        assertTrue(controller.getAllSensors().getBody().contains(sensor));
    }

    @Test
    void getSpecificSensor() {
        controller.createSensor(sensor);
        ResponseEntity<SensorDto> response = controller.getSensor(sensor.getSensorId());
        assertEquals(sensor, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void getNotExistingSensor() {
        ResponseEntity<SensorDto> response = controller.getSensor(10);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void addSensorDouble(){
        ResponseEntity<SensorDto> response = controller.createSensor(sensor);
        assertEquals(201, response.getStatusCodeValue());
        assertTrue(controller.getAllSensors().getBody().contains(sensor));
        response = controller.createSensor(sensor);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void deleteSensor(){
        ResponseEntity<SensorDto> response = controller.createSensor(sensor);
        assertEquals(201, response.getStatusCodeValue());
        response = controller.deleteSensor(1);
        assertEquals(204, response.getStatusCodeValue());
        assertTrue(controller.getSensor(1).getBody().isDeleted());
    }

    @Test
    void deleteNonExistingSensor(){
        ResponseEntity<SensorDto> response = controller.deleteSensor(10);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void updateSensor(){
        ResponseEntity<SensorDto> response = controller.createSensor(sensor);
        assertEquals(201, response.getStatusCodeValue());
        sensor.setSensorName("Test2");
        response = controller.updateSensor(1,sensor);
        assertEquals(204, response.getStatusCodeValue());
        assertEquals("Test2", controller.getSensor(1).getBody().getSensorName());
    }

    @Test
    void updateNonExistingSensor() {
        ResponseEntity<SensorDto> response = controller.updateSensor(10, sensor);
        assertEquals(404, response.getStatusCodeValue());
    }
}
