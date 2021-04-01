package de.throsenheim.vvss21;

import de.throsenheim.vvss21.helperclasses.WriteFiles;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * Main Class from where the program should be started
 * @author Alexander
 * @version 1.2.0
 */
public class Main {

    private File configFile = new File("./alexanderasbeck.config");

    public static void main(String[] args) {
        new Main(args);
    }

    /**
     * Constructor for Main Class
     * Used to run the Program
     * @param args args given by main
     */
    public Main(String[] args) {
        if(args.length>0){
            inputComparison(args);
        }
        Thread consoleRead = new Thread(new ReadConsole());
        consoleRead.start();
    }

    /**
     * Used to see if the input parameters are correct
     * @param input the inputs that should be compared
     */
    private void inputComparison(String[] input){
        for (int i = 0; i < input.length; i++) {
            if(input[i].equalsIgnoreCase("--config")){
                if(input.length >= i+2 && input[i+1].endsWith(".conf")){
                    configFile = new File(input[i+1]);
                    if(!configFile.exists()){
                        WriteFiles.getWriteFiles().createConfig(configFile);
                    }
                }else {
                    System.out.println("Parameter --config has to be entered with a filepath\n" +
                            "Note that file has to end with .conf\n" +
                            "Now using default config file "+configFile.getAbsolutePath());
                }
            }
        }
    }


    /**
     * Class that reads from input from the Console
     * @author Alexander Asbeck
     * @version 1.0.0
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
                e.printStackTrace();
            }
        }

        /**
         * Compares if a correct command has been entered
         * @param command A string of the command witch has been entered
         */
        private void commandComparison(String command){
            if(command.equalsIgnoreCase("exit")){
                read = false;
                System.out.println("Stopped Program");
            }
            String[] splittedCommand = command.split(" ");
            if(splittedCommand[0].equalsIgnoreCase("config")){
                if(splittedCommand.length == 2 && splittedCommand[1].endsWith(".conf")){
                    configFile = new File(splittedCommand[1]);
                    if(!configFile.exists()){
                        WriteFiles.getWriteFiles().createConfig(configFile);
                    }
                }else {
                    System.out.println("Use Command like config [filepath]\n Note that you have to enter a .conf file");
                }
                return;
            }
            System.out.println("Use help to get all commands");
        }

    }
}
