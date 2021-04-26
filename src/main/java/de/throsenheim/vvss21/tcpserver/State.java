package de.throsenheim.vvss21.tcpserver;

public enum State {
    WAIT_FOR_CLIENT,
    WAIT_FOR_ACKNOWLEDGE,
    WAIT_FOR_MEASUREMENT,
    TERMINATED,
    ERROR
}
