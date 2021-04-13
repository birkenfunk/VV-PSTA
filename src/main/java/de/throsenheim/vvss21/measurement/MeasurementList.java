package de.throsenheim.vvss21.measurement;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class MeasurementList {
    List<Measurement> measurements;

    public MeasurementList(@JsonProperty("measurements") List<Measurement> measurements) {
        this.measurements = measurements;
    }
}
