package de.throsenheim.vvss21.application.interfaces;

import de.throsenheim.vvss21.tcpserver.Connector;

/**
 * Interface for handling the accepting of new connections
 */
public interface IServer {

    /**
     * Method to remove a Connector from the Connector list in case the connection is closed
     * @param connector Connector that should be removed
     * @return True if operation was a success False if operation failed
     */
    boolean removeConnector(Connector connector);


    /**
     * Method to stop the Server and the connections
     */
    void stop();
}
