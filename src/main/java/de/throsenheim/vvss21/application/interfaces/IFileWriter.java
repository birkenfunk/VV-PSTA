package de.throsenheim.vvss21.application.interfaces;

import java.io.File;
import java.util.List;

/**
 * Interface to write Files
 * Can be used to create Files or add content to existing files
 */
public interface IFileWriter {

    /**
     * Creates a file at a specific location
     * @param file location of the file
     * @param content content of the file each entry of the list is a new line
     * @param overwrite If the file should be overwritten or the content should be just added
     * @return true if file is created false if not
     */
    boolean writeFile(File file, List<String> content, boolean overwrite);

    /**
     * Creates a file at a specific location
     * @param file location of the file
     * @param content Writes the Sting into the File
     * @param overwrite If the file should be overwritten or the content should be just added
     * @return true if file is created false if not
     */
    boolean writeFile(File file, String content, boolean overwrite);

}
