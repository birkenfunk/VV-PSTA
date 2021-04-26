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

    /**
     * Creates a file at a specific location
     * @param file location of the file
     * @param content content of the file each entry of the list is a new line
     * @param overwrite If the file should be overwritten or the content should be just added
     * @return true if file is created false if not
     */
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
