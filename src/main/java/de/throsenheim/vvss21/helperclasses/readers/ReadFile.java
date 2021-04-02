package de.throsenheim.vvss21.helperclasses.readers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * A Class that scans a File
 * @author Alexander Asbeck
 * @version 1.0.0
 */
public class ReadFile {

    private static final Logger LOGGER = LogManager.getLogger(ReadFile.class);

    /**
     * Private constructor due to no need of a new instance of a class
     */
    private ReadFile(){
    }

    /**
     * A method for reading a File
     * @param file File that should be read
     * @return Gives a List with the content of the File each Line ist a entry in the List
     */
    public static List<String> readFile(File file){
        String debugMsg = "Try to read the file: " + file.getAbsolutePath();
        List<String> out = new LinkedList<>();
        LOGGER.debug(debugMsg);
        if (!file.exists()){
            debugMsg = "Could not found the file: " + file.getAbsolutePath();
            LOGGER.debug(debugMsg);
            return out;
        }
        try (Scanner fileScanner = new Scanner(file)){
            String line;
            while (fileScanner.hasNext()){
                line=fileScanner.nextLine();
                out.add(line);
            }
            debugMsg = "File reading was successful";
            LOGGER.debug(debugMsg);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            LOGGER.error(e);
        }
        return out;
    }
}
