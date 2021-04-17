package de.throsenheim.vvss21.tcpserver;

import de.throsenheim.vvss21.Main;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ServerTest {

    private  static Thread serverThread;

    @BeforeAll
    static void beforeAll() {
        Server.stop();
        serverThread = new Thread(Server.getSERVER());
        serverThread.start();
    }

    @Test
    void run() throws Exception {
        Socket socket = new Socket("localhost", 1024);
        InputStream fromClientStream = socket.getInputStream();
        Scanner fromClient = new Scanner(fromClientStream);
        String output = fromClient.nextLine();
        assertEquals("Connection successful",output);
    }

    @Test
    void stop() throws  Exception{
        Socket socket = new Socket("localhost", 1024);
        InputStream fromClientStream = socket.getInputStream();
        Scanner fromClient = new Scanner(fromClientStream);
        String output = fromClient.nextLine();
        assertEquals("Connection successful",output);
        Server.stop();
        assertThrows(ConnectException.class,()->new Socket("localhost", 1024));
    }

}