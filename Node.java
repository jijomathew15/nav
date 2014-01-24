package pathserver;

import java.util.ArrayList;

/**
 * The Node class represents notable locations on a map
 * and is used by the Map class
 * @author Jacob D'Onofrio
 */
public class Node {
    private int id, parent;
    private ArrayList<Integer> neighbors;
    /**
     * X Location
     */
    /**
     * Y Location
     */
    /**
     * Z Location
     */
    public double X,Y,Z;
    private double F, G, H;
    /**
     * Type of Node/Room
     */
    public String label;

    Node(){
        id = 0;
        parent = 0;
        label = "Node";
        
        F = 0;
        G = 0;
        H = 0;
        
        X = 0;
        Y = 0;
        Z = 0;
        
        neighbors = new ArrayList<Integer>() {};
    }
    Node(int i, double x, double y, double z){
        X = x;
        Y = y;
        Z = z;
        id = i;
        parent = 0;
        
        F = 0;
        G = 0;
        H = 0;
        
        neighbors = new ArrayList<Integer>();
    }
    /**
     * Copy Constructor
     * @param x
     */
    public Node(Node x){
        X = x.X;
        Y = x.Y;
        Z = x.Z;
        id = x.getId();
        parent = x.getParent();
        G = x.G;
        H = x.H;
        F = x.F;
        neighbors = x.getNeighbors();
    }

    /**
     * Function to print all of the current nodes
     * information to stdout. Used for debugging
     * purposes
     */
    public void Print(){
        System.out.println("X:" + X);
        System.out.println("Y:" + Y);
        System.out.println("Z:" + Z);
        System.out.println("ID:" + id);
        System.out.println("N Size:" + neighbors.size());
    }
    
    /**
     * Getter function. Returns the list of neighbors
     * @return
     */
    public ArrayList<Integer> getNeighbors(){
        return neighbors;
    }
    /**
     * Get function. Returns a specific node. I is location in list
     * @param i
     * @return
     */
    public int getNeighborID(int i){
        return neighbors.get(i);
    }
    /**
     * Used to populate a nodes list of neighbors
     * from the database
     * @param x
     */
    public void addNeighbor(int x){
        neighbors.add(x);
    }

    /**
     * Getter function.
     * @return
     */
    public int getParent(){
        return parent;
    }
    
    /**
     * Getter function
     * @return
     */
    public int getId(){
        return id;
    }

    /**
     * Setter function. Used before the member variables
     * were made public.
     * @param x
     * @param y
     * @param z
     */
    public void setCoordinates(double x, double y, double z){
        X = x;
        Y = y;
        Z = z;
    }
    /**
     * Getter function.
     * @return
     */
    public double getX(){return this.X;}
    /**
     * Getter function.
     * @return
     */
    public double getY(){return this.Y;}
    /**
     * Getter function.
     * @return
     */
    public double getZ(){return this.Z;}
    /**
     * Getter function.
     * @return
     */
    public double getG(){return this.G;}
    /**
     * Getter function.
     * @return
     */
    public double getH(){return this.H;}

    /**
     * Getter function
     * @return
     */
    public double getScore(){
        return F;
    }

    /**
     * Setter function.
     * @param g
     * @param h
     */
    public void setScore(double g, double h){
        G = g;
        H = h;
        F = G + H;
    }
    /**
     * Setter function. Sets g but performs F = G + H
     * @param g
     */
    public void setScore(double g){
        G = g;
        F = G + H;
    }
    /**
     * Setter function.
     * @param i
     */
    public void setParent(int i){
        parent = i;
    }
}

