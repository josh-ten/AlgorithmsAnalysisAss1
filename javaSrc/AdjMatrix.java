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
    /** _|A|B|C_
     *  A|1|0|1
     *  B|1|0|0
     *  C|1|1|0
     */
    
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
        //Add new row
        ArrayList<T> vertex = new ArrayList<T>();
        vertex.add(vertLabel);
        matrix.add(vertex);
        //Add to label row
        ArrayList<T> labelRow = matrix.get(0);
        labelRow.add(vertLabel);
        System.out.println("Adding vertex " + vertLabel + "...");
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
        
        System.out.println("Adding edge from " + srcLabel + " to " + tarLabel + "...");
                
        //Find the new value to put in the relation cell
        int currSrcEdges = (int) src.get(tarIndex);
        int currTarEdges = (int) tar.get(srcIndex);
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
            if ((int) vertex.get(i) > 0) {
                ArrayList<T> neighbour = matrix.get(i); //Add for entire list of neighbours 
                T neighbourLabel = neighbour.get(0); //Add for only label
                neighbours.add(neighbourLabel);
            }
        }
        
        return neighbours;
    } // end of neighbours()
    
    
    public void removeVertex(T vertLabel) {
        int vertIndex = findVertIndex(vertLabel);
        ArrayList<T> vertexToRemove = matrix.get(vertIndex);
        
        System.out.println("Removing " + vertLabel + "...");
        
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
        ArrayList<T> src = matrix.get(srcIndex);
        ArrayList<T> tar = matrix.get(tarIndex);
                
        //Find the new value to put in the relation cell
        int currSrcEdges = (int) src.get(tarIndex);
        int currTarEdges = (int) tar.get(srcIndex);
        T newSrcEdges = (T) new Integer(currSrcEdges - 1);
        T newTarEdges = (T) new Integer(currTarEdges - 1);
        
        if ((int) newSrcEdges < 0 || (int) newTarEdges < 0) {
            System.out.println("\nNo edge between " + srcLabel + " and " + tarLabel + "...");
        } else {
            System.out.println("\nRemoving edge between " + srcLabel + " and " + tarLabel);
            //Set the value
            src.set(tarIndex, newSrcEdges);
            tar.set(srcIndex, newTarEdges);
        }
    } // end of removeEdges()
	
    
    public void printVertices(PrintWriter os) {
        ArrayList<T> labelRow = matrix.get(0);
        os.print("Vertices: ");
        for (int i = 0; i < labelRow.size(); i++) {
            os.print(labelRow.get(i) + ", ");
        }
        os.println();
    } // end of printVertices()
	
    
    public void printEdges(PrintWriter os) {
        os.println("\nAdjacency Matrix: ");
        os.println("   " + matrix.get(0)); //Label row
        for (int i = 1; i < matrix.size(); i++) {
            ArrayList<T> row = matrix.get(i);
            os.println(row);
        }
        os.println();
    } // end of printEdges()
    
    public int shortestPathDistance(T vertLabel1, T vertLabel2) {
    	// Implement me!
    	
        // if we reach this point, source and target are disconnected
        return disconnectedDist;    	
    } // end of shortestPathDistance()
    

    private int findVertIndex(T vertLabel) {
        //Find the corresponding vertex
        ArrayList<T> labelRow = matrix.get(0);
        int vertIndex;
        if ((vertIndex = labelRow.indexOf(vertLabel)) == -1) {
            System.out.println("Error: Vertex '" + vertLabel + "' does not exist in the matrix");
            throw new IllegalArgumentException();
        }
        vertIndex += 1; //Add 1 to account for the label cell
        return vertIndex;
    }
    
} // end of class AdjMatrix