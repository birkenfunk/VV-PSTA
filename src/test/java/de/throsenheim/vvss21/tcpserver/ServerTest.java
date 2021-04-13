package de.throsenheim.vvss21.tcpserver;

import de.throsenheim.vvss21.Main;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.net.Socket;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ServerTest {

    @Test
    void run() throws Exception {
        Main.main(null);
        Socket socket = new Socket("localhost", 1024);
        InputStream fromClientStream = socket.getInputStream();
        Scanner fromClient = new Scanner(fromClientStream);
        String output = fromClient.nextLine();
        assertEquals("Connection successful",output);
    }

    @Test
    void stop() throws Exception {

    }

}