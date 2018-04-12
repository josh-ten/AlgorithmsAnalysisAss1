import java.io.*;
import java.util.*;


/**
 * Incidence matrix implementation for the FriendshipGraph interface.
 * 
 * Your task is to complete the implementation of this class.  You may add methods, but ensure your modified class compiles and runs.
 *
 * @author Jeffrey Chan, 2016.
 */
public class IndMatrix <T extends Object> implements FriendshipGraph<T>
{

    public ArrayList<ArrayList<T>> matrix;
	/**
	 * Contructs empty graph.
	 */
    public IndMatrix() {
        matrix = new ArrayList<ArrayList<T>>();
        //Label row
        ArrayList<T> labelRow = new ArrayList<T>();
        matrix.add(labelRow);
    } // end of IndMatrix()
    
    
    @SuppressWarnings("unchecked")
    public void addVertex(T vertLabel) {
        //check if vertex already exists[
        if (findVertIndex(vertLabel)!=-1){
            //System.err.println("Vertex already exists");
            return;
        }
        ArrayList<T> vertex = new ArrayList<T>();
        vertex.add(vertLabel);

        //for the edges that exist in the array, set value to 0
        for (int i=0; i<matrix.get(0).size();i++){
            T blankCell = (T) new Integer(0);
            vertex.add(blankCell);
        }
        matrix.add(vertex);
        //System.out.println("Adding vertex " + vertex.get(0) + " To array");
    } // end of addVertex()
	
    
    @SuppressWarnings("unchecked")
    public void addEdge(T srcLabel, T tarLabel) {
        //check that src and tar exist in graph
        int srcInd = findVertIndex(srcLabel);
        int tarInd = findVertIndex(tarLabel);
        if ((srcInd==-1) || (tarInd==-1)){
            System.out.println("label not in graph");
            return;
        }
        //add label to matrix
        String newEdge= (String)srcLabel + " " + (String)tarLabel;
        //System.out.println("adding new edge " + newEdge);
        matrix.get(0).add((T) newEdge);
        int edgeIndex = findEdgeIndex((T) newEdge);
        //add cells to new edge
        for (int i=1; i<matrix.size();i++){
            T newCell;
            if ((i==srcInd) || (i==tarInd)){
                newCell = (T) new Integer(1);
            }
            else{
                newCell = (T) new Integer(0);
            }
            matrix.get(i).add(newCell);
        }
    } // end of addEdge()
	

    public ArrayList<T> neighbours(T vertLabel) {
        ArrayList<T> neighbours = new ArrayList<T>();
        int vertIndex = findVertIndex(vertLabel);
        
        for (int i=1; i<matrix.get(vertIndex).size(); i++){
                if (matrix.get(vertIndex).get(i).equals(1)){
                   neighbours.add(findSrcFromTar(vertIndex, i));
                }
        }
        return neighbours;
    } // end of neighbours()
    
    
    public void removeVertex(T vertLabel) {

        int vertIndex = findVertIndex(vertLabel);
        if (vertIndex==-1){
            //given vertex does not exist.
            //System.err.println("Vertex does not exist");
            return;
        }
        ArrayList<T> edges = new ArrayList<T>();
        
        //first find and remove edges incorporating the vertex to remove
        
        for(int i=1; i<matrix.get(vertIndex).size(); i++){
            if (matrix.get(vertIndex).get(i).equals(1)){
                //System.out.println("Must remove edge " + matrix.get(0).get(i-1));
                edges.add(matrix.get(0).get(i-1));        
            }
        }
        
        for (int i=0; i<edges.size(); i++){
            int edgeIndex = findEdgeIndex(edges.get(i));
           // System.out.println("Edge index is " + edgeIndex);
            matrix.get(0).remove(edgeIndex);
            
            for (int j=1; j<matrix.size(); j++){
                matrix.get(j).remove(edgeIndex+1);
            }
        }
        matrix.remove(vertIndex);
        
       
    } // end of removeVertex()
	
