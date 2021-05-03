package de.throsenheim.vvss21;

import de.throsenheim.vvss21.common.ConfigData;
import de.throsenheim.vvss21.persistance.Server;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.ClearEnvironmentVariable;
import org.junitpioneer.jupiter.SetEnvironmentVariable;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConfigDataTest {

    private Main main;
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private static final Logger LOGGER = LogManager.getLogger(ConfigDataTest.class);

    @AfterAll
    @ClearEnvironmentVariable(key = "JSON_FILE")
    @ClearEnvironmentVariable(key = "PORT")
    static void afterAll() {

    }

    @Test
    @SetEnvironmentVariable(key = "JSON_FILE", value = "test.json")
    @SetEnvironmentVariable(key = "PORT", value = "1000")
    void customEnvironmentVariables() {
        LOGGER.debug("Executing Tests customEnvironmentVariables\n\n\n");
        String[] s ={};
        main = new Main(s);
        assertEquals("test.json", ConfigData.getJsonLocation());
        assertEquals(1000,ConfigData.getPort());
    }

    @Test
    void defaultTest() {
        LOGGER.debug("Executing Tests defaultTest\n\n\n");
        String[] s ={};
        main = new Main(s);
        assertEquals("data.json",ConfigData.getJsonLocation());
        assertEquals(1024,ConfigData.getPort());
    }
}