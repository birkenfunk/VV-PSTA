package de.throsenheim.vvss21.tcpserver;

import java.io.IOException;
import java.net.ServerSocket;

public class Server implements Runnable{

    private static final int PORT = 1024;
    private static boolean run = true;

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
        try (ServerSocket server = new ServerSocket(PORT);){
            while (run){
                new Connector(server.accept());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void stop(){
        run = false;
    }
}
