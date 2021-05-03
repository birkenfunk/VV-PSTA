package de.throsenheim.vvss21;

import com.fasterxml.jackson.databind.JsonNode;
import de.throsenheim.vvss21.application.interfaces.ICommandlineReader;
import de.throsenheim.vvss21.common.ConfigData;
import de.throsenheim.vvss21.common.Json;
import de.throsenheim.vvss21.common.ReadFile;
import de.throsenheim.vvss21.domain.models.MeasurementList;
import de.throsenheim.vvss21.persistance.CommandlineReader;
import de.throsenheim.vvss21.persistance.Server;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.LinkedList;

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
    private final ICommandlineReader readConsole;

    /**
     * Constructor for Main Class
     * Used to run the Program
     * @param args args given by main
     */
    public Main(String[] args) {
        this.readConsole = new CommandlineReader(getMeasurementList());
        logStartup();
        getMeasurementList();
        Thread measurementSave  = new Thread(measurementList);
        measurementSave.start();
        Thread consoleRead = new Thread(readConsole);
        consoleRead.start();
        Thread serverThread = new Thread(Server.getSERVER());
        serverThread.start();
    }

    /**
     * Returns the {@link MeasurementList} that is stored in the main class
     * <p>Creates a new {@link MeasurementList} if measurementList in main is null
     * @return MeasurementList that is stored in the main class
     */
    public static MeasurementList getMeasurementList() {
        if(measurementList == null){
            File jsonFile = new File(ConfigData.getJsonLocation());
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

}
