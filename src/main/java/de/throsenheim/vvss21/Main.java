package de.throsenheim.vvss21;

import com.fasterxml.jackson.databind.JsonNode;
import de.throsenheim.vvss21.common.Json;
import de.throsenheim.vvss21.common.ReadFile;
import de.throsenheim.vvss21.domain.models.MeasurementList;
import de.throsenheim.vvss21.tcpserver.Server;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;

/**
 * Main Class from where the program should be started
 * @author Alexander
 * @version 1.3.2
 */
public class Main {

    public static void main(String[] args) {
        new Main(args);
    }
    private static final Logger LOGGER = LogManager.getLogger(Main.class);
    private static MeasurementList measurementList;
    private static String jsonLocation = "data.json";
    private static int port = 1024;
    private final ReadConsole readConsole;

    /**
     * Constructor for Main Class
     * Used to run the Program
     * @param args args given by main
     */
    public Main(String[] args) {
        this.readConsole = new ReadConsole();
        logStartup();
        readProperties();
        getMeasurementList();
        Thread measurementSave  = new Thread(measurementList);
        measurementSave.start();
        Thread consoleRead = new Thread(readConsole);
        consoleRead.start();
        Thread serverThread = new Thread(Server.getSERVER());
        serverThread.start();
    }

    /**
     * Method reads the properties out of a file and sets it
     */
    private static void readProperties(){
        try (InputStream fileInputStream = Main.class.getClassLoader().getResourceAsStream("alexanderasbeck.properties")){
            Properties properties = new Properties();
            properties.load(fileInputStream);
            String temp;
            if(properties.containsKey("JSON_FILE")) {
                temp = properties.getProperty("JSON_FILE");
                jsonLocation = transformProperties(temp);
            }
            if(properties.containsKey("PORT")) {
                temp = properties.getProperty("PORT");
                temp = transformProperties(temp);
                port = Integer.parseInt(temp);
            }
        } catch (IOException e) {
            LOGGER.error(e);
        }

    }

    /**
     * Transforms a property string into a Correct property
     * @param property String from the property file
     * @return String with the correct data
     */
    private static String transformProperties(String property){
        if(!property.startsWith("${") && !property.endsWith("}")){
            return property;
        }
        String copyProperty = property.replaceAll("[${|}]","");
        String[] splitedProperty = copyProperty.split(":");
        if (!splitedProperty[0].equals("env") && splitedProperty.length<3){
            return property;
        }
        Map<String, String> env = System.getenv();
        if(env.containsKey(splitedProperty[1])){
            return env.get(splitedProperty[1]);
        }
        return splitedProperty[2].replaceAll("[-]","");
    }

    /**
     * Returns the location for the json file
     * <p> Gets location from the config file
     * @return location for the json file
     */
    public static String getJsonLocation() {
        return jsonLocation;
    }

    /**
     * Returns the {@link MeasurementList} that is stored in the main class
     * <p>Creates a new {@link MeasurementList} if measurementList in main is null
     * @return MeasurementList that is stored in the main class
     */
    public static MeasurementList getMeasurementList() {
        readProperties();
        if(measurementList == null){
            File jsonFile = new File(jsonLocation);
            measurementList = new MeasurementList(new LinkedList<>());
            if(!jsonFile.exists()){
                return measurementList;
            }
            String jsonString = ReadFile.readFileToString(jsonFile);
            if(jsonString.isEmpty()){
                return measurementList;
            }
            try {
                JsonNode node = Json.parse(jsonString);
                measurementList = Json.fromJson(node, MeasurementList.class);
            } catch (IOException e) {
                LOGGER.error(e);
            }
        }
        return measurementList;
    }

    public static int getPort() {
        readProperties();
        return port;
    }

    /**
     * Method writes the start variables to the log
     */
    private void logStartup(){
        String debugMsg = "Program start";
        LOGGER.debug(debugMsg);
        debugMsg = "Java Version: "+ System.getProperty("java.version");
        LOGGER.debug(debugMsg);
        debugMsg = "OS: "+ System.getProperty("os.name");
        LOGGER.debug(debugMsg);
        debugMsg = "OS Version: "+System.getProperty("os.version");
        LOGGER.debug(debugMsg);
        debugMsg = "Architecture: "+System.getProperty("os.arch");
        LOGGER.debug(debugMsg);
        debugMsg = "User: "+System.getProperty("user.name");
        LOGGER.debug(debugMsg);

    }

    /**
     * Class that reads from input from the Console
     * @author Alexander Asbeck
     * @version 1.1.0
     */
    class ReadConsole implements Runnable{
        private boolean read = true;
        /**
         * Starts a Commandline reader
         *
         * @see Thread#run()
         */
        @Override
        public void run() {
            String line;

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            try {
                while (read){
                    line = reader.readLine();
                    commandComparison(line);
                }
                LOGGER.debug("Commandline Reader Closed");
            } catch (Exception e) {
                LOGGER.error(e);
            }
        }

        /**
         * Compares if a correct command has been entered
         * @param command A string of the command witch has been entered
         */
        private void commandComparison(String command){
            if(command.equalsIgnoreCase("exit")){
                read = false;
                measurementList.stop();
                Server.stop();
                LOGGER.info("Stopped Program");
                return;
            }
            LOGGER.info("Use help to get all commands");
        }

    }
}
