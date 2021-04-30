package de.throsenheim.vvss21.application.interfaces;

import java.io.File;
import java.util.List;

/**
 * For handling the File reading
 * Can be used to read a File
 */
public interface IFileReader {

    /**
     * Reads a file and puts the content into a String
     * @param file witch file it should read
     * @return String with the content of the file
     */
    String readFileToString(File file);

    /**
     * A method for reading a File
     * @param file File that should be read
     * @return Gives a List with the content of the File each Line ist a entry in the List
     */
    List<String> readFile(File file);

}
