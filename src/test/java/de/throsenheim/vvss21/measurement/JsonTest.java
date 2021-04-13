package de.throsenheim.vvss21.measurement;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class JsonTest {

    private String TestCase = "{\"unit\":\"CELSIUS\",\"type\":\"TEMPERATURE\",\"value\":10,\"timestamp\":\"2021-04-03 17:48:01.0\"}";

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
}