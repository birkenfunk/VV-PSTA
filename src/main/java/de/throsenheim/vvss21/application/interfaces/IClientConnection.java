package de.throsenheim.vvss21.application.interfaces;

/**
 * Interface for handling the client connection
 * Can be used to send and receive data from the client
 */
public interface IClientConnection extends Runnable{

    /**
     * To terminate the connection with the client and end the Tread
     */
    void stop();

    /**
     * To check if the connection is still running
     * @return True if connection is still active False if connection isn't active any more
     */
    boolean isRunning();
}
