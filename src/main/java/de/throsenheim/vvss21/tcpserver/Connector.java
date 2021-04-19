package de.throsenheim.vvss21.tcpserver;

import com.fasterxml.jackson.databind.JsonNode;
import de.throsenheim.vvss21.Main;
import de.throsenheim.vvss21.helperclasses.json.Json;
import de.throsenheim.vvss21.measurement.Measurement;
import de.throsenheim.vvss21.measurement.MeasurementList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

class Connector implements Runnable{

    private Socket client;
    private static final Logger LOGGER = LogManager.getLogger(Connector.class);

    /**
     * Constructor for {@link Connector}
     * @param client a Socket to a client
     */
    public Connector(Socket client) {
        this.client = client;
    }


    private void connection(){
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
                JsonNode node = Json.parse(line);
                MeasurementList measurementList = Main.getMeasurementList();
                String debugString = "Received: " + line;
                LOGGER.debug(debugString);
                measurementList.add(Json.fromJson(node , Measurement.class));
                toClient.println("Added: " + node.toString());
            }
            Server.removeConnector(this);
        } catch (IOException e) {
            LOGGER.error(e);
        }
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
        connection();
    }

    public void stop(){
        try {
            client.close();
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }
}