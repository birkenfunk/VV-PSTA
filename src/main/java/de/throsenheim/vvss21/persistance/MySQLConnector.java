package de.throsenheim.vvss21.persistance;

import com.sun.security.auth.login.ConfigFile;
import de.throsenheim.vvss21.application.interfaces.IDatabase;
import de.throsenheim.vvss21.common.Config;
import de.throsenheim.vvss21.domain.Actor;
import de.throsenheim.vvss21.domain.Rule;
import de.throsenheim.vvss21.domain.Sensor;
import de.throsenheim.vvss21.domain.SensorData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class MySQLConnector implements IDatabase {

    private static final MySQLConnector MY_SQL_CONNECTOR = new MySQLConnector();
    private Connection con;
    private Statement stmt;
    private boolean running;

    private MySQLConnector() {
    }

    private void open(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://"+ Config.getdBURL() +"?useSSL=false&serverTimezone=UTC",Config.getdBUsername(),Config.getdBPassword());
            stmt = con.createStatement();
            running=true;
        }catch (SQLException | ClassNotFoundException e){
            running=false;
            e.printStackTrace();
        }
    }

    /**
     * Adds a new {@link Sensor} to the Database
     *
     * @param newSensor the new Sensor that should added
     * @throws IllegalArgumentException If SensorID already exists
     */
    @Override
    public void addSensor(Sensor newSensor) {

    }

    /**
     * Removes the Sensor with a special ID from the DB
     *
     * @param sensorID The SensorID that should be removed
     */
    @Override
    public void removeSensor(int sensorID) {

    }

    /**
     * Updates a Sensor in the DB
     *
     * @param toUpdate New Data of the Sensor (ID has to be the same)
     */
    @Override
    public void updateSensor(Sensor toUpdate) {

    }

    /**
     * Returns a list of all Sensors in the DB
     *
     * @return List of Sensors
     */
    @Override
    public List<Sensor> getSensors() {
        return null;
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
}
