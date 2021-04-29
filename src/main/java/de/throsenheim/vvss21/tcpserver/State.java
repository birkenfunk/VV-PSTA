package de.throsenheim.vvss21.tcpserver;

/**
 * Enum for handling an connection automat
 * @version 1.0.0
 * @author Alexander Asbeck
 */
public enum State {
    WAIT_FOR_CLIENT,
    WAIT_FOR_ACKNOWLEDGE,
    WAIT_FOR_MEASUREMENT,
    TERMINATED,
    ERROR
}
