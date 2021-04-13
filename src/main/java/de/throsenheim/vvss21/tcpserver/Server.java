package de.throsenheim.vvss21.tcpserver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable{

    private static final int PORT = 1024;
    private static boolean run = true;
    private static Server server;
    private static final Logger LOGGER = LogManager.getLogger(Server.class);
    private static final LinkedList<Connector> connectors = new LinkedList<>();
    private final ExecutorService executer = Executors.newFixedThreadPool(50);
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
            setServerSocket();
            while (run){
                Socket socket = serverSocket.accept();
                connectors.add(new Connector(socket));
                executer.execute(connectors.getLast());
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

    public boolean removeConnector(Connector connector){
        return connectors.remove(connector);
    }

    private static void setServerSocket() throws IOException {
        if(serverSocket == null || serverSocket.isClosed()){
            serverSocket = new ServerSocket(PORT);
        }
    }


    public static void stop(){
        if(serverSocket == null || serverSocket.isClosed()){
            return;
        }
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

    public static Server getServer() {
        if(server == null){
            server = new Server();
        }
        return server;
    }
}
