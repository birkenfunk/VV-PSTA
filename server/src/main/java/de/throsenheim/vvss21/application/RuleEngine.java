package de.throsenheim.vvss21.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.throsenheim.vvss21.domain.dtoentity.ActorDto;
import de.throsenheim.vvss21.domain.dtoentity.RuleDto;
import de.throsenheim.vvss21.domain.dtoentity.SensorDataDto;
import de.throsenheim.vvss21.domain.exception.ActorNotFoundException;
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
 * Class for checking the set rules
 * @version 2.0
 * @author Alexander
 */
@Service
public class RuleEngine extends TimerTask {

    private static final BlockingQueue<SensorDataDto> sensorDataDtos = new LinkedBlockingQueue<>();
    @Autowired
    private IDBConnector connector;
    @Autowired
    private IContactActor contactActor;
    @Autowired
    private IWeatherService weatherService;

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
                LOGGER.error(e.getMessage());
                Thread.currentThread().interrupt();
            }
            if(data==null)
                continue;
            List<RuleDto> rules = connector.getRulesForSensor(data.getSensorBySensorID().getSensorId());
            scanForNewStatus(data, rules);
        }
        weatherService();
    }

    /**
     * Compares if the new data violates any rule of the senor
     * @param data new Data
     * @param rules rules it should be compared to
     */
    private void scanForNewStatus(SensorDataDto data, List<RuleDto> rules) {
        for (RuleDto rule: rules) {
            String status = "CLOSE";
            if(rule.getThreshold() < data.getCurrentValue()
                    && !rule.getActorByActorID().getStatus().equals(status)){
                contactActor.contact(rule.getActorByActorID().getServiceUrl(), status);
                try {
                    connector.setActorStatus(rule.getActorId(),status);
                } catch (ActorNotFoundException e) {
                    LOGGER.error(e.getMessage());
                }
                continue;
            }
            status = "OPEN";
            if(rule.getThreshold() > data.getCurrentValue()
                    && !rule.getActorByActorID().getStatus().equals(status)){
                contactActor.contact(rule.getActorByActorID().getServiceUrl(), status);
                try {
                    connector.setActorStatus(rule.getActorId(),status);
                } catch (ActorNotFoundException e) {
                    LOGGER.error(e.getMessage());
                }
            }
        }
    }

    /**
     * Changes the status of the actors if the weather is sunny
     */
    private void weatherService(){

        JsonNode node;
        String weather = "";
        try {
            node = new ObjectMapper().readTree(weatherService.contactWeatherService());
            weather = node.findValue("summary").asText();
        } catch (JsonProcessingException e) {
            LOGGER.error(e.getMessage());
        }
        if(weather.equalsIgnoreCase("Sunny")) {
            LOGGER.info("It is sunny my friends");
            changeActors();
        }
    }

    /**
     * Changes the status of all actors
     */
    private void changeActors(){
        for (ActorDto actor:connector.getActors()) {
            String newStatus = "CLOSE";
            if(actor.getStatus().equals("OPEN")) {
                contactActor.contact(actor.getServiceUrl(), newStatus);
                try {
                    connector.setActorStatus(actor.getAktorId(),newStatus);
                } catch (ActorNotFoundException e) {
                    LOGGER.error(e.getMessage());
                }
            }else {
                newStatus = "OPEN";
                contactActor.contact(actor.getServiceUrl(), newStatus);
                try {
                    connector.setActorStatus(actor.getAktorId(), newStatus);
                } catch (ActorNotFoundException e) {
                    LOGGER.error(e.getMessage());
                }
            }

        }
    }

    /**
     * Adds sensordata that should be compared
     * @param sensorDataDto new sensordata
     * @return True if everything went right false if data couldn't be added
     */
    public boolean addSensorData(SensorDataDto sensorDataDto){
        sensorDataDtos.remove(sensorDataDto);
        return sensorDataDtos.offer(sensorDataDto);
    }
}
