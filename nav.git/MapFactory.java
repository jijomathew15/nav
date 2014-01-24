/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pathserver;

import java.sql.*;
import java.sql.SQLException;

/**
 *
 * @author Jacob D'Onofrio
 */
public class MapFactory {

    DBManager db;
    
    /**
     * This constructor creates a factory class with a built in
     * database management object. THe DBManager is a copy of the
     * main DBManager object.
     * @param d
     * @throws SQLException
     */
    public MapFactory(DBManager d) throws SQLException{
        db = d;
        //Statement s = db.getConnection().createStatement();
        //s.executeUpdate("USE mapping_test");
        //s.close();
    }
    
    /**
     * This function connects to the database and fetches all
     * nodes and based on the types, are placed in certain lists
     * for the map to use.
     * @return
     * @throws SQLException
     */
    public Map Build() throws SQLException{
        Map m = new Map();
        
        //Get a list of all the nodes and add them to the map
        Statement s = db.getConnection().createStatement();
        
        s.executeQuery("SELECT * FROM rooms");
        ResultSet rs = s.getResultSet();
        while(rs.next()){
            //Create a new node for each entry in the table and assign location
            int i = rs.getInt("number");
            double x = rs.getDouble("x");
            double y = rs.getDouble("y");
            double z = rs.getDouble("z");
            String type = rs.getString("type");
            Node tmp = new Node(i,x,y,z);
            tmp.label = type;
            
            //Parse returned neightbors and add them in the node
            String n = rs.getString("neighbors");
            String [] na = n.split(",");
            for(int a = 0; a < na.length; a++){
                int b = Integer.parseInt(na[a]);
                tmp.addNeighbor(b);
            }
            m.addNode(tmp);
            //Add to correct grouping if necesary
            if(type.equals("MENS_RESTROOM")){
                m.AddMensRestroom(tmp);
            }
            else if(type.equals("WOMENS_RESTROOM")){
                m.AddWomensRestroom(tmp);
            }
            else if(type.equals("EXIT")){
                m.AddExit(tmp);
            }
        }
        rs.close();
        s.close();
        return m;
    }
}
