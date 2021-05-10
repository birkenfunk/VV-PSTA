package de.throsenheim.vvss21.persistence;

import de.throsenheim.vvss21.application.interfaces.IDatabase;
import de.throsenheim.vvss21.common.Config;
import de.throsenheim.vvss21.domain.Actor;
import de.throsenheim.vvss21.domain.Rule;
import de.throsenheim.vvss21.domain.Sensor;
import de.throsenheim.vvss21.domain.SensorData;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;

public class MySQLConnector implements IDatabase {

    private static final MySQLConnector MY_SQL_CONNECTOR = new MySQLConnector();
    private Connection con;
    private Statement stmt;
    private boolean running;
    private EntityManager em;

    public static void main(String[] args) throws InterruptedException {
        MySQLConnector connector = MySQLConnector.getMySqlConnector();
        Sensor sensor = new Sensor();
        sensor.setSensorId(3);
        sensor.setSensorName("Test Sensor");
        sensor.setLocation("Room");
        sensor.setRegisterDate(Date.valueOf(LocalDate.now()));
        connector.addSensor(sensor);
        sensor.setSensorName("1234");
        connector.updateSensor(sensor);
        List<Sensor> sensors= connector.getSensors();
        for (Sensor s:sensors) {
            System.out.println(s.toString());
        }
        Thread.sleep(10000);
        connector.removeSensor(sensor.getSensorId());
    }

    private MySQLConnector() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        em = emf.createEntityManager();
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
        em.persist(newSensor);
        em.getTransaction().commit();
    }

    /**
     * Removes the Sensor with a special ID from the DB
     *
     * @param sensorID The SensorID that should be removed
     */
    @Override
    public void removeSensor(int sensorID) {
        em.getTransaction().begin();
        Sensor sensor = em.find(Sensor.class,sensorID);
        em.remove(sensor);
        em.getTransaction().commit();
    }

    /**
     * Updates a Sensor in the DB
     *
     * @param toUpdate New Data of the Sensor (ID has to be the same)
     */
    @Override
    public void updateSensor(Sensor toUpdate) {
        em.getTransaction().begin();
        Sensor sensor = em.find(Sensor.class,toUpdate.getSensorId());
        sensor = toUpdate;
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
        return null;
    }

    /**
     * Adds a new {@link Actor} to the Database
     *
     * @param newActor the new Actor that should added
     * @throws IllegalArgumentException If ActorID already exists
     */
    @Override
    public void addActor(Actor newActor) {

    }

    /**
     * Returns a list of all {@link Actor} in the DB
     *
     * @return List of Actors
     */
    @Override
    public List<Actor> getActors() {
        return null;
    }

    /**
     * Returns a list of all {@link Actor} in the DB with matches the ids
     *
     * @param iDs Specialises the Actors that should be returned
     * @return List of Actors that match the ids
     */
    @Override
    public List<Actor> getActors(int[] iDs) {
        return null;
    }

    /**
     * Adds a new {@link Rule } to the Database
     *
     * @param newRule the new Rule that should be added
     * @throws IllegalArgumentException If RuleID already exists
     */
    @Override
    public void addRule(Rule newRule) {

    }

    /**
     * Returns a list of all {@link Rule} in the DB
     *
     * @return List of Rules
     */
    @Override
    public List<Rule> getRules() {
        return null;
    }

    /**
     * Returns a list of all {@link Rule} in the DB with matches the ids
     *
     * @param iDs Specialises the Rules that should be returned
     * @return List of Rules that match the ids
     */
    @Override
    public List<Rule> getRules(int[] iDs) {
        return null;
    }

    /**
     * Adds new Sensordata to the DB
     *
     * @param newData New data that should be added
     */
    @Override
    public void addSensorData(SensorData newData) {

    }

    public static MySQLConnector getMySqlConnector() {
        return MY_SQL_CONNECTOR;
    }
}
