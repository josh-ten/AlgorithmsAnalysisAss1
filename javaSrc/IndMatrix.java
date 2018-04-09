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
        if ((srcInd==0) || (tarInd==0)){
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
        if (vertIndex==0){
            //given vertex does not exist.
            System.err.println("Vertex does not exist");
            return;
        }
        
        //first remove connected edges
//        for(int i=1; i<matrix.get(vertIndex).size(); i++){
//            if (matrix.get(vertIndex).get(i).equals(1))
//                matrix.get(0).remove(i-1);
//                for (int j=1; j<matrix.size(); j++){
//                    matrix.get(j).remove(edgeIndex+1);
//                }
//        }
        
        
        matrix.remove(vertIndex);
        
       
    } // end of removeVertex()
	
    
    public void removeEdge(T srcLabel, T tarLabel) {
       String edge = (String)(srcLabel + " " + tarLabel);
       int edgeIndex = findEdgeIndex((T)edge);
       if (edgeIndex==-1){
           //given edge does not exist.
           System.err.println("Edge does not exist");
           return;
       }
       if (findVertIndex(srcLabel)==0 || findVertIndex(srcLabel)==0){
           System.err.println("One or both vertex/s does not exist");
       }
       
       matrix.get(0).remove(edgeIndex);
       for (int i=1; i<matrix.size(); i++){
           matrix.get(i).remove(edgeIndex+1);
       }
    } // end of removeEdges()
	
    
    public void printVertices(PrintWriter os) {
        
        //System.out.println("printVertices call");
        //os.print("Vertices: ");
        for (int i=1; i<matrix.size(); i++){
            os.print(matrix.get(i).get(0));
        }
        os.println();

           
    } // end of printVertices()
	
    
    public void printEdges(PrintWriter os) {
      
        for(int i=0; i<matrix.get(0).size(); i++){
            os.println(matrix.get(0).get(i));
        }
    } // end of printEdges()
    
    
    public int shortestPathDistance(T vertLabel1, T vertLabel2) {
    	// Implement me!
    	
        // if we reach this point, source and target are disconnected
        return disconnectedDist;    	
    } // end of shortestPathDistance()
    
    
    /* returns the index of the given vertex, or 0 if not found */
    public int findVertIndex(T label){
        
        for (int i=1; i<matrix.size();i++){
            if (matrix.get(i).get(0).equals(label)){
                return i;
            }
        }
        return 0;
    }
    
    public int findEdgeIndex(T label){
        for (int i=0; i<matrix.get(0).size(); i++){
            if (label.equals(matrix.get(0).get(i))){
                return i;
            }
        }
        return -1;
    }
    
    //finds the second half of relationship from the first
    public T findSrcFromTar(int srcInd, int edgeInd){
        
        for (int i=1; i<matrix.size(); i++){
            if ((i!= srcInd) && matrix.get(i).get(edgeInd).equals(1)){
                return (T)matrix.get(i).get(0);
            }
        }
        return null;
          
    }
    
    
    
} // end of class IndMatrix
