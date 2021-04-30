package de.throsenheim.vvss21.application.interfaces;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.Logger;

/**
 * Interface for logging data
 * Can be used to add content to the Log
 */
public interface ILogger extends Logger {
    void received(JsonNode node);
}
