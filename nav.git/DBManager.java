package pathserver;

import java.sql.*;

/**
 * 
 * @author Jacob D'Onofrio
 */
public class DBManager {

    //This is the URL that we used. mysql is the driver and ebfloor1 is the
    //database schema name
    private final String url = "jdbc:mysql://10.10.15.1:3306/ebfloor1";
    private Connection con;
    private boolean Connected;

    DBManager(){
        Connected = false;
        try{
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch(Exception e){
            System.out.println(e);
        }
    }

    /**
     * This function establishes a connection to the database using the 
     * constant private values above. The user and password are hardcoded
     * into this function. If the connection is made, then the boolean
     * value is set to true.
     */
    public void Connect(){
        //
        try{

            con = DriverManager.getConnection(url, "root", "password");
            Connected = true;
        }
        catch(SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
    }

    /**
     * This function just calls the close function from the jdbc member
     * and sets the boolean flag to false.
     */
    public void Disconnect(){
        try{
            con.close();
            Connected = false;
        }
        catch(SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
    }

    /**
     * This function returns the connection status
     * @return
     */
    public boolean isConnected(){
        return Connected;
    }
    
    /**
     * This function returns the jdbc member so that 
     * SQL statements can be built and executed
     * @return
     */
    public Connection getConnection(){
        return con;
    }
}
