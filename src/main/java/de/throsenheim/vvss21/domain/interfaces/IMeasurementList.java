package de.throsenheim.vvss21.domain.interfaces;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.throsenheim.vvss21.domain.models.Measurement;

import java.util.List;

/**
 * Interface for handling a list of Measurements
 */
public interface IMeasurementList extends Runnable{

    /**
     * To get a {@link List} with {@link Measurement}
     * @return List of {@link Measurement} Objects
     */
    public List<Measurement> getMeasurements();

    /**
     * Adds a {@link Measurement} to a blocking queue
     * <p>If a thread is started the blocking queue will be emptied and added to the list
     * @param measurement {@link IMeasurement} object that should be added
     */
    public void add(Measurement measurement);

    /**
     * Stops the thread and saves everything to a json file
     */
    public void stop();

    /**
     * Returns if the thread with the while loop is running
     * @return false if thread is not running true if thread is running
     */
    @JsonIgnore
    public boolean isRuning();

}
