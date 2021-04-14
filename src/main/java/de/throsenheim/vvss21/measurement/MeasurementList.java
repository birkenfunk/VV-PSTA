package de.throsenheim.vvss21.measurement;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import de.throsenheim.vvss21.Main;
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

public class MeasurementList implements Runnable{
    private final List<Measurement> measurements;
    private static final Logger LOGGER = LogManager.getLogger(MeasurementList.class);
    private final BlockingQueue<Measurement> measurementBlockingQueue = new LinkedBlockingQueue<>();
    private boolean run = true;

    public MeasurementList(@JsonProperty("measurements") List<Measurement> measurements) {
        this.measurements = measurements;
    }

    public List<Measurement> getMeasurements() {
        return measurements;
    }

    public void add(Measurement measurement){
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

    private void addToMeasurement(){
        while (run) {
            try {
                Measurement measurement = measurementBlockingQueue.poll(1, TimeUnit.SECONDS);
                if (measurement != null) {
                    measurements.add(measurement);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop(){
        run = false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(measurements);
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        addToMeasurement();
    }
}
