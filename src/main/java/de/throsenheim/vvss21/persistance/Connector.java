package de.throsenheim.vvss21.persistance;

import com.fasterxml.jackson.databind.JsonNode;
import de.throsenheim.vvss21.Main;
import de.throsenheim.vvss21.application.StateMachine;
import de.throsenheim.vvss21.application.interfaces.IClientConnection;
import de.throsenheim.vvss21.application.interfaces.IStateMachine;
import de.throsenheim.vvss21.domain.enums.EState;
import de.throsenheim.vvss21.domain.enums.ESymbol;
import de.throsenheim.vvss21.domain.enums.EType;
import de.throsenheim.vvss21.domain.enums.EUnit;
import de.throsenheim.vvss21.domain.interfaces.IMeasurementList;
import de.throsenheim.vvss21.domain.models.Measurement;
import de.throsenheim.vvss21.common.Json;
import de.throsenheim.vvss21.domain.models.SendAndReceive;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Scanner;

/**
 * Class for the communication with a client
 * @version 1.0.0
 * @author Alexander Asbeck
 */
public class Connector implements IClientConnection, Runnable {

    private Socket client;
    private static final Logger LOGGER = LogManager.getLogger(Connector.class);
    private EState state = EState.WAIT_FOR_CLIENT;

    /**
     * Constructor for {@link Connector}
     * @param client a Socket to a client
     */
    public Connector(Socket client) {
        this.client = client;
    }



    /**
     * Handels the Input and output of a connection with a client
     * @throws IOException If something went wrong with the Server Client Connection
     */
    private void connection() throws IOException {
        InputStream fromClientStream = client.getInputStream();
        Scanner fromClient = new Scanner(fromClientStream);
        OutputStream toClientStream = client.getOutputStream();
        PrintStream toClient = new PrintStream(toClientStream);
        JsonNode emptyNote = Json.parse("{}");
        SendAndReceive send = new SendAndReceive("", emptyNote);
        IStateMachine stateMachine = StateMachine.getStateMachine();
        while (state != EState.TERMINATED){
            if(!fromClient.hasNext()){
                break;
            }
            JsonNode node = null;
            String line = fromClient.nextLine();
            SendAndReceive receive = new SendAndReceive("",emptyNote);
            try {
                node = Json.parse(line);
                receive = Json.fromJson(node, SendAndReceive.class);
                state = stateMachine.nextState(receive.getType(), state);
            }catch (IOException e){
                state = stateMachine.nextState(line,state);
            }


            if(state == EState.WAIT_FOR_ACKNOWLEDGE){
                send.setType(ESymbol.STATION_HELLO.toString());
                send.setPayload(emptyNote);
                node = Json.toJson(send);
                toClient.println(Json.stringify(node));
            }
            if(state == EState.WAIT_FOR_MEASUREMENT){
                putToMeasurement(receive);
                send.setType(ESymbol.STATION_READY.toString());
                send.setPayload(emptyNote);
                node = Json.toJson(send);
                toClient.println(Json.stringify(node));
            }
            if(state == EState.ERROR){
                send.setType(EState.ERROR.toString());
                send.setPayload(emptyNote);
                node = Json.toJson(send);
                toClient.println(Json.stringify(node));
            }
        }
        send.setType(ESymbol.TERMINATE_STATION.toString());
        send.setPayload(emptyNote);
        JsonNode node = Json.toJson(send);
        toClient.println(Json.stringify(node));
        stop();
        Server.removeConnector(this);
    }

    /**
     * Adds a measurement Input to the {@link Measurement} queue
     * @param input {@link SendAndReceive} Object with the input parameters from the sensor
     * @throws IOException If something with the Json transformation goes wrong
     */
    private void putToMeasurement(SendAndReceive input) throws IOException {
        if(input == null){
            return;
        }
        if(!input.getType().equalsIgnoreCase(ESymbol.MEASUREMENT.toString())){
            return;
        }
        JsonNode node = input.getPayload();
        IMeasurementList measurementList = Main.getMeasurementList();
        String debugString = "Received: " + input;
        LOGGER.debug(debugString);
        try {
            Measurement mes = Json.fromJson(node, Measurement.class);
            measurementList.add(mes);
        }catch (IOException e){
            LOGGER.error(e);
            Measurement mes = new Measurement(node.get("value").intValue(), EUnit.NONE, EType.NONE, LocalDateTime.now());
            measurementList.add(mes);
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
        try {
            connection();
        } catch (IOException e) {
            LOGGER.error(e);
            stop();
        }
        LOGGER.debug("Thread Connector Stopped");
    }

    /**
     * To Stop the connection to a client
     */
    public void stop(){
        try {
            client.close();
            LOGGER.debug("Client Connection Closed");
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }

    /**
     * To check if the connection is still running
     *
     * @return True if connection is still active False if connection isn't active any more
     */
    @Override
    public boolean isRunning() {
        return state!=EState.TERMINATED;
    }


}