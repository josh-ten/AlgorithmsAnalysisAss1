import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class JoshGraphTester {

    private static FriendshipGraph<String> graph;
    
    public static void main(String args[]) {
        PrintWriter writer = new PrintWriter(System.out, true);
        
        graph = new AdjMatrix<String>();
        
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addVertex("D");
        graph.addVertex("E");
        graph.addEdge("A", "B");
        graph.addEdge("B", "D");
        graph.addEdge("D", "C");
        graph.addEdge("A", "E");
        
        graph.printEdges(writer);
        
        graph.shortestPathDistance("A", "E");
/*
        graph.removeVertex("B");
        
        graph.printEdges(writer);
        graph.addVertex("D");
        graph.addEdge("D", "C");
        graph.printEdges(writer);
        writer.println(graph.neighbours("C"));
        
        graph.removeEdge("C", "D");
        graph.printEdges(writer);
        graph.printVertices(writer);
*/
        String inputFilename = "";
        if (inputFilename != "") loadFile(inputFilename);
    }
    
    static void loadFile(String inputFilename) {
        if (inputFilename != null) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(inputFilename));
                
                String line;
                String delimiter = " ";
                String[] tokens;
                String srcLabel, tarLabel;
                
                while ((line = reader.readLine()) != null) {
                    tokens = line.split(delimiter);
                    srcLabel = tokens[0];
                    tarLabel = tokens[1];
                    graph.addVertex(srcLabel);
                    graph.addVertex(tarLabel);
                    graph.addEdge(srcLabel, tarLabel);
                }           
                reader.close();
            }
            catch (FileNotFoundException ex) {
                System.err.println("File " + inputFilename + " not found.");
            }
            catch(IOException ex) {
                System.err.println("Cannot open file " + inputFilename);
            }
        }
    }
}
