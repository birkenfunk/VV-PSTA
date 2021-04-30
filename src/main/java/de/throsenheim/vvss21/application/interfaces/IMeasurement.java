package de.throsenheim.vvss21.application.interfaces;

import de.throsenheim.vvss21.measurement.Type;
import de.throsenheim.vvss21.measurement.Unit;

/**
 * Interface for handling measurement objects
 */
public interface IMeasurement{

    /**
     * Getter of value
     * @return Integer with the value of the measurement
     */
    public int getValue();

    /**
     * Getter of Unit
     * @return String with the Unit of the measurement
     */
    public Unit getUnit();

    /**
     * Getter of Type
     * @return String with the Type of the measurement
     */
    public Type getType();

    /**
     * Getter of Timestamp
     * @return String with the Timestamp of the measurement with the pattern: 'yyyy-MM-dd HH:mm:ss.SS'
     */
    public String getTimestamp();

}
