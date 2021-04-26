package de.throsenheim.vvss21.tcpserver;

import de.throsenheim.vvss21.Main;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable{

    private static final int PORT = Main.getPort();
    private static boolean run = true;
    private static final Logger LOGGER = LogManager.getLogger(Server.class);
    private static final LinkedList<Connector> connectors = new LinkedList<>();
    private static ExecutorService executer = Executors.newFixedThreadPool(50);
    private static ServerSocket serverSocket;
    private static final Server SERVER = new Server();

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
            LOGGER.debug("Server listening");
            init();
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
                if(serverSocket != null) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                LOGGER.error(e);
            }
        }
        LOGGER.debug("Thread Server Stopped");
    }

    private static void init(){
        run = true;
        if(executer.isShutdown()){
            executer = Executors.newFixedThreadPool(50);
        }
    }

    public static boolean removeConnector(Connector connector){
        return connectors.remove(connector);
    }

    private static void setServerSocket() throws IOException {
        if(serverSocket == null || serverSocket.isClosed()){
            serverSocket = new ServerSocket(PORT);
        }
    }


    public static void stop(){
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

    public static Server getSERVER() {
        return SERVER;
    }
}
