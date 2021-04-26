package de.throsenheim.vvss21.measurement;

import com.fasterxml.jackson.databind.JsonNode;
import de.throsenheim.vvss21.Main;
import de.throsenheim.vvss21.helperclasses.json.Json;
import de.throsenheim.vvss21.helperclasses.readers.ReadFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.ClearEnvironmentVariable;

import java.io.File;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class MeasurementListTest {

    private static final Logger LOGGER = LogManager.getLogger(MeasurementListTest.class);
    MeasurementList measurementList = Main.getMeasurementList();

    @Test
    void add() throws InterruptedException {
        LOGGER.debug("Executing Test add\n\n\n");
        Thread thread = new Thread(measurementList);
        thread.start();
        Measurement measurement = new Measurement(10, "CELSIUS", "TEMPERATURE", LocalDateTime.now().toString());
        measurementList.add(measurement);
        Thread.sleep(1000);
        assertTrue(measurementList.getMeasurements().contains(measurement));
        measurementList.getMeasurements().remove(measurement);
        measurementList.add(null);
        assertFalse(measurementList.getMeasurements().contains(measurement));
        measurementList.stop();
    }

    @Test
    void add2() throws Exception {
        LOGGER.debug("Executing Test add\n\n\n");
        File file = new File(Main.getJsonLocation());
        JsonNode node = Json.parse(ReadFile.readFileToString(file));
        measurementList = Json.fromJson(node, MeasurementList.class);
        Thread thread = new Thread(measurementList);
        thread.start();
        if(file.exists()){
            file.delete();
        }
        Thread.sleep(1000);
        measurementList.stop();
        assertTrue(file.exists());
        assertFalse(measurementList.isRuning());
    }
}