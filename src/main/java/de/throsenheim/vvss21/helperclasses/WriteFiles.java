package de.throsenheim.vvss21.helperclasses;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * For writing new files witch are predefined
 * <p>e.g. config file
 */
public class WriteFiles {

    public WriteFiles() {
    }

    public synchronized void createConfig(File file){
        try {
            System.out.println("Creating new File at " + file.getAbsolutePath());
            FileWriter writer = new FileWriter(file);
            writer.write("This is the config file for alexanderasbeck\n"+
                    "Logaddress= \n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
