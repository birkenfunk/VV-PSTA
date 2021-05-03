package de.throsenheim.vvss21.application.interfaces;

import de.throsenheim.vvss21.persistance.Connector;

/**
 * Interface for handling the accepting of new connections
 */
public interface IServer {

    /**
     * Method to stop the Server and the connections
     */
    void stop();
}
