package de.throsenheim.vvss21.measurement;

import com.fasterxml.jackson.databind.JsonNode;
import de.throsenheim.vvss21.Main;
import de.throsenheim.vvss21.helperclasses.json.Json;
import de.throsenheim.vvss21.helperclasses.readers.ReadFile;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class MeasurementListTest {

    MeasurementList measurementList = Main.getMeasurementList();

    @Test
    void add() throws InterruptedException {
        Thread thread = new Thread(measurementList);
        thread.start();
        Measurement measurement = new Measurement(10, "CELSIUS", "TEMPERATURE", Timestamp.valueOf(LocalDateTime.now()).toString());
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
        File file = new File("data.json");
        JsonNode node = Json.parse(ReadFile.readFileToString(file));
        measurementList = Json.fromJson(node, MeasurementList.class);
        Thread thread = new Thread(measurementList);
        thread.start();
        if(file.exists()){
            file.delete();
        }
        measurementList.stop();
        assertTrue(file.exists());
        assertTrue(thread.isAlive());
    }
}