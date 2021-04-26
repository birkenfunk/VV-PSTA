package de.throsenheim.vvss21.tcpserver;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public class SendAndReceive{
    private String type;
    private JsonNode payload;

    public SendAndReceive(@JsonProperty("type") String type, @JsonProperty("payload") JsonNode payload) {
        this.type = type;
        this.payload = payload;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public JsonNode getPayload() {
        return payload;
    }

    public void setPayload(JsonNode payload) {
        this.payload = payload;
    }
}
