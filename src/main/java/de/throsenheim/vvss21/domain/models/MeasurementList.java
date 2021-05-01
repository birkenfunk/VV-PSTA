package de.throsenheim.vvss21.measurement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import de.throsenheim.vvss21.Main;
import de.throsenheim.vvss21.application.interfaces.IMeasurement;
import de.throsenheim.vvss21.domain.models.Measurement;
import de.throsenheim.vvss21.helperclasses.json.Json;
import de.throsenheim.vvss21.helperclasses.writers.WriteFiles;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Class to store {@link Measurement}
 * <p> Can be converted to a Json
 * @author Alexander Asbeck
 * @version 1.1.0
 */
public class MeasurementList implements Runnable{
    private final List<IMeasurement> measurements;
    private static final Logger LOGGER = LogManager.getLogger(MeasurementList.class);
    private final BlockingQueue<IMeasurement> measurementBlockingQueue = new LinkedBlockingQueue<>();
    private boolean run = true;

    /**
     * Constructor for Class
     * @param measurements {@link List} with {@link Measurement}
     */
    public MeasurementList(@JsonProperty("measurements") List<IMeasurement> measurements) {
        this.measurements = measurements;
    }

    /**
     * To get a {@link List} with {@link Measurement}
     * @return List of {@link Measurement} Objects
     */
    public List<IMeasurement> getMeasurements() {
        return measurements;
    }

    /**
     * Adds a {@link Measurement} to a blocking queue
     * <p>If a thread is started the blocking queue will be emptied and added to the list
     * @param measurement {@link Measurement} object that should be added
     */
    public void add(IMeasurement measurement){
        if(measurement == null){
            return;
        }
        try {
            boolean success = measurementBlockingQueue.offer(measurement);
            if(!success){
                LOGGER.debug("Inserting Measurement went wrong");
            }
        }catch (Exception e){
            LOGGER.error(e);
        }


    }

    /**
     * Saves the Class to a Json file
     */
    private void saveToJsonFile(){
        List<String> strings = new LinkedList<>();
        JsonNode node = Json.toJson(this);
        try {
            strings.add(Json.prittyPrint(node));
        } catch (IOException e) {
            LOGGER.error(e);
        }
        File storeTo = new File(Main.getJsonLocation());
        WriteFiles.writeFile(storeTo, strings, true);
    }



    /**
     * Removes objects from the measurement list and adds them to the list
     */
    private void addToMeasurement(){
        while (run) {
            try {
                IMeasurement measurement = measurementBlockingQueue.poll(1, TimeUnit.SECONDS);
                if (measurement != null) {
                    String debugString = "Consumed: "+ measurement;
                    LOGGER.debug(debugString);
                    measurements.add(measurement);
                }
            } catch (InterruptedException e) {
                LOGGER.error(e);
                Thread.currentThread().interrupt();
            }
        }
        LOGGER.debug("Remove from Blocking queue stopped");
    }

    /**
     * Stops the thread and saves everything to a json file
     */
    public void stop(){
        run = false;
        saveToJsonFile();
    }

    /**
     * Returns if the thread with the while loop is running
     * @return false if thread is not running true if thread is running
     */
    @JsonIgnore
    public boolean isRuning() {
        return run;
    }

    /**
     * Starts the Thread and moves objects from the blocking queue to a list
     * @see Thread#run()
     */
    @Override
    public void run() {
        run = true;
        LOGGER.debug("Remove from Blocking queue stated");
        addToMeasurement();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MeasurementList that = (MeasurementList) o;
        if(measurements.size()!=that.measurements.size()){
            return false;
        }
        for (int i = 0; i < measurements.size(); i++) {
            if(!measurements.get(i).equals(that.measurements.get(i))){
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(measurements);
    }
}
