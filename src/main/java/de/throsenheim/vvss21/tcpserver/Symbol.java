package de.throsenheim.vvss21.tcpserver;

/**
 * Enum for handling an connection automat
 * @version 1.0.0
 * @author Alexander Asbeck
 */
public enum Symbol {
    SENSOR_HELLO,
    STATION_HELLO,
    ACKNOWLEDGE,
    STATION_READY,
    MEASUREMENT,
    TERMINATE,
    TERMINATE_STATION,
    UNKNOWN
}
