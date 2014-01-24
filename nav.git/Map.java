package pathserver;

import java.util.ArrayList;

/**
 * 
 * @author Jacob D'Onofrio
 */
public class Map {
    
    private ArrayList<Node> nodes;  //All the Nodes in the map
    private ArrayList<Node> m_rr;  //All of the mens restrooms
    private ArrayList<Node> w_rr;  //All of the womens restrooms
    private ArrayList<Node> exits; //All of the exits
    
    //Lists that are used to run A*
    //open has nodes that are still to be checked
    //closed has nodes that are already checked
    private ArrayList<Node> open;
    private ArrayList<Node> closed;

    Map() {
        open = new ArrayList<Node>();
        closed = new ArrayList<Node>();
        nodes = new ArrayList<Node>();
        m_rr = new ArrayList<Node>();
        w_rr = new ArrayList<Node>();
        exits = new ArrayList<Node>();
    }

    /**
     * This function runs the A* algorithm and returns the
     * node path in reverse order.
     * @see en.wikipedia.com/A*
     * @param x
     * @param y
     * @return
     */
    public ArrayList<Node> getDirection(int x, int y) {
        ArrayList<Node> path = new ArrayList<Node>();
        Node curr;
        Node start = getNode(x);
        Node end = getNode(y);
        start.setParent(x);

        open.add(start);
        while (open.size() > 0) {
            //Grab Node with lowest score
            curr = open.get(FindLowestScore());

            //Remove from open and put in closed
            OpenToClosed(curr.getId());

            //Check to see if we are done
            if (curr.getId() == end.getId()) {
                break;
            }

            //Parse through neighbors
            for (int i = 0; i < curr.getNeighbors().size(); i++) {
                boolean tentative = false;
                //Check if in closed
                if (isClosed(curr.getNeighborID(i))) {
                    continue;
                }

                //set tentative G
                double t_G = Distance(curr, getNode(curr.getNeighborID(i)));
                double t_H = Distance(getNode(curr.getNeighborID(i)), end);

                //If not an intersection, add 50 to prevent from using as a path
                if (!(getNode(curr.getNeighborID(i)).label.equals("INTERSECTION"))) {
                    //t_H += 50.0;
                }
                //Check for rooms on differnt floors
                if (getNode(curr.getNeighborID(i)).getZ() != end.getZ()) {
                    //t_H += 5000;
                }
                //Check if in Open Set
                if (!isOpen(curr.getNeighborID(i))) {
                    NodesToOpen(curr.getNeighborID(i));
                    tentative = true;
                } else if ((t_G+t_H) < getOpen(curr.getNeighborID(i)).getScore()) {
                    tentative = true;
                } else {
                    tentative = false;
                }

                if (tentative) {
                    getOpen(curr.getNeighborID(i)).setParent(curr.getId());
                    getOpen(curr.getNeighborID(i)).setScore(t_G, t_H);
                }
            }
        }
        //Reconstruct Path
        path.add(closed.get(closed.size() - 1));
        while(true)
        {
            Node tmp = getClosed(path.get(path.size()-1).getParent());
            if(tmp.getId() == start.getId()){
                path.add(tmp);
                break;
            }
            else{
                path.add(tmp);
            }
        }
        return path;
    }

    private Node getClosed(int x) {
        boolean flag = false;
        int i = 0;
        while (i < closed.size()) {
            if (closed.get(i).getId() == x) {
                flag = true;
                break;
            }
            i++;
        }
        if (flag) {
            return closed.get(i);
        } else {
            return null;
        }
    }

    private Node getOpen(int x) {
        boolean flag = false;
        int i = 0;
        while (i < open.size()) {
            if (open.get(i).getId() == x) {
                flag = true;
                break;
            }
            i++;
        }
        if (flag) {
            return open.get(i);
        } else {
            return null;
        }
    }

