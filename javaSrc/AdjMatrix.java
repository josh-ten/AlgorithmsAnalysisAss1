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
    
    
    public void addVertex(T vertLabel) {
        //Add it's own row
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
	
    
    public void addEdge(T srcLabel, T tarLabel) {
        //Find the corresponding vertex
        ArrayList<T> labelRow = matrix.get(0);
        int srcIndex = labelRow.indexOf(srcLabel);
        int tarIndex = labelRow.indexOf(tarLabel);
        ArrayList<T> src = matrix.get(srcIndex + 1);
        ArrayList<T> tar = matrix.get(tarIndex + 1);
                
        int currSrcEdges = (int) src.get(tarIndex+1);
        T newSrcEdges = (T) new Integer(currSrcEdges+1);
        int currTarEdges = (int) tar.get(srcIndex+1);
        T newTarEdges = (T) new Integer(currTarEdges+1);
        src.set(tarIndex+1, newSrcEdges);
        tar.set(srcIndex+1, newTarEdges);
       
    } // end of addEdge()
	

    public ArrayList<T> neighbours(T vertLabel) {
        ArrayList<T> neighbours = new ArrayList<T>();
        
        // Implement me!
        
        return neighbours;
    } // end of neighbours()
    
    
    public void removeVertex(T vertLabel) {
        // Implement me!
    } // end of removeVertex()
	
    
    public void removeEdge(T srcLabel, T tarLabel) {
        // Implement me!
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
    } // end of printEdges()
    
    public int shortestPathDistance(T vertLabel1, T vertLabel2) {
    	// Implement me!
    	
        // if we reach this point, source and target are disconnected
        return disconnectedDist;    	
    } // end of shortestPathDistance()
    
} // end of class AdjMatrix