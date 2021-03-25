package de.throsenheim.vvss21;

import de.throsenheim.vvss21.helperclasses.WriteFiles;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

/**
 * Main Class from where the program should be started
 * @author Alexander
 * @version 1.0.0
 */
public class Main {

    private File configFile = new File("./alexanderasbeck.config");
    private final Logger LOGGER = LogManager.getLogger(this.getClass());

    public static void main(String[] args) {
        new Main(args);
    }

    /**
     * Constructor for Main Class
     * Used to run the Program
     * @param args args given by main
     */
    public Main(String[] args) {
        LOGGER.debug("Program has Started");
        if(args.length>0) {
            if (args[0].equalsIgnoreCase("--config")) {
                if (args.length == 2) {
                    if (args[1].endsWith(".config")) {
                        configFile = new File(args[1]);
                        LOGGER.info("Changed config File during start to " + configFile.getAbsolutePath());
                        if (!configFile.exists()) {
                            new WriteFiles().createConfig(configFile);
                        }
                    } else {
                        LOGGER.debug("Tried to change config file during start, but no .config has been entered");
                        System.out.println("Please enter a .config file");
                    }
                } else {
                    LOGGER.debug("Tried to change config file during start, but no filepath has been entered");
                    System.out.println("Please use command with --config [filepath]");
                }

            }
        }
        LOGGER.debug("Config path is: " + configFile.getAbsolutePath());
        Thread consoleRead = new Thread(new ReadConsole());
        consoleRead.start();

    }

    /**
     * Class that reads from input from the Console
     * @author Alexander Asbeck
     * @version 1.0.0
     */
    class ReadConsole implements Runnable{

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
                while (true){
                    line = reader.readLine();
                    boolean notFound = true;
                    if (notFound && line.equalsIgnoreCase("exit")) {
                        notFound = false;
                        LOGGER.debug("Program has been stopped");
                        System.out.println("Program stopped");
                        reader.close();
                        break;
                    }
                    if(notFound && line.startsWith("config")){
                        notFound = false;
                        String[] splittedMessage = line.split(" ");
                        if(splittedMessage.length==2) {
                            if(splittedMessage[1].endsWith(".config")) {
                                configFile = new File(splittedMessage[1]);
                                LOGGER.info("Changed config File to " + configFile.getAbsolutePath());
                                if (!configFile.exists()) {
                                    new WriteFiles().createConfig(configFile);
                                }
                            }else {
                                LOGGER.debug("Tried to change config file but no .config has been entered");
                                System.out.println("Please enter a .config file");
                            }
                        }else {
                            LOGGER.debug("Tried to change config file but no filepath has been entered");
                            System.out.println("Please use command with config [filepath]");
                        }
                    }


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }
}
