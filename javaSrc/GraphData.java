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
    public boolean addEdge(Integer vertexA, Integer vertexB) {
        return addEdge(new String[]{vertexA.toString(), vertexB.toString()});
    }
    public ArrayList<String[]> getEdges() {
        return edges;
    }
    public int getEdgeCount() {
        return edges.size();
    }
    public boolean removeEdge(String vertexA, String vertexB) {
        for (int i = 0; i < edges.size(); i++) {
            if ((edges.get(i)[0] == vertexA &&
                 edges.get(i)[1] == vertexB) ||
                (edges.get(i)[1] == vertexA &&
                 edges.get(i)[0] == vertexB)) {
                edges.remove(i);
                return true;
            }
        }
        return false;
    }
    public boolean removeEdge(String[] vertices) {
        return removeEdge(vertices[0], vertices[1]);
    }
    
    public boolean addVertex(String vertex) {
        if (vertices.contains(vertex)) return false;
        vertices.add(vertex);
        return true;
    }
    public ArrayList<String> getVertices() {
        return vertices;
    }
    public int getVertexCount() {
        return vertices.size();
    }
}
