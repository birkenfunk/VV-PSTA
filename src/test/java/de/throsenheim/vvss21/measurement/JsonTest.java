package de.throsenheim.vvss21.measurement;

import com.fasterxml.jackson.databind.JsonNode;
import de.throsenheim.vvss21.helperclasses.json.Json;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonTest {

    private static final Logger LOGGER = LogManager.getLogger(JsonTest.class);
    private String TestCase = "{\"unit\":\"CELSIUS\",\"type\":\"TEMPERATURE\",\"value\":10,\"timestamp\":\"2021-04-03 17:48:01.0\"}";

    @BeforeAll
    static void beforeAll() {
        LOGGER.debug("Executing Test Json\n\n\n");
    }

    @Test
    void parse() throws IOException {
        JsonNode node = Json.parse(TestCase);
        assertEquals("CELSIUS",node.get("unit").asText());
    }

    @Test
    void fromJson() throws IOException {
        JsonNode node = Json.parse(TestCase);
        Measurement measurement = Json.fromJson(node, Measurement.class);
        assertEquals("Measurement{value=10, unit=CELSIUS, type=TEMPERATURE, timestamp=2021-04-03 17:48:01.0}",measurement.toString());
    }

    @Test
    void toJson() throws IOException {
        JsonNode node = Json.parse(TestCase);
        Measurement measurement = Json.fromJson(node, Measurement.class);
        node = Json.toJson(measurement);
        assertEquals("CELSIUS", node.get("unit").asText());
        assertEquals("TEMPERATURE", node.get("type").asText());
        assertEquals("10",node.get("value").asText());
        assertEquals("2021-04-03 17:48:01.0", node.get("timestamp").asText());
    }

    @Test
    void stringify() throws IOException {
        JsonNode node = Json.parse(TestCase);
        assertEquals("{\n" +
                "  \"unit\" : \"CELSIUS\",\n" +
                "  \"type\" : \"TEMPERATURE\",\n" +
                "  \"value\" : 10,\n" +
                "  \"timestamp\" : \"2021-04-03 17:48:01.0\"\n" +
                "}", Json.prittyPrint(node));
        assertEquals("{\"unit\":\"CELSIUS\",\"type\":\"TEMPERATURE\",\"value\":10,\"timestamp\":\"2021-04-03 17:48:01.0\"}", Json.stringify(node));
    }

    @Test
    void measurementListTest() throws IOException {
        List<Measurement> measurements = new LinkedList<>();
        for (int i = 0; i < 50; i++) {
            measurements.add(new Measurement(10, "CELSIUS", "TEMPERATURE", "2021-04-03 17:48:01.0"));
        }
        MeasurementList measurementList = new MeasurementList(measurements);
        JsonNode node = Json.toJson(measurementList);
        assertEquals(measurementList,Json.fromJson(node,MeasurementList.class));
    }
}