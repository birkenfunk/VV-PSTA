package de.throsenheim.vvss21.tcpserver;

import com.fasterxml.jackson.databind.JsonNode;
import de.throsenheim.vvss21.common.Json;
import de.throsenheim.vvss21.domain.enums.ESymbol;
import de.throsenheim.vvss21.domain.models.SendAndReceive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class SendAndReceiveTest {

    private SendAndReceive sendAndReceive;

    @BeforeEach
    void setUp() {
        sendAndReceive = new SendAndReceive(ESymbol.UNKNOWN, null);
    }

    @Test
    void setType() {
        assertEquals(ESymbol.UNKNOWN, sendAndReceive.getType());
        sendAndReceive.setType(ESymbol.SENSOR_HELLO);
        assertEquals(ESymbol.SENSOR_HELLO, sendAndReceive.getType());
    }

    @Test
    void setPayload() throws IOException {
        assertEquals(null, sendAndReceive.getPayload());
        JsonNode node = Json.parse("{}");
        sendAndReceive.setPayload(node);
        assertEquals(node, sendAndReceive.getPayload());
    }
}