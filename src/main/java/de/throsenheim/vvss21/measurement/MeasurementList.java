package de.throsenheim.vvss21.measurement;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

public class MeasurementList {
    private List<Measurement> measurements;

    public MeasurementList(@JsonProperty("measurements") List<Measurement> measurements) {
        this.measurements = measurements;
    }

    public List<Measurement> getMeasurements() {
        return measurements;
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
