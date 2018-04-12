import java.util.ArrayList;

public class GraphData {
    private ArrayList<String[]> edges;
    private ArrayList<String> vertices;
    
    public GraphData() {
        edges = new ArrayList<String[]>();
        vertices = new ArrayList<String>();
    }
    
    public float calcDensity() {
        //Calculate the density of the graph using D=(2*edgeCount)/(vertCount*(vertCount-1))
        return (2 * getEdgeCount()) / (float)(getVertexCount() * (getVertexCount() - 1));
    }
    
    public boolean addEdge(String[] vertices) {
        if (edges.contains(vertices)) return false;
        edges.add(vertices);
        return true;
    }
    public boolean addEdge(String vertexA, String vertexB) {
        return addEdge(new String[]{vertexA, vertexB});
    }
    public int getEdgeCount() {
        return edges.size();
    }
    
    public boolean addVertex(String vertex) {
        if (vertices.contains(vertex)) return false;
        vertices.add(vertex);
        return true;
    }
    public int getVertexCount() {
        return vertices.size();
    }
}
