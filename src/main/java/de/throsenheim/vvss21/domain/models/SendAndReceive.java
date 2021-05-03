package de.throsenheim.vvss21.domain.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Class for sending and receiving json objects from and to the client
 * <p>Note: This class is only for transforming Json objects that have been received and sent</p>
 * @version 1.0.0
 * @author Alexander Asbeck
 */
public class SendAndReceive{
    private String type;
    private JsonNode payload;

    /**
     * Default constructor for {@link SendAndReceive}
     * @param type The type of massage that has been received or should be sent
     * @param payload The data that has been received in a json format
     */
    public SendAndReceive(@JsonProperty("type") String type, @JsonProperty("payload") JsonNode payload) {
        this.type = type;
        this.payload = payload;
    }

    /**
     * Getter for the Type
     * @return The Type of massage that has been received
     */
    public String getType() {
        return type;
    }

    /**
     * Setter for the Type
     * @param type sets the Type of massage
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Getter for the payload
     * @return A {@link JsonNode} with the payload
     */
    public JsonNode getPayload() {
        return payload;
    }

    /**
     * Setter for the Payload
     * @param payload Sets the payload of the objet to the given {@link JsonNode}
     */
    public void setPayload(JsonNode payload) {
        this.payload = payload;
    }
}
