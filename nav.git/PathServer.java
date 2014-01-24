package pathserver;

import java.io.*;
import java.net.*;

/**
 * 
 * @author Jacob D'Onofrio
 */
public class PathServer{

    /**
     * Total number of threads the server can create
     */
    public static final int maxConn = 20;

    /**
     * Main thread. Does not use any arguments
     * @param args
     */
    public static void main(String[] args) {

        try{
            ServerSocket Listener = new ServerSocket(1024);
            Socket server;
            int i = 0;

            while(i < maxConn){
                ConnThread connection;
                server = Listener.accept();
                connection = new ConnThread(server);
                Thread t = new Thread(connection);
                t.start();
                i = Thread.activeCount();
            }
        }
        catch (IOException e){
            System.out.println(e);
        }
    }
}
