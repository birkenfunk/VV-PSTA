package de.throsenheim.vvss21.application.interfaces;

/**
 * Interface for reading from the commandline
 * Can be used to read from the commandline
 */
public interface ICommandlineReader extends Runnable{

    /**
     * To stop the reading from the commandline
     */
    void stop();
    
}