    private boolean isOpen(int x) {
        boolean flag = false;
        for (int i = 0; i < open.size(); i++) {
            if (open.get(i).getId() == x) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    private boolean isClosed(int x) {
        boolean flag = false;
        for (int i = 0; i < closed.size(); i++) {
            if (closed.get(i).getId() == x) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    private void AddNeighbor(int x) {
        for (int i = 0; i < 5; i++) {
            Node tmp = new Node();
        }
    }

    //Return the Index of the node of the
    //lowest score in the open list
    private int FindLowestScore() {
        int node = 0;
        double current_lowest = open.get(0).getScore();

        for (int i = 0; i < open.size(); i++) {
            if (open.get(i).getScore() < open.get(node).getScore()) {
                node = i;
            }
        }
        return node;
    }

    //Create a new node and copy the data from the 
    //ID in nodes list. Then add the node to the 
    //open list
    private void NodesToOpen(int id) {
        Node tmp = new Node();
        for (int i = 0; i < nodes.size(); i++) {
            if (id == nodes.get(i).getId()) {
                tmp = nodes.get(i);
                //tmp.setCoordinates(nodes.get(i).getX(), nodes.get(i).getY(), nodes.get(i).getZ());
                //tmp.setScore(nodes.get(i).getG(), nodes.get(i).getH());
                open.add(tmp);
                break;
            }
        }
    }

    //Create a new node and copy the data from the
    //open index node to the new node. THen add the 
    //new node to the closed list and remove it from the
    //open list
    private void OpenToClosed(int id) {
        Node tmp = new Node();
        for (int i = 0; i < open.size(); i++) {
            if (id == open.get(i).getId()) {
                tmp = open.get(i);
                //tmp.setCoordinates(open.get(i).getX(), open.get(i).getY(), open.get(i).getZ());
                //tmp.setScore(open.get(i).getG(), open.get(i).getH());
                closed.add(tmp);
                open.remove(i);
                break;
            }
        }
    }

    /**
     * This function computes the distance between two Node objects.
     * @param x
     * @param y
     * @return
     */
    public double Distance(Node x, Node y) {
        double tmp = ((y.X - x.X) * (y.X - x.X)) + ((y.Y - x.Y) * (y.Y - x.Y)) + ((y.Z - x.Z) * (y.Z - x.Z));
        double dist = Math.sqrt(tmp);
        return dist;
    }

    /**
     * @deprecated 
     * @param l
     * @return
     */
    public int Lowest(ArrayList<Node> l) {
        int i = 0;
        return i;
    }

    /**
     * This function is used when building the map to populate
     * the list of nodes on the map.
     * @param x
     */
    public void addNode(Node x) {
        nodes.add(x);
    }

    /**
     * This function returns a selected node based on the supplied ID
     * @param i
     * @return
     */
    public Node getNode(int i) {
        int j = 0;
        boolean flag = false;
        while (j < nodes.size()) {
            if (nodes.get(j).getId() == i) {
                flag = true;
                break;
            }
            j++;
        }
        if (flag) {
            return nodes.get(j);
        } else {
            return null;
        }
    }

    /**
     * This function is used when calling directions to find the closest
     * Node on the map to the client
     * @param x
     * @param y
     * @param z
     * @return
     */
    public int FindNearestNode(double x, double y, double z) {
        int id = 0;
        Node tmp = new Node(0, x, y, z);
        double dist = Distance(tmp, nodes.get(0));

        id = nodes.get(0).getId();
        for (int i = 0; i < nodes.size(); i++) {
            double blah = Distance(tmp, nodes.get(i));
            if (dist > blah) {
                id = nodes.get(i).getId();
                dist = blah;
            }
        }

        return id;
    }

    /**
     * This finds the nearest Mens Restroom to the client
     * @param x
     * @param y
     * @param z
     * @return
     */
    public int FindNearestMensRestroom(double x, double y, double z) {
        int id = 0;
        Node tmp = new Node(0, x, y, z);
        double dist = Distance(tmp, m_rr.get(0));

        id = m_rr.get(0).getId();
        for (int i = 0; i < m_rr.size(); i++) {
            double blah = Distance(tmp, m_rr.get(i));
            if (dist > blah) {
                id = m_rr.get(i).getId();
                dist = blah;
            }
        }

        return id;
    }

    /**
     * THis function finds the nearest womens restroom to the client
     * @param x
     * @param y
     * @param z
     * @return
     */
    public int FindNearestWomensRestroom(double x, double y, double z) {
        int id = 0;
        Node tmp = new Node(0, x, y, z);
        double dist = Distance(tmp, w_rr.get(0));

        id = w_rr.get(0).getId();
        for (int i = 0; i < w_rr.size(); i++) {
            double blah = Distance(tmp, w_rr.get(i));
            if (dist > blah) {
                id = w_rr.get(i).getId();
                dist = blah;
            }
        }

        return id;
    }

    /**
     * This function finds the nearest exit to the client
     * @param x
     * @param y
     * @param z
     * @return
     */
    public int FindNearestExit(double x, double y, double z) {
        int id = 0;
        Node tmp = new Node(0, x, y, z);
        double dist = Distance(tmp, exits.get(0));

        id = exits.get(0).getId();
        for (int i = 0; i < exits.size(); i++) {
            double blah = Distance(tmp, exits.get(i));
            if (dist > blah) {
                id = exits.get(i).getId();
                dist = blah;
            }
        }

        return id;
    }

    /**
     * THis function is used when building the map to populate
     * the list of mens restroom nodes
     * @param x
     */
    public void AddMensRestroom(Node x) {
        m_rr.add(x);
    }

    /**
     * This function is used when building the map to populate
     * the list of womens restroom nodes
     * @param x
     */
    public void AddWomensRestroom(Node x) {
        w_rr.add(x);
    }

    /**
     * This function is used when building the map to populate
     * the list of exit nodes
     * @param x
     */
    public void AddExit(Node x) {
        exits.add(x);
    }
}
