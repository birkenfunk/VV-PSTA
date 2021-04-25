package de.throsenheim.vvss21.tcpserver;

import com.fasterxml.jackson.databind.JsonNode;
import de.throsenheim.vvss21.Main;
import de.throsenheim.vvss21.helperclasses.json.Json;
import de.throsenheim.vvss21.measurement.Measurement;
import de.throsenheim.vvss21.measurement.MeasurementList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;

import java.io.IOException;
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
        LOGGER.debug("Executing Task giveJson");
        Thread mesThread = new Thread(Main.getMeasurementList());
        mesThread.start();
        Thread.sleep(1000);
        Socket socket = new Socket("localhost", 1024);
        InputStream fromServerStream = socket.getInputStream();
        Scanner fromServer = new Scanner(fromServerStream);
        OutputStream toServerStream = socket.getOutputStream();
        PrintStream toServer = new PrintStream(toServerStream);

        toServer.println("{\"type\":\"Sensor_Hello\",\"payload\":{}}");
        assertEquals("{\"type\":\"STATION_HELLO\",\"payload\":{}}", fromServer.nextLine());
        toServer.println("{\"type\":\"Acknowledge\",\"payload\":{}}");
        assertEquals("{\"type\":\"STATION_READY\",\"payload\":{}}", fromServer.nextLine());
        Measurement measurement = new Measurement(10, "CELSIUS", "TEMPERATURE",  Timestamp.valueOf(LocalDateTime.now()).toString());
        String data = "{\"type\":\"Measurement\",\"payload\":"+Json.stringify(Json.toJson(measurement))+"}";
        toServer.println(data);
        assertEquals("{\"type\":\"STATION_READY\",\"payload\":{}}", fromServer.nextLine());
        measurement = new Measurement(10, "Random Stuff", "Random Stuff",  Timestamp.valueOf(LocalDateTime.now()).toString());
        data = "{\"type\":\"Measurement\",\"payload\":"+Json.stringify(Json.toJson(measurement))+"}";
        toServer.println(data);
        Thread.sleep(1000);
        assertTrue(Main.getMeasurementList().getMeasurements().contains(measurement));
        assertTrue(Main.getMeasurementList().getMeasurements().contains(measurement));
    }

    /**
     * Checks if the stop method of the Server works
     */
    @Test
    void stop() throws  Exception{
        LOGGER.debug("Executing Task stopServer\n\n\n");
        Thread.sleep(1000);
        Socket socket = new Socket("localhost", 1024);
        InputStream fromServerStream = socket.getInputStream();
        Scanner fromServer = new Scanner(fromServerStream);
        OutputStream toServerStream = socket.getOutputStream();
        PrintStream toServer = new PrintStream(toServerStream);
        toServer.println("{\"type\":\"Sensor_Hello\",\"payload\":{}}");
        assertEquals("{\"type\":\"STATION_HELLO\",\"payload\":{}}", fromServer.nextLine());
        Server.stop();
        assertFalse(fromServer.hasNext());
    }

    @Test
    void TestAutomat() throws IOException, InterruptedException {
        LOGGER.debug("Executing Task TestAutomat\n\n\n");
        Thread.sleep(1000);
        Socket socket = new Socket("localhost", 1024);
        InputStream fromServerStream = socket.getInputStream();
        Scanner fromServer = new Scanner(fromServerStream);
        OutputStream toServerStream = socket.getOutputStream();
        PrintStream toServer = new PrintStream(toServerStream);
        toServer.println("{\"type\":\"Sensor_Hello\",\"payload\":{}}");
        assertEquals("{\"type\":\"STATION_HELLO\",\"payload\":{}}", fromServer.nextLine());
        toServer.println("{\"type\":\"Acknowledge\",\"payload\":{}}");
        assertEquals("{\"type\":\"STATION_READY\",\"payload\":{}}", fromServer.nextLine());
        toServer.println("{\"type\":\"Sensor_Hello\",\"payload\":{}}");
        assertEquals("{\"type\":\"ERROR\",\"payload\":{}}", fromServer.nextLine());
        toServer.println("{\"type\":\"Terminate\",\"payload\":{}}");
        assertEquals("{\"type\":\"TERMINATE_STATION\",\"payload\":{}}", fromServer.nextLine());
        assertFalse(fromServer.hasNext());

        socket = new Socket("localhost", 1024);
        fromServerStream = socket.getInputStream();
        fromServer = new Scanner(fromServerStream);
        toServerStream = socket.getOutputStream();
        toServer = new PrintStream(toServerStream);
        toServer.println("{\"type\":\"Acknowledge\",\"payload\":{}}");
        assertEquals("{\"type\":\"ERROR\",\"payload\":{}}", fromServer.nextLine());
        toServer.println("{\"type\":\"Terminate\",\"payload\":{}}");
        assertEquals("{\"type\":\"TERMINATE_STATION\",\"payload\":{}}", fromServer.nextLine());
        assertFalse(fromServer.hasNext());
    }
}