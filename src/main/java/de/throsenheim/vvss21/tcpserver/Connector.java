package de.throsenheim.vvss21.tcpserver;

import de.throsenheim.vvss21.measurement.Measurement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

class Connector implements Runnable{

    private Socket client;
    private static final Logger LOGGER = LogManager.getLogger(Connector.class);

    public Connector(Socket client) {
        this.client = client;
    }

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
        try {
            InputStream fromClientStream = client.getInputStream();
            Scanner fromClient = new Scanner(fromClientStream);
            OutputStream toClientStream = client.getOutputStream();
            PrintStream toClient = new PrintStream(toClientStream);
            toClient.println("Connection successful");
            while (fromClient.hasNext()){
                String line = fromClient.nextLine();
                if(line.equalsIgnoreCase("exit")){
                    client.close();
                    break;
                }
                LOGGER.info(line);

            }
            Server.removeConnector(this);
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }

    public void stop(){
        try {
            client.close();
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }
}