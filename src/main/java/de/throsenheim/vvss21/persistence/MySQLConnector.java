package de.throsenheim.vvss21.persistence;

import de.throsenheim.vvss21.application.interfaces.IDatabase;
import de.throsenheim.vvss21.domain.Actor;
import de.throsenheim.vvss21.domain.Rule;
import de.throsenheim.vvss21.domain.Sensor;
import de.throsenheim.vvss21.domain.SensorData;
import de.throsenheim.vvss21.persistence.exeptions.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MySQLConnector implements IDatabase {

    private static final MySQLConnector MY_SQL_CONNECTOR = new MySQLConnector();
    private EntityManager em;
    private static final Logger LOGGER = LogManager.getLogger(MySQLConnector.class);

    public static void main(String[] args) throws SQLIntegrityConstraintViolationException, EntityNotFoundException {
        MySQLConnector connector = MySQLConnector.getMySqlConnector();
        Sensor sensor = new Sensor();
        sensor.setSensorId(3);
        sensor.setSensorName("Test Sensor");
        sensor.setLocation("Room");
        sensor.setRegisterDate(Date.valueOf(LocalDate.now()));
        connector.addSensor(sensor);
        sensor.setSensorName("1234");
        connector.updateSensor(sensor);
        List<Sensor> sensors= connector.getSensors(new int[]{1, 2,3, 5});
        for (Sensor s:sensors) {
            LOGGER.info(s);
        }
        connector.removeSensor(sensor.getSensorId());
    }

    private MySQLConnector() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        em = emf.createEntityManager();
        createTables();
    }

    /**
     * Adds a new {@link Sensor} to the Database
     *
     * @param newSensor the new Sensor that should added
     * @throws IllegalArgumentException If SensorID already exists
     */
    @Override
    public void addSensor(Sensor newSensor) {
        em.getTransaction().begin();
        em.flush();
        try {
            em.persist(newSensor);
        }catch (Exception e){
            throw new IllegalArgumentException();
        }finally {
            em.getTransaction().commit();
        }
    }

    /**
     * Removes the Sensor with a special ID from the DB
     *
     * @param sensorID The SensorID that should be removed
     * @throws SQLIntegrityConstraintViolationException Violation of the Database integrity
     * @throws EntityNotFoundException If Entity witch should be deleted wasn't found in the database
     */
    @Override
    public void removeSensor(int sensorID) throws SQLIntegrityConstraintViolationException, EntityNotFoundException {
        em.getTransaction().begin();
        em.flush();
        Sensor sensor = em.find(Sensor.class,sensorID);
        if(sensor == null) {//if Sensor not found Throws Not Found exception
            em.getTransaction().commit();
            throw new EntityNotFoundException();
        }
        try {
            em.remove(sensor);
        }catch (Exception e){
            throw new SQLIntegrityConstraintViolationException();
        }finally {
            em.getTransaction().commit();
        }

    }

    /**
     * Updates a Sensor in the DB
     *
     * @param toUpdate New Data of the Sensor (ID has to be the same)
     */
    @Override
    public void updateSensor(Sensor toUpdate) {
        em.getTransaction().begin();
        em.find(Sensor.class,toUpdate.getSensorId());
        em.getTransaction().commit();
    }

    /**
     * Returns a list of all Sensors in the DB
     *
     * @return List of Sensors
     */
    @Override
    public List<Sensor> getSensors() {
        return em.createNativeQuery("SELECT * FROM Sensor", Sensor.class).getResultList();
    }

    /**
     * Returns a list of all {@link Sensor} in the DB with matches the ids
     *
     * @param iDs Specialises the Sensors that should be returned
     * @return List of sensors that match the ids
     */
    @Override
    public List<Sensor> getSensors(int[] iDs) {
        List<Sensor> sensors = new ArrayList<>();
        em.getTransaction().begin();
        for (int id: iDs) {
            sensors.add(em.find(Sensor.class,id));
        }
        em.getTransaction().commit();
        return sensors;
    }

    /**
     * Returns a {@link Sensor} form the DB with matches the id
     *
     * @param id@return The Sensor with the special id
     */
    @Override
    public Sensor getSensor(int id) {
        em.getTransaction().begin();
        Sensor sensor = em.find(Sensor.class, id);
        em.getTransaction().commit();
        return sensor;
    }

    /**
     * Adds a new {@link Actor} to the Database
     *
     * @param newActor the new Actor that should added
     * @throws IllegalArgumentException If ActorID already exists
     */
    @Override
    public void addActor(Actor newActor) {
        em.getTransaction().begin();
        em.persist(newActor);
        em.getTransaction().commit();
    }

    /**
     * Returns a list of all {@link Actor} in the DB
     *
     * @return List of Actors
     */
    @Override
    public List<Actor> getActors() {
        return em.createNativeQuery("SELECT * FROM Actor",Actor.class).getResultList();
    }

    /**
     * Returns a list of all {@link Actor} in the DB with matches the ids
     *
     * @param iDs Specialises the Actors that should be returned
     * @return List of Actors that match the ids
     */
    @Override
    public List<Actor> getActors(int[] iDs) {
        List<Actor> actors = new ArrayList<>();
        em.getTransaction().begin();
        for (int id: iDs) {
            actors.add(em.find(Actor.class,id));
        }
        em.getTransaction().commit();
        return actors;
    }

    /**
     * Adds a new {@link Rule } to the Database
     *
     * @param newRule the new Rule that should be added
     * @throws IllegalArgumentException If RuleID already exists
     */
    @Override
    public void addRule(Rule newRule) {
        em.getTransaction().begin();
        em.persist(newRule);
        em.getTransaction().commit();
    }

    /**
     * Returns a list of all {@link Rule} in the DB
     *
     * @return List of Rules
     */
    @Override
    public List<Rule> getRules() {
        return em.createNativeQuery("SELECT * FROM Rule",Rule.class).getResultList();
    }

    /**
     * Returns a list of all {@link Rule} in the DB with matches the ids
     *
     * @param iDs Specialises the Rules that should be returned
     * @return List of Rules that match the ids
     */
    @Override
    public List<Rule> getRules(int[] iDs) {
        List<Rule> rules = new ArrayList<>();
        em.getTransaction().begin();
        for (int id: iDs) {
            rules.add(em.find(Rule.class,id));
        }
        em.getTransaction().commit();
        return rules;
    }

    /**
     * Adds new Sensordata to the DB
     *
     * @param newData New data that should be added
     */
    @Override
    public void addSensorData(SensorData newData) {
        em.getTransaction().begin();
        em.persist(newData);
        em.getTransaction().commit();
    }

    /**
     * Creates the Tables in the DB so the Programm can work with them
     * Only creates them if they are missing
     */
    private void createTables(){

        em.getTransaction().begin();
        Query q = em.createNativeQuery("SHOW TABLES;");
        List<String> res = q.getResultList();
        if(!res.contains("Actor")){
            em.createNativeQuery("CREATE TABLE `Actor` (\n" +
                    "  `AktorId` int NOT NULL,\n" +
                    "  `AktorName` varchar(100) NOT NULL,\n" +
                    "  `RegisterDate` date NOT NULL DEFAULT (curdate()),\n" +
                    "  `Location` varchar(100) NOT NULL,\n" +
                    "  `ServiceURL` varchar(100) NOT NULL,\n" +
                    "  `Status` varchar(100) NOT NULL,\n" +
                    "  PRIMARY KEY (`AktorId`)\n" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci").executeUpdate();
        }
        if(!res.contains("Sensor")){
            em.createNativeQuery("CREATE TABLE `Sensor` (\n" +
                    "  `SensorId` int NOT NULL,\n" +
                    "  `SensorName` varchar(100) NOT NULL,\n" +
                    "  `RegisterDate` date NOT NULL DEFAULT (curdate()),\n" +
                    "  `Location` varchar(100) NOT NULL,\n" +
                    "  PRIMARY KEY (`SensorId`)\n" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci").executeUpdate();
        }
        if(!res.contains("Rule")){
            em.createNativeQuery("CREATE TABLE `Rule` (\n" +
                    "  `RuleId` int NOT NULL AUTO_INCREMENT,\n" +
                    "  `RuleName` varchar(100) NOT NULL,\n" +
                    "  `SensorID` int DEFAULT NULL,\n" +
                    "  `AktorID` int DEFAULT NULL,\n" +
                    "  `Treshhold` tinyint DEFAULT NULL,\n" +
                    "  PRIMARY KEY (`RuleId`),\n" +
                    "  KEY `SensorID` (`SensorID`),\n" +
                    "  KEY `AktorID` (`AktorID`),\n" +
                    "  CONSTRAINT `Rule_ibfk_1` FOREIGN KEY (`SensorID`) REFERENCES `Sensor` (`SensorId`) ON UPDATE CASCADE,\n" +
                    "  CONSTRAINT `Rule_ibfk_2` FOREIGN KEY (`AktorID`) REFERENCES `Actor` (`AktorId`) ON UPDATE CASCADE,\n" +
                    "  CONSTRAINT `Rule_chk_1` CHECK ((`Treshhold` between 1 and 29))\n" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci").executeUpdate();
        }
        if (!res.contains("SensorData")) {
            em.createNativeQuery("CREATE TABLE `SensorData` (\n" +
                    "  `SensorID` int DEFAULT NULL,\n" +
                    "  `TemperatureUnit` enum('Kelvin','Celsius','Fahrenheit') NOT NULL,\n" +
                    "  `Timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                    "  `CurrentValue` tinyint NOT NULL,\n" +
                    "  `SensorDataID` int NOT NULL AUTO_INCREMENT,\n" +
                    "  PRIMARY KEY (`SensorDataID`),\n" +
                    "  KEY `SensorID` (`SensorID`),\n" +
                    "  CONSTRAINT `SensorData_ibfk_1` FOREIGN KEY (`SensorID`) REFERENCES `Sensor` (`SensorId`) ON DELETE SET NULL ON UPDATE CASCADE,\n" +
                    "  CONSTRAINT `SensorData_chk_1` CHECK ((`CurrentValue` between 0 and 30))\n" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci").executeUpdate();
        }
        em.getTransaction().commit();
    }

    public static MySQLConnector getMySqlConnector() {
        return MY_SQL_CONNECTOR;
    }
}
