
/**
 * DTO entity for the senor
 */
public class SensorDto {

    private int sensorId;
    private String sensorName;
    private String location;

    public SensorDto(int sensorId, String sensorName, String location) {
        this.sensorId = sensorId;
        this.sensorName = sensorName;
        this.location = location;
    }

    public SensorDto() {
    }

    public int getSensorId() {
        return sensorId;
    }

    public void setSensorId(int sensorId) {
        this.sensorId = sensorId;
    }

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
