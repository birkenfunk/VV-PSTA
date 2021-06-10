package de.throsenheim.vvss21.application;

import de.throsenheim.vvss21.domain.dtoentity.RuleDto;
import de.throsenheim.vvss21.domain.dtoentity.SensorDataDto;

import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class RuleEngine extends TimerTask {

    private static final RuleEngine RULE_ENGINE = new RuleEngine();
    private BlockingQueue<SensorDataDto> sensorDataDtos = new LinkedBlockingQueue<>();
    private IDBConnector connector;

    private RuleEngine(){

    }

    /**
     * The action to be performed by this timer task.
     */
    @Override
    public void run() {
        int size = sensorDataDtos.size();
        for (int i = 0 ; i<size; i++){
            SensorDataDto data = null;
            try {
                 data = sensorDataDtos.poll(10, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
            //List<RuleDto> rules = connector.getRulesForSensor(data.getSensorBySensorID().getSensorId());
            //TODO Logic
        }
    }

    public boolean addSensorData(SensorDataDto sensorDataDto){
        sensorDataDtos.remove(sensorDataDto);
        return sensorDataDtos.offer(sensorDataDto);
    }

    public static RuleEngine getRuleEngine() {
        return RULE_ENGINE;
    }
}
