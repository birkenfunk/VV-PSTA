package de.throsenheim.vvss21.tcpserver;

import com.fasterxml.jackson.databind.JsonNode;
import de.throsenheim.vvss21.Main;
import de.throsenheim.vvss21.helperclasses.json.Json;
import de.throsenheim.vvss21.measurement.Measurement;
import de.throsenheim.vvss21.measurement.MeasurementList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.net.ConnectException;
import java.net.Socket;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class ServerTest {

    private static final Logger LOGGER = LogManager.getLogger(ServerTest.class);
    private  static Thread serverThread;

    @BeforeEach
    void setUp() {
        Server.stop();
        serverThread = new Thread(Server.getSERVER());
        serverThread.start();
    }

    @AfterEach
    void tearDown() {
        Server.stop();
        Main.getMeasurementList().stop();
    }

    /**
     * Checks if the connection works without any problems
     */
    @Test
    void giveJson() throws Exception {
        LOGGER.debug("Executing Task giveJson\n\n\n");
        Thread.sleep(1000);
        Socket socket = new Socket("localhost", 1024);
        InputStream fromClientStream = socket.getInputStream();
        Scanner fromClient = new Scanner(fromClientStream);
        String output = fromClient.nextLine();
        assertEquals("Connection successful",output);//Checks if Connection was successful

        OutputStream toClientStream = socket.getOutputStream();
        PrintStream toClient = new PrintStream(toClientStream); //Creates a printStream to send data to the connector
        MeasurementList measurementList = Main.getMeasurementList();
        Thread measurementListThread = new Thread(measurementList);//Creates a Thread for the Blocking Queue
        measurementListThread.start();

        String dataTestStream = "{\"unit\":\"CELSIUS\",\"type\":\"TEMPERATURE\",\"value\":10,\"timestamp\":\""+ Timestamp.valueOf(LocalDateTime.now()) +"\"}";//A json String to send to the Connector
        toClient.println(dataTestStream);

        JsonNode node = Json.parse(dataTestStream);
        assertEquals("Added: "+ node.toString(),fromClient.nextLine());//Checks if the return of the Connector is correct

        List<Measurement> measurements = measurementList.getMeasurements();
        Measurement dataTestMeasurement = Json.fromJson(node, Measurement.class);
        boolean iscontained = false;
        Thread.sleep(2000);
        for (Measurement measurement: measurements) {//Checks if the object is in the MeasurementList
            if(measurement.equals(dataTestMeasurement)){
                iscontained = true;
                break;
            }
        }
        assertTrue(iscontained);
    }

    /**
     * Checks if the stop method of the Server works
     */
    @Test
    void stop() throws  Exception{
        LOGGER.debug("Executing Task stopServer\n\n\n");
        Socket socket = new Socket("localhost", 1024);
        InputStream fromClientStream = socket.getInputStream();
        Scanner fromClient = new Scanner(fromClientStream);
        String output = fromClient.nextLine();
        assertEquals("Connection successful",output);

        Socket socket1 = new Socket("localhost", 1024);
        OutputStream toClientStream = socket1.getOutputStream();
        PrintStream toClient = new PrintStream(toClientStream);
        toClient.println("exit");
        Field connectors = Server.getSERVER().getClass().getDeclaredField("connectors");
        connectors.setAccessible(true);
        LinkedList<Connector> connectorLinkedList = (LinkedList<Connector>) connectors.get(Server.getSERVER());
        Thread.sleep(100);
        assertEquals(1 , connectorLinkedList.size());

        Server.stop();
        assertEquals(0 , connectorLinkedList.size());
        Thread.sleep(100);
        assertThrows(ConnectException.class,()->new Socket("localhost", 1024));
    }

}