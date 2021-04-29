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
import java.util.EnumMap;
import java.util.Scanner;

/**
 * Class for the communication with a client
 * @version 1.0.0
 * @author Alexander Asbeck
 */
public class Connector implements Runnable{

    private Socket client;
    private static final Logger LOGGER = LogManager.getLogger(Connector.class);
    private final EnumMap<State, EnumMap<Symbol, State>> stateSymbolStateHashmap = new EnumMap<>(State.class);//Map for the automat
    private State state = State.WAIT_FOR_CLIENT;

    /**
     * Constructor for {@link Connector}
     * @param client a Socket to a client
     */
    public Connector(Socket client) {
        this.client = client;
        initEnumMap();
    }

    /**
     * Initialises the stateSymbolStateHashmap
     */
    private void initEnumMap(){
        EnumMap<Symbol,State> symbolStateHashMap = new EnumMap<>(Symbol.class);
        symbolStateHashMap.put(Symbol.SENSOR_HELLO, State.WAIT_FOR_ACKNOWLEDGE);
        stateSymbolStateHashmap.put(State.WAIT_FOR_CLIENT, symbolStateHashMap);
        symbolStateHashMap = new EnumMap<>(Symbol.class);
        symbolStateHashMap.put(Symbol.ACKNOWLEDGE, State.WAIT_FOR_MEASUREMENT);
        symbolStateHashMap.put(Symbol.TERMINATE, State.TERMINATED);
        symbolStateHashMap.put(Symbol.MEASUREMENT, State.TERMINATED);
        stateSymbolStateHashmap.put(State.WAIT_FOR_ACKNOWLEDGE,symbolStateHashMap);
        symbolStateHashMap = new EnumMap<>(Symbol.class);
        symbolStateHashMap.put(Symbol.MEASUREMENT,State.WAIT_FOR_MEASUREMENT);
        symbolStateHashMap.put(Symbol.TERMINATE, State.TERMINATED);
        stateSymbolStateHashmap.put(State.WAIT_FOR_MEASUREMENT, symbolStateHashMap);
        symbolStateHashMap = new EnumMap<>(Symbol.class);
        symbolStateHashMap.put(Symbol.SENSOR_HELLO, State.TERMINATED);
        symbolStateHashMap.put(Symbol.ACKNOWLEDGE, State.TERMINATED);
        symbolStateHashMap.put(Symbol.MEASUREMENT, State.TERMINATED);
        symbolStateHashMap.put(Symbol.TERMINATE, State.TERMINATED);
        stateSymbolStateHashmap.put(State.TERMINATED, symbolStateHashMap);
        symbolStateHashMap = new EnumMap<>(Symbol.class);
        symbolStateHashMap.put(Symbol.TERMINATE, State.TERMINATED);
        stateSymbolStateHashmap.put(State.ERROR, symbolStateHashMap);
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
        while (state != State.TERMINATED){
            if(!fromClient.hasNext()){
                break;
            }
            JsonNode node = null;
            String line = fromClient.nextLine();
            SendAndReceive receive = new SendAndReceive("",emptyNote);
            try {
                node = Json.parse(line);
                receive = Json.fromJson(node, SendAndReceive.class);
                nextState(receive.getType());
            }catch (IOException e){
                nextState(line);
            }


            if(state == State.WAIT_FOR_ACKNOWLEDGE){
                send.setType(Symbol.STATION_HELLO.toString());
                send.setPayload(emptyNote);
                node = Json.toJson(send);
                toClient.println(Json.stringify(node));
            }
            if(state == State.WAIT_FOR_MEASUREMENT){
                putToMeasurement(receive);
                send.setType(Symbol.STATION_READY.toString());
                send.setPayload(emptyNote);
                node = Json.toJson(send);
                toClient.println(Json.stringify(node));
            }
            if(state == State.ERROR){
                send.setType(State.ERROR.toString());
                send.setPayload(emptyNote);
                node = Json.toJson(send);
                toClient.println(Json.stringify(node));
            }
        }
        send.setType(Symbol.TERMINATE_STATION.toString());
        send.setPayload(emptyNote);
        JsonNode node = Json.toJson(send);
        toClient.println(Json.stringify(node));
        stop();
        Server.removeConnector(this);
    }

    /**
     * Goes to the next State
     * @param input the State of the input
     */
    private void nextState(String input){
        input = input.toUpperCase();
        Symbol inputSymbol;
        try {
            inputSymbol = Symbol.valueOf(input);
        }catch (IllegalArgumentException e){
            inputSymbol = Symbol.UNKNOWN;
        }
        state = stateSymbolStateHashmap.get(state).get(inputSymbol);
        if(state == null)
            state = State.ERROR;
    }

    /**
     * Adds a measurement Input to the {@link MeasurementList} queue
     * @param input {@link SendAndReceive} Object with the input parameters from the sensor
     * @throws IOException If something with the Json transformation goes wrong
     */
    private void putToMeasurement(SendAndReceive input) throws IOException {
        if(input == null){
            return;
        }
        if(!input.getType().equalsIgnoreCase(Symbol.MEASUREMENT.toString())){
            return;
        }
        JsonNode node = input.getPayload();
        MeasurementList measurementList = Main.getMeasurementList();
        String debugString = "Received: " + input;
        LOGGER.debug(debugString);
        try {
            Measurement mes = Json.fromJson(node, Measurement.class);
            measurementList.add(mes);
        }catch (IOException e){
            LOGGER.error(e);
            Measurement mes = new Measurement(node.get("value").intValue(),node.get("unit").asText(),node.get("type").asText(),node.get("timestamp").asText());
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


}