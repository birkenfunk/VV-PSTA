package de.throsenheim.vvss21.domain.enums;

/**
 * Enum for handling an connection automat
 * @version 1.0.0
 * @author Alexander Asbeck
 */
public enum EState {
    WAIT_FOR_CLIENT,
    WAIT_FOR_ACKNOWLEDGE,
    WAIT_FOR_MEASUREMENT,
    TERMINATED,
    ERROR,
    WAITFORCLIENT,
    WAITFORACKNOWLEDGE,
    WAITFORMEASUREMENT
}
