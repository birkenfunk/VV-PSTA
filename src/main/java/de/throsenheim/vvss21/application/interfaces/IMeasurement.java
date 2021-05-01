package de.throsenheim.vvss21.application.interfaces;

import de.throsenheim.vvss21.application.enums.EType;
import de.throsenheim.vvss21.application.enums.EUnit;

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
     * Getter of EUnit
     * @return String with the EUnit of the measurement
     */
    public EUnit getUnit();

    /**
     * Getter of EType
     * @return String with the EType of the measurement
     */
    public EType getType();

    /**
     * Getter of Timestamp
     * @return String with the Timestamp of the measurement with the pattern: 'yyyy-MM-dd HH:mm:ss.SS'
     */
    public String getTimestamp();

}
