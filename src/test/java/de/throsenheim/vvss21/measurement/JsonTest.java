package de.throsenheim.vvss21.measurement;

import com.fasterxml.jackson.databind.JsonNode;
import de.throsenheim.vvss21.domain.interfaces.IMeasurementList;
import de.throsenheim.vvss21.domain.models.Measurement;
import de.throsenheim.vvss21.domain.models.MeasurementList;
import de.throsenheim.vvss21.common.Json;
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
    private String TestCase = "{\n" +
            "  \"value\" : 10,\n" +
            "  \"unit\" : \"CELSIUS\",\n" +
            "  \"type\" : \"TEMPERATURE\",\n" +
            "  \"timestamp\" : \"2021-04-26 17:04:10.11\"\n" +
            "}";

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
        assertEquals("Measurement{value=10, unit=CELSIUS, type=TEMPERATURE, timestamp=2021-04-26T17:04:10.110}",measurement.toString());
    }

    @Test
    void toJson() throws IOException {
        JsonNode node = Json.parse(TestCase);
        Measurement measurement = Json.fromJson(node, Measurement.class);
        node = Json.toJson(measurement);
        assertEquals("CELSIUS", node.get("unit").asText());
        assertEquals("TEMPERATURE", node.get("type").asText());
        assertEquals("10",node.get("value").asText());
        assertEquals("2021-04-26 17:04:10.11", node.get("timestamp").asText());
    }

    @Test
    void stringify() throws IOException {
        JsonNode node = Json.parse(TestCase);
        assertEquals("{\n" +
                "  \"value\" : 10,\n" +
                "  \"unit\" : \"CELSIUS\",\n" +
                "  \"type\" : \"TEMPERATURE\",\n" +
                "  \"timestamp\" : \"2021-04-26 17:04:10.11\"\n" +
                "}", Json.prittyPrint(node));
        assertEquals("{\"value\":10,\"unit\":\"CELSIUS\",\"type\":\"TEMPERATURE\",\"timestamp\":\"2021-04-26 17:04:10.11\"}", Json.stringify(node));
    }

    @Test
    void measurementListTest() throws IOException {
        List<Measurement> measurements = new LinkedList<>();
        for (int i = 0; i < 50; i++) {
            measurements.add(new Measurement(10, "CELSIUS", "TEMPERATURE", "2021-04-26 17:04:10.11"));
        }
        IMeasurementList measurementList = new MeasurementList(measurements);
        JsonNode node = Json.toJson(measurementList);
        assertEquals(measurementList,Json.fromJson(node, MeasurementList.class));
    }
}