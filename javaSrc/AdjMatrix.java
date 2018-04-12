import java.io.*;
import java.util.*;


/**
 * Adjacency matrix implementation for the FriendshipGraph interface.
 * 
 * Your task is to complete the implementation of this class.  You may add methods, but ensure your modified class compiles and runs.
 *
 * @author Jeffrey Chan, 2016.
 */
public class AdjMatrix <T extends Object> implements FriendshipGraph<T>
{
    public ArrayList<ArrayList<T>> matrix;
	/**
	 * Contructs empty graph.
	 */
    public AdjMatrix() {
    	matrix = new ArrayList<ArrayList<T>>();
    	//Label row
    	ArrayList<T> labelRow = new ArrayList<T>();
    	matrix.add(labelRow);
    } // end of AdjMatrix()
    
    @SuppressWarnings("unchecked")
    public void addVertex(T vertLabel) {
        // System.out.println("Adding vertex " + vertLabel + "...");
        int existingIndex = findVertIndex(vertLabel);
        if (existingIndex != -1) return;

        //Add new row
        ArrayList<T> vertex = new ArrayList<T>();
        vertex.add(vertLabel);
        matrix.add(vertex);
        //Add to label row
        ArrayList<T> labelRow = matrix.get(0);
        labelRow.add(vertLabel);
        //Add blank cells to existing verticies (skipping label row)
        for (int i = 1; i < matrix.size(); i++) {
            vertex = matrix.get(i);
            for (int j = vertex.size(); j < matrix.size(); j++) {
                T blankCell = (T) new Integer(0);
                vertex.add(blankCell);
            }
        }
    } // end of addVertex()
	
    
    @SuppressWarnings("unchecked")
    public void addEdge(T srcLabel, T tarLabel) {
        int srcIndex = findVertIndex(srcLabel);
        int tarIndex = findVertIndex(tarLabel);
        ArrayList<T> src = matrix.get(srcIndex);
        ArrayList<T> tar = matrix.get(tarIndex);
        // System.out.println("Adding edge from " + srcLabel + " to " + tarLabel + "...");
                
        //Find the new value to put in the relation cell
        Integer currSrcEdges = (Integer) src.get(tarIndex);
        Integer currTarEdges = (Integer) tar.get(srcIndex);
        T newSrcEdges = (T) new Integer(currSrcEdges+1);
        T newTarEdges = (T) new Integer(currTarEdges+1);
        
        //Set the value
        src.set(tarIndex, newSrcEdges);
        tar.set(srcIndex, newTarEdges);
        
    } // end of addEdge()
	

    public ArrayList<T> neighbours(T vertLabel) {
        ArrayList<T> neighbours = new ArrayList<T>();
        
        int vertIndex = findVertIndex(vertLabel);
        ArrayList<T> vertex = matrix.get(vertIndex);
        
        //Find which vertices have 1 or more connection and add them to the list
        for (int i = 1; i < vertex.size(); i++) {
            if ((Integer) vertex.get(i) > 0) {
                ArrayList<T> neighbour = matrix.get(i); //Add for entire list of neighbours 
                T neighbourLabel = neighbour.get(0); //Add for only label
                neighbours.add(neighbourLabel);
            }
        }
        
        return neighbours;
    } // end of neighbours()
    
    
    public void removeVertex(T vertLabel) {
        int vertIndex = findVertIndex(vertLabel);
        if (vertIndex == -1) return;
        ArrayList<T> vertexToRemove = matrix.get(vertIndex);
        // System.out.println("Removing " + vertLabel + "...");
        
        for (int i = 1; i < matrix.size(); i++) {
            ArrayList<T> vertex = matrix.get(i);
            vertex.remove(vertIndex);
        }
        
        matrix.remove(vertexToRemove);
        matrix.get(0).remove(vertIndex - 1);
    } // end of removeVertex()
	
    
    @SuppressWarnings("unchecked")
    public void removeEdge(T srcLabel, T tarLabel) {
        int srcIndex = findVertIndex(srcLabel);
        int tarIndex = findVertIndex(tarLabel);
        if (srcIndex == -1 || tarIndex == -1) return;
        ArrayList<T> src = matrix.get(srcIndex);
        ArrayList<T> tar = matrix.get(tarIndex);
                
        //Find the new value to put in the relation cell
        Integer currSrcEdges = (Integer) src.get(tarIndex);
        Integer currTarEdges = (Integer) tar.get(srcIndex);
        T newSrcEdges = (T) new Integer(currSrcEdges - 1);
        T newTarEdges = (T) new Integer(currTarEdges - 1);
        
        if ((Integer) newSrcEdges < 0 || (Integer) newTarEdges < 0) {
//            System.out.println("ERROR: No edge between " + srcLabel + " and " + tarLabel + "...");
        } else {
            // System.out.println("\nRemoving edge between " + srcLabel + " and " + tarLabel);
            //Set the value
            src.set(tarIndex, newSrcEdges);
            tar.set(srcIndex, newTarEdges);
        }
    } // end of removeEdges()
	
    
    public void printVertices(PrintWriter os) {
        ArrayList<T> labelRow = matrix.get(0);
        for (int i = 0; i < labelRow.size(); i++) {
            os.print(labelRow.get(i) + " ");
        }
        os.println();
    } // end of printVertices()
	
    
    public void printEdges(PrintWriter os) {
        for (int i = 1; i < matrix.size(); i++) {
            for (int j = 1; j < matrix.get(i).size(); j++) {
                if ((Integer) matrix.get(i).get(j) > 0) {
                    os.println(matrix.get(i).get(0) + " " + matrix.get(0).get(j-1));
                }
            }
        }
        /*os.println("\nAdjacency Matrix: ");
        os.println("   " + matrix.get(0)); //Label row
        for (int i = 1; i < matrix.size(); i++) {
            ArrayList<T> row = matrix.get(i);
            os.println(row);
        }
        os.println();*/
    } // end of printEdges()
    
    public int shortestPathDistance(T vertLabel1, T vertLabel2) {
    	ArrayList<T> start = labelToVert(vertLabel1);
    	ArrayList<T> goal = labelToVert(vertLabel2);
    	
    	ArrayList<ArrayList<T>> currNodes = new ArrayList<ArrayList<T>>();
    	currNodes.add(start);
    	
    	ArrayList<ArrayList<T>> checkedNodes = new ArrayList<ArrayList<T>>();
    	int distance = checkNeighbours(currNodes, goal, 0, checkedNodes);
    	return distance;    	
    } // end of shortestPathDistance()
    
    /*
     * Find the node using a breadth first search
     */
    private int checkNeighbours(ArrayList<ArrayList<T>> currNodes, ArrayList<T> goal, int distance, ArrayList<ArrayList<T>> checkedNodes) {
        if (currNodes.contains(goal)) {
            return distance;
        }
        
        ArrayList<ArrayList<T>> neighbours = new ArrayList<ArrayList<T>>();
        //Go through all nodes at level
        for (int i = 0; i < currNodes.size(); i++) {
            ArrayList<T> node = currNodes.get(i);
            T nodeLabel = node.get(0);
            ArrayList<T> connectedNodes = neighbours(nodeLabel);
            //Go through all the connected nodes
            for (int n = 0; n < connectedNodes.size(); n++) {
                ArrayList<T> conNode = labelToVert(connectedNodes.get(n));
                //If the node hasn't yet been checked, add it to the list of neighbours
                if (!checkedNodes.contains(conNode)) {
                    neighbours.add(conNode);
                }
            }
        }
        //Determine if source and target are disconnected
        if (neighbours.size() == 0) return disconnectedDist;
        
        checkedNodes.addAll(currNodes);
        //Perform the same set of checks on the next unchecked neighbours
        return checkNeighbours(neighbours, goal, ++distance, checkedNodes);
    }

    
    private ArrayList<T> labelToVert(T vertLabel) {
        int vertIndex = findVertIndex(vertLabel);
        ArrayList<T> vertex = matrix.get(vertIndex);
        return vertex;
    }
    

    private int findVertIndex(T vertLabel) {
        //Find the corresponding vertex
        ArrayList<T> labelRow = matrix.get(0);
        int vertIndex;
        if ((vertIndex = labelRow.indexOf(vertLabel)) == -1) {
            // System.out.println("Error: Vertex '" + vertLabel + "' does not exist in the matrix");
            return -1;
        }
        vertIndex += 1; //Add 1 to account for the label cell
        return vertIndex;
    }
    
} // end of class AdjMatrix