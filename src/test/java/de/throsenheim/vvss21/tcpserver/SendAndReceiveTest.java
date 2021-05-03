package de.throsenheim.vvss21.tcpserver;

import com.fasterxml.jackson.databind.JsonNode;
import de.throsenheim.vvss21.common.Json;
import de.throsenheim.vvss21.domain.models.SendAndReceive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class SendAndReceiveTest {

    private SendAndReceive sendAndReceive;

    @BeforeEach
    void setUp() {
        sendAndReceive = new SendAndReceive("", null);
    }

    @Test
    void setType() {
        assertEquals("", sendAndReceive.getType());
        sendAndReceive.setType("Test");
        assertEquals("Test", sendAndReceive.getType());
    }

    @Test
    void setPayload() throws IOException {
        assertEquals(null, sendAndReceive.getPayload());
        JsonNode node = Json.parse("{}");
        sendAndReceive.setPayload(node);
        assertEquals(node, sendAndReceive.getPayload());
    }
}