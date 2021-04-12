package de.throsenheim.vvss21.tcpserver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.LinkedList;

public class Server implements Runnable{

    private static final int PORT = 1024;
    private static boolean run = true;
    private static Server server;
    private static final Logger LOGGER = LogManager.getLogger(Server.class);
    private static final LinkedList<Connector> connectors = new LinkedList<>();
    private static ServerSocket serverSocket;

    private Server() {
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
            setServer();
            while (run){
                connectors.add(new Connector(serverSocket.accept()));
                Thread connector = new Thread(connectors.getLast());
                connector.start();
                LOGGER.info("Connection");
            }
        } catch (IOException e) {
            LOGGER.error(e);
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                LOGGER.error(e);
            }
        }

    }

    private static void setServer() throws IOException {
        serverSocket = new ServerSocket(PORT);
    }

    public static void stop(){
        run = false;
        try {
            serverSocket.close();
        } catch (IOException e) {
            LOGGER.error(e);
        }
        while (!connectors.isEmpty()){
            connectors.remove().stop();
        }
    }

    public static Server getServerSocket() {
        if(serverSocket == null){
            server = new Server();
        }
        return server;
    }
}