    @SuppressWarnings("unchecked")
    public void removeEdge(T srcLabel, T tarLabel) {
        
        int v1 = findVertIndex(srcLabel);
        int v2 = findVertIndex(tarLabel);
        if (v1==-1 || v2==-1){
            // System.err.println("One or both vertex/s does not exist");
            return;
        }
        
        T edge = null;
        for (int i=1; i<matrix.get(v1).size();i++){
            if (matrix.get(v1).get(i).equals(1) && matrix.get(v2).get(i).equals(1)){
                edge = matrix.get(0).get(i-1);
            }
        }
        if (edge == null) {
            return;
        }
        int edgeIndex = findEdgeIndex(edge);
       
        matrix.get(0).remove(edgeIndex);
        for (int i=1; i<matrix.size(); i++){
            matrix.get(i).remove(edgeIndex+1);
        }
    } // end of removeEdges()
	
    
    public void printVertices(PrintWriter os) {
        
        //System.out.println("printVertices call");
        //os.print("Vertices: ");
        for (int i=1; i<matrix.size(); i++){
            os.print(matrix.get(i).get(0) + " ");
        }
        os.println();

           
    } // end of printVertices()
	
    
    public void printEdges(PrintWriter os) {
        for(int i=1; i<matrix.size(); i++){
            for(int j=1; j<matrix.get(i).size();j++){
                if (matrix.get(i).get(j).equals(1)){
                    T v1 = matrix.get(i).get(0);
                    T v2 = findSrcFromTar(i , j);
                    os.println(v1 + " " + v2);
                }
            }
        }
    } // end of printEdges()
    
    
    public int shortestPathDistance(T vertLabel1, T vertLabel2) {
    	int v1=findVertIndex(vertLabel1);
    	int v2=findVertIndex(vertLabel2);
    	T goal = matrix.get(v2).get(0);
    	int distance = 0;
        
    	ArrayList<T> path = new ArrayList<T>();
    	ArrayList<T> checked = new ArrayList<T>();
    	path.add(matrix.get(v1).get(0));
    	distance = checkVertex(distance, path, goal, checked);
    	
    	return distance;
    } // end of shortestPathDistance()
    
    
  
    /* recursively checks each next level of the graph until either it checks all 
     * neighbours or finds a connection*/
    public int checkVertex(int distance, ArrayList<T> toCheck, T goal, ArrayList<T> checked){
        
        //recursive break case
        if (toCheck.contains(goal)){
            //.out.println("FOUND A CONNECTION, distance is " + distance);
            return distance;
        }
        distance++;
        ArrayList<T> neighbours = new ArrayList<T>();
        
        //for each vertex in toCheck
        for (int i=0; i<toCheck.size(); i++){
            int currNode = findVertIndex(toCheck.get(i));
            if (currNode==-1){
                //vert not found
                continue;
            }
            //System.out.println("Checking vertex " + toCheck.get(i));
            
            for (int j=1; j<matrix.get(currNode).size();j++){
                if (matrix.get(currNode).get(j).equals(1)){
                    T target = findSrcFromTar(currNode, j);
                    //if end vertex of relationship hasn't already been checked
                    if (!checked.contains(target)){
                        neighbours.add(target);
                    }
                }
            } 
            //finished checking this vertex so add to checked arraylist
            checked.add(toCheck.get(i));
        }
        
        if (neighbours.size()==0){
            //System.out.println("NO CONNECTION");
            return disconnectedDist;
        }
        return checkVertex(distance, neighbours, goal, checked);
        
        
       
        
    }
    
    /*DEBUGGING METHOD
     * public void printMatrix(){
        
        for (int i=0; i<matrix.size();i++){
            for (int j=0; j<matrix.get(i).size();j++){
                if(i==0 && j==0){
                    System.out.print("  ");
                }
                System.out.print(matrix.get(i).get(j));
            }
            System.out.println();
        }
    }
    
    */
    
    /* returns the index of the given vertex, or 0 if not found */
    public int findVertIndex(T label){
        
        for (int i=1; i<matrix.size();i++){
            if (matrix.get(i).get(0).equals(label)){
                return i;
            }
        }
        return -1;
    }
    /* finds index of given edge or -1 if not found*/
    public int findEdgeIndex(T label){
        for (int i=0; i<matrix.get(0).size(); i++){
            if (label.equals(matrix.get(0).get(i))){
                return i;
            }
        }
        return -1;
    }
    
    /*finds the second half of relationship from the first*/
    public T findSrcFromTar(int srcInd, int edgeInd){
        
        for (int i=1; i<matrix.size(); i++){
            if ((i!= srcInd) && matrix.get(i).get(edgeInd).equals(1)){
                return (T)matrix.get(i).get(0);
            }
        }
        return null;
          
    }
    
    
    
} // end of class IndMatrix
