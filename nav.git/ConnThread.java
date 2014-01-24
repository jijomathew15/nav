package pathserver;

import com.mysql.jdbc.Statement;
import java.io.*;
import java.net.*;
import java.sql.*;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * 
 * @author Jacob D'Onofrio
 */
public class ConnThread implements Runnable{
    private Socket server;
    private String line,input;
    private DBManager db;
    private DataInputStream in;
    private PrintStream out;


    ConnThread(Socket Server){
        this.server = Server;
        db = new DBManager();
    }
    /*
     * This function is called then the thread is started
     * from main(). This function takes in the input from
     * the client and as long as it the quit function isn't
     * called, it sends the command to the process command
     * 
     */
    public void run() {
        input="";
        try{
            in = new DataInputStream(server.getInputStream());
            out = new PrintStream(server.getOutputStream());
            
            System.out.println(server.getInetAddress() + " Connected ... " + Thread.activeCount());

            while((line = in.readLine()) != null && !line.equals("quit")) {
                input=input + line;
                String args [] = line.split(" ");
                try{
                    Process(args);
                }
                catch(SQLException e){
                    System.out.println(e);
                }
            }

            System.out.println(server.getInetAddress() + " Disconnected ...");
            out.println("Bye");

            server.close();
        }
        catch (IOException e){
            System.out.println(e);
        }
    }

    /**
     * This function is called from the main thread loop. It checks the command
     * with all known commands which performs and returns the proper
     * information.
     * 
     * @param l
     * @throws SQLException
     */
    public void Process(String [] l) throws SQLException{
        System.out.print(server.getInetAddress() + ": ");
        for(int i = 0; i< l.length; i++){
            System.out.print(l[i] + " ");
        }
        System.out.println();

        if(l[0].equals("nearest-exit")){
            int start,destination;
            double x,y,z; 
            
            x = Float.parseFloat(l[1]);
            y = Float.parseFloat(l[2]);
            z = Float.parseFloat(l[3]);
            
            db.Connect();
            MapFactory t = new MapFactory(db);
            Map m = t.Build();
            destination = m.FindNearestExit(x, y, z);
            start = m.FindNearestNode(x, y, z);
            ArrayList<Node> tmp = m.getDirection(start, destination);
            for(int i = 0; i< tmp.size();i++){
                out.println(tmp.get(i).getId());
                System.out.println(tmp.get(i).getId());
            }
            out.println("DONE");
            db.Disconnect();
        }
        if(l[0].equals("nearest-womens-restroom")){
            int start,destination;
            double x,y,z;
            
            x = Float.parseFloat(l[1]);
            y = Float.parseFloat(l[2]);
            z = Float.parseFloat(l[3]);
            
            db.Connect();
            MapFactory t = new MapFactory(db);
            Map m = t.Build();
            destination = m.FindNearestWomensRestroom(x, y, z);
            start = m.FindNearestNode(x, y, z);
            ArrayList<Node> tmp = m.getDirection(start, destination);
            for(int i = 0; i< tmp.size();i++){
                out.println(tmp.get(i).getId());
                System.out.println(tmp.get(i).getId());
            }
            out.println("DONE");
            db.Disconnect();
        }
        if(l[0].equals("nearest-mens-restroom")){
            int start,destination;
            double x,y,z;
            
            x = Float.parseFloat(l[1]);
            y = Float.parseFloat(l[2]);
            z = Float.parseFloat(l[3]);
            
            db.Connect();
            MapFactory t = new MapFactory(db);
            Map m = t.Build();
            destination = m.FindNearestMensRestroom(x, y, z);
            start = m.FindNearestNode(x, y, z);
            ArrayList<Node> tmp = m.getDirection(start, destination);
            for(int i = 0; i< tmp.size();i++){
                out.println(tmp.get(i).getId());
                System.out.println(tmp.get(i).getId());
            }
            out.println("DONE");
            db.Disconnect();
        }
        if(l[0].equals("directions")){
            int start, destination;
            double x,y,z;
            
            x = Float.parseFloat(l[1]);
            y = Float.parseFloat(l[2]);
            z = Float.parseFloat(l[3]);
            destination = Integer.parseInt(l[4]);
            
            db.Connect();
            MapFactory t = new MapFactory(db);
            Map m = t.Build();
            start = m.FindNearestNode(x, y, z);
            ArrayList<Node> tmp = m.getDirection(start, destination);
            for(int i = 0; i< tmp.size();i++){
                out.println(tmp.get(i).getId());
                System.out.println(tmp.get(i).getId());
            }
            out.println("DONE");
            db.Disconnect();
        }
        if(l[0].equals("get-router-coord")){
            if(l[1] != null){
                db.Connect();
                RouterLookup(l[1]);
                db.Disconnect();
            }
            else{
                out.println("INVALID COMMAND");
            }
        }
        if(l[0].equals("get-routers")){
            db.Connect();
            AllRouters();
            db.Disconnect();
        }
        if(l[0].equals("get-room-list")){
            db.Connect();
            getRoomList();
            db.Disconnect();
        }        
        if(l[0].equals("db_test")){
            db.Connect();
            out.println(db.isConnected());
            db.Disconnect();
        }
    }
    /*
     * This function takes in a mac address, looks up its information
     * in the SQL database, and returns the information
     */
    private void RouterLookup(String mac) throws SQLException{
        Statement s = (Statement) db.getConnection().createStatement();
        s.executeQuery("SELECT * FROM routers WHERE mac=" + mac);
        ResultSet rs = s.getResultSet();
        if(rs.next()){
            double x = rs.getDouble("x");
            double y = rs.getDouble("y");
            double z = rs.getDouble("z");
            out.println(mac + " " + x + " " + y + " " + z);
        }
        else{
            out.println("ERROR: " + mac);
        }        
    }
    /*
     * This function retrieves all rooms from the database
     * and sends them to the connected client
     */
    private void getRoomList() throws SQLException{
        Statement s = (Statement) db.getConnection().createStatement();
        s.executeQuery("SELECT * FROM rooms");
        ResultSet rs = s.getResultSet();
        
        while(rs.next()){
            int rn = rs.getInt("number");
            String type = rs.getString("type");
            float x = rs.getFloat("x");
            float y = rs.getFloat("y");
            float z = rs.getFloat("z");
            
            
            
            out.println(rn + " " + type + " " + x + " " + y + " " + z);
        }
        out.println("DONE");
    }
    /*
     * This function retrieves all information about
     * the access points and sends them to the connected client
     */
    private void AllRouters() throws SQLException{
        Statement s = (Statement) db.getConnection().createStatement();
        s.executeQuery("SELECT * FROM routers");
        ResultSet rs = s.getResultSet();
        String message = "";
        while(rs.next()){
            String mac;
            float x,y,z;
            
            mac = rs.getString("mac");
            x = rs.getFloat("x");
            y = rs.getFloat("y");
            z = rs.getFloat("z");
            
            message += mac + " " + x + " " + y + " " + z + " ";
        }
        out.println(message);
    }    
}
