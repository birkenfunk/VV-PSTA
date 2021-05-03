package de.throsenheim.vvss21.persistance;

import de.throsenheim.vvss21.application.interfaces.ICommandlineReader;
import de.throsenheim.vvss21.domain.models.MeasurementList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Class that reads from input from the Console
 * @author Alexander Asbeck
 * @version 1.1.0
 */
public class CommandlineReader implements ICommandlineReader, Runnable{

    private static final Logger LOGGER = LogManager.getLogger(CommandlineReader.class);
    private MeasurementList measurementList;
    private boolean read = true;
    private BufferedReader reader;
    public CommandlineReader(MeasurementList measurementList) {
        this.measurementList = measurementList;
    }

    /**
     * Starts a Commandline reader
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        read();
    }

    /**
     * Method for reading from the commandline
     */
    private void read(){
        String line;

        reader = new BufferedReader(new InputStreamReader(System.in));
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
            Server.getSERVER().stop();
            LOGGER.info("Stopped Program");
            return;
        }
        LOGGER.info("Use help to get all commands");
    }

    /**
     * To stop the reading from the commandline
     */
    @Override
    public void stop() {
        read = false;
        if(reader!=null) {
            try {
                reader.close();
            } catch (IOException e) {
                LOGGER.error(e);
            }
        }
    }
}
