package de.throsenheim.vvss21.measurement;

import com.fasterxml.jackson.databind.JsonNode;
import de.throsenheim.vvss21.Main;
import de.throsenheim.vvss21.domain.enums.EType;
import de.throsenheim.vvss21.domain.enums.EUnit;
import de.throsenheim.vvss21.domain.models.Measurement;
import de.throsenheim.vvss21.domain.models.MeasurementList;
import de.throsenheim.vvss21.common.Json;
import de.throsenheim.vvss21.common.ReadFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import java.io.File;
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
        Measurement measurement = new Measurement(10, EUnit.CELSIUS, EType.TEMPERATURE, LocalDateTime.now());
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