package de.throsenheim.vvss21.presentation;

import de.throsenheim.vvss21.FakeDatabase;
import de.throsenheim.vvss21.domain.TemperatureUnit;
import de.throsenheim.vvss21.domain.dtoentity.SensorDataDto;
import de.throsenheim.vvss21.domain.dtoentity.SensorDto;
import de.throsenheim.vvss21.domain.exception.AlreadyInDataBaseException;
import de.throsenheim.vvss21.domain.exception.SensorNotFoundException;
import de.throsenheim.vvss21.presentation.controller.SensorController;
import de.throsenheim.vvss21.presentation.controller.SensorDataController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {SensorController.class, SensorDataController.class, FakeDatabase.class})
class SensorDataTest {
    @Autowired
    SensorController sensorController;
    
    @Autowired
    SensorDataController sensorDataController;  

    SensorDto sensor;
    
    SensorDataDto sensorData;

    @BeforeEach
    void setUp() {
        sensor = new SensorDto(1,
                "TestSensor",
                "TestLocation",
                false);
        sensorData = new SensorDataDto(TemperatureUnit.CELSIUS,
                Timestamp.valueOf(LocalDateTime.now()),
                (byte) 12,null);
    }

    @AfterEach
    void tearDown() {
        List<SensorDto> sensorDtos = sensorController.getAllSensors().getBody();
        List<SensorDataDto> sensorDataDtos = sensorDataController.getAllSensordata().getBody();
        assert sensorDtos != null;
        sensorDtos.clear();
        assert sensorDataDtos != null;
        sensorDataDtos.clear();
    }

    @Test
    void addSensorData() {
        sensorController.createSensor(sensor);
        ResponseEntity<SensorDataDto> response = sensorController.addSensorData(1,sensorData);
        assertEquals(201, response.getStatusCodeValue());
        assertTrue(sensorDataController.getAllSensordata().getBody().contains(sensorData));
    }

    @Test
    void addSensorDataToNonExistingSensor() {
        ResponseEntity<SensorDataDto> response = sensorController.addSensorData(1,sensorData);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void getSensorData() {
        ResponseEntity<List<SensorDataDto>> response = sensorDataController.getAllSensordata();
        List<SensorDataDto> sensordata = response.getBody();
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(sensordata);
    }

    @Test
    void getSpecificData() {
        sensorController.createSensor(sensor);
        sensorController.addSensorData(1, sensorData);
        ResponseEntity<SensorDataDto> response = sensorDataController.getAllSensordata(0);
        assertEquals(sensorData, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void getNonExistingData() {
        ResponseEntity<SensorDataDto> response = sensorDataController.getAllSensordata(100);
        assertEquals(404, response.getStatusCodeValue());
    }
}
