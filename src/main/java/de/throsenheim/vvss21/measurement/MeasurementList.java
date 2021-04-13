package de.throsenheim.vvss21.measurement;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import de.throsenheim.vvss21.helperclasses.writers.WriteFiles;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class MeasurementList {
    private final List<Measurement> measurements;
    private static final Logger LOGGER = LogManager.getLogger(MeasurementList.class);

    public MeasurementList(@JsonProperty("measurements") List<Measurement> measurements) {
        this.measurements = measurements;
    }
    // TODO: 13.04.21  BlockingQue
    public List<Measurement> getMeasurements() {
        return measurements;
    }

    public void add(Measurement measurement){
        measurements.add(measurement);

    }

    private void saveToJsonFile(File file){
        List<String> strings = new LinkedList<>();
        JsonNode node = Json.toJson(this);
        try {
            strings.add(Json.prittyPrint(node));
        } catch (IOException e) {
            LOGGER.error(e);
        }
        WriteFiles.writeFile(file, strings, true);
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
