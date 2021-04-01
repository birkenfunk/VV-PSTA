package de.throsenheim.vvss21;

import de.throsenheim.vvss21.helperclasses.WriteFiles;

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
            if(args[0].equalsIgnoreCase("--config")){
                if(args.length==2){
                    if (args[1].endsWith(".config")){
                        configFile = new File(args[1]);
                        if(!configFile.exists()){
                            WriteFiles.getWriteFiles().createConfig(configFile);
                        }
                    }
                }
            }
        }
        Thread consoleRead = new Thread(new ReadConsole());
        consoleRead.start();

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
