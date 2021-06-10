package de.throsenheim.vvss21.application;

import de.throsenheim.vvss21.domain.dtoentity.RuleDto;
import de.throsenheim.vvss21.domain.dtoentity.SensorDataDto;
import de.throsenheim.vvss21.domain.exception.ActorNotFoundException;
import de.throsenheim.vvss21.presentation.ContactActor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 *
 */
@Service
public class RuleEngine extends TimerTask {

    private static BlockingQueue<SensorDataDto> sensorDataDtos = new LinkedBlockingQueue<>();
    @Autowired
    private IDBConnector connector;
    @Autowired
    private ContactActor contactActor;
    private static final Logger LOGGER = LogManager.getLogger(RuleEngine.class);

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
            if(data==null)
                continue;
            List<RuleDto> rules = connector.getRulesForSensor(data.getSensorBySensorID().getSensorId());
            scanForNewStatus(data, rules);
        }
    }

    private void scanForNewStatus(SensorDataDto data, List<RuleDto> rules) {
        for (RuleDto rule: rules) {
            if(rule.getThreshold()> data.getCurrentValue()
                    && !rule.getActorByActorID().getStatus().equals("CLOSE")){
                contactActor.sendData(rule.getActorByActorID().getServiceUrl(), "CLOSE");
                try {
                    connector.setActorStatus(rule.getActorId(),"CLOSE");
                } catch (ActorNotFoundException e) {
                    LOGGER.error(e.getMessage());
                }
                continue;
            }
            if(rule.getThreshold()< data.getCurrentValue()
                    && !rule.getActorByActorID().getStatus().equals("OPEN")){
                contactActor.sendData(rule.getActorByActorID().getServiceUrl(), "OPEN");
                try {
                    connector.setActorStatus(rule.getActorId(),"OPEN");
                } catch (ActorNotFoundException e) {
                    LOGGER.error(e.getMessage());
                }
            }
        }
    }

    public boolean addSensorData(SensorDataDto sensorDataDto){
        sensorDataDtos.remove(sensorDataDto);
        return sensorDataDtos.offer(sensorDataDto);
    }
}
