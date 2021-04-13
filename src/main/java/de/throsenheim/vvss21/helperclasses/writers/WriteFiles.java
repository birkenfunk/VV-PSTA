package de.throsenheim.vvss21.helperclasses.writers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * For writing new files witch are predefined
 * <p>e.g. config file
 * @author Alexander Asbeck
 * @version 1.1.0
 */
public class WriteFiles {

    private static final Logger LOGGER = LogManager.getLogger(WriteFiles.class);
    private WriteFiles() {
    }

    public static synchronized void createConfig(File file){
        String debugMsg = "Creating new File at " + file.getAbsolutePath();
        LOGGER.info(debugMsg);
        try (FileWriter writer = new FileWriter(file)){
            writer.write("This is the config file for alexanderasbeck\n"+
                    "Nothing yet ");
            debugMsg = "New log file has been created";
            LOGGER.debug(debugMsg);
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }

    public static synchronized boolean writeFile(File file, List<String> content, boolean overwrite){
        String debugMsg;
        try (FileWriter writer = new FileWriter(file,!overwrite)){
            for (String s : content) {
                writer.write(s + "\n");
            }
            debugMsg = "File " + file.getAbsolutePath() + " has been created";
            LOGGER.debug(debugMsg);
        } catch (IOException e) {
            LOGGER.error(e);
            return false;
        }
        return true;
    }
}
