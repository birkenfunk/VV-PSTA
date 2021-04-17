package de.throsenheim.vvss21;

import com.fasterxml.jackson.databind.JsonNode;
import de.throsenheim.vvss21.helperclasses.json.Json;
import de.throsenheim.vvss21.helperclasses.readers.ReadFile;
import de.throsenheim.vvss21.helperclasses.writers.WriteFiles;
import de.throsenheim.vvss21.measurement.MeasurementList;
import de.throsenheim.vvss21.tcpserver.Server;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

/**
 * Main Class from where the program should be started
 * @author Alexander
 * @version 1.3.1
 */
public class Main {

    private File configFile = new File("alexanderasbeck.conf");
    private static final Logger LOGGER = LogManager.getLogger(Main.class);
    private final ReadConsole readConsole;
    private static MeasurementList measurementList;
    private static String jsonLocation = "data.json";

    public static void main(String[] args) {
        new Main(args);
    }

    /**
     * Constructor for Main Class
     * Used to run the Program
     * @param args args given by main
     */
    public Main(String[] args) {
        this.readConsole = new ReadConsole();
        logStartup();
        if(args!= null && args.length>0){
            inputComparison(args);
        }
        if(!configFile.exists()){
            WriteFiles.createConfig(configFile);
        }
        readConf(ReadFile.readFile(configFile));
        getMeasurementList();
        Thread measurementSave  = new Thread(measurementList);
        measurementSave.start();
        Thread consoleRead = new Thread(readConsole);
        consoleRead.start();
        Thread serverThread = new Thread(Server.getSERVER());
        serverThread.start();
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
     * Reads out the Config out of a List
     * @param list List with the config
     */
    private static boolean readConf(List<String> list){
        if(list.isEmpty()){
            LOGGER.info("No config received");
            return false;
        }
        String confStart = "This is the config file for alexanderasbeck";
        if(list.remove(0).equals(confStart)){
            for (String s: list) {
                String[] splittedConf = s.split("=");
                if(s.startsWith("JSON_Location") && splittedConf.length>2){
                    jsonLocation = splittedConf[1];
                }
                //here can other config parameter be sorted out
            }
        }
        list.add(0,confStart);
        return true;
    }

    /**
     * Used to see if the input parameters are correct
     * @param input the inputs that should be compared
     */
    private void inputComparison(String[] input){
        for (int i = 0; i < input.length; i++) {
            if(input[i].equalsIgnoreCase("--conf")){
                if(input.length >= i+2 && input[i+1].endsWith(".conf")){
                    configFile = new File(input[i+1]);
                    if(!configFile.exists()){
                        WriteFiles.createConfig(configFile);
                    }
                }else {
                    String debugMsg = "Use --conf [filepath]\n" +
                            "Note that you have to enter a .conf file";
                    LOGGER.info(debugMsg);
                    debugMsg = "Now using default config "+ configFile.getPath();
                    LOGGER.debug(debugMsg);
                }

            }
        }
    }

    public File getConfigFile() {
        return configFile;
    }

    public static String getJsonLocation() {
        return jsonLocation;
    }

    public static MeasurementList getMeasurementList() {
        if(measurementList == null){
            String jsonString = ReadFile.readFileToString(new File(jsonLocation));
            try {
                JsonNode node = Json.parse(jsonString);
                measurementList = Json.fromJson(node, MeasurementList.class);
            } catch (IOException e) {
                LOGGER.error(e);
                measurementList = new MeasurementList(new LinkedList<>());
            }
        }
        return measurementList;
    }

    /**
     * Class that reads from input from the Console
     * @author Alexander Asbeck
     * @version 1.1.0
     */
    class ReadConsole implements Runnable{
        private boolean read = true;
        /**
         * When an object implementing interface <code>Runnable</code> is used
         * to create a thread, starting the thread causes the object's
         * <code>run</code> method to be called in that separately executing
         * thread.
         * <p>
         * The general contract of the method <code>run</code> is that it may
         * take any action whatsoever.
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
            String[] splittedCommand = command.split(" ");
            if(splittedCommand[0].equalsIgnoreCase("config")){
                if(splittedCommand.length == 2 && splittedCommand[1].endsWith(".conf")){
                    configFile = new File(splittedCommand[1]);
                    if(!configFile.exists()){
                        WriteFiles.createConfig(configFile);
                    }
                }else {
                    LOGGER.info("Use Command like config [filepath]\n Note that you have to enter a .conf file");
                }
                return;
            }
            LOGGER.info("Use help to get all commands");
        }

    }
}
