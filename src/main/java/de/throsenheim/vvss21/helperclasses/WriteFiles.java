package de.throsenheim.vvss21.helperclasses;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * For writing new files witch are predefined
 * <p>e.g. config file
 * @author Alexander Asbeck
 * @version 1.1.0
 */
public class WriteFiles {

    private static final WriteFiles writeFiles = new WriteFiles();
    private final Logger LOGGER = LogManager.getLogger(this.getClass());
    private WriteFiles() {
    }

    public synchronized void createConfig(File file){
        try {
            System.out.println("Creating new File at " + file.getAbsolutePath());
            FileWriter writer = new FileWriter(file);
            writer.write("This is the config file for alexanderasbeck\n"+
                    "Logaddress= \n");
            writer.close();
            LOGGER.debug("New log file has been created");
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error(e);
        }
    }

    public static WriteFiles getWriteFiles() {
        return writeFiles;
    }
}
