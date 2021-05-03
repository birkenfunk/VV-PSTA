package de.throsenheim.vvss21.persistance;

import de.throsenheim.vvss21.Main;
import de.throsenheim.vvss21.application.interfaces.IServer;
import de.throsenheim.vvss21.common.ConfigData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A Class for handling the Connections to the Client
 * <p>If a Client wants to connect it will open a new Thread with the Connection</p>
 * @version 1.0.0
 * @author Alexander Asbeck
 */
public class Server implements IServer, Runnable {

    private static final int PORT = ConfigData.getPort();
    private static boolean run = true;
    private static final Logger LOGGER = LogManager.getLogger(Server.class);
    private static final LinkedList<Connector> connectors = new LinkedList<>();
    private static ExecutorService executer = Executors.newFixedThreadPool(50);
    private static ServerSocket serverSocket;
    private static final Server SERVER = new Server();

    /**
     * Is private due to the fact that only one Server should be active at the same time
     */
    private Server() {
    }

    /**
     * Initializes the variables for the Server
     */
    private static void init() throws IOException {
        run = true;
        if(executer.isShutdown()){
            executer = Executors.newFixedThreadPool(50);
        }
        if(serverSocket == null || serverSocket.isClosed()){
            serverSocket = new ServerSocket(PORT);
        }
    }

    /**
     * Method used by {@link Server#run()} to start the Server
     */
    private void startServer() {
        LOGGER.debug("Server listening");
        try {
            init();
            while (run){
                Socket socket = serverSocket.accept();
                connectors.add(new Connector(socket));
                executer.execute(connectors.getLast());
                LOGGER.info("Connection");
            }
        } catch (IOException e) {
            LOGGER.error(e);
        } finally {
            stop();
        }
        LOGGER.debug("Thread Server Stopped");
    }

    /**
     * Method to remove a Connector from the Connector list in case the connection is closed
     * @param connector Connector that should be removed
     * @return True if operation was a success False if operation failed
     */
    public static boolean removeConnector(Connector connector){
        return connectors.remove(connector);
    }

    /**
     * Method to stop the Server and the connections
     */
    public void stop(){
        if(serverSocket != null){
            try {
                serverSocket.close();
                serverSocket = null;
                LOGGER.debug("Server Closed");
            } catch (IOException e) {
                LOGGER.error(e);
            }
        }
        run = false;
        while (!connectors.isEmpty()){
            connectors.remove().stop();
        }
        executer.shutdown();
    }

    /**
     * Method to get the Server
     * @return The Server object
     */
    public static Server getSERVER() {
        return SERVER;
    }

    /**
     * Method to start the Server Thread
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        startServer();
    }
}
