import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;

public class DataGenerator {
    
    static FriendshipGraph<String> graph;
    static int vertexRange = 100;
    static float desiredDensity = 0.1f;
    
    public static void main(String args[]) {
        PrintWriter os = new PrintWriter(System.out, true);
        //Load up the facebook_combined.txt file
        //Take a subset of it so that it's faster to work with (1000 lines)
        String filename = "facebook_combined.txt";
        int[] vertEdgeCount = loadFile(filename, "adjmat", vertexRange);
        int vertCount = vertEdgeCount[0];
        int edgeCount = vertEdgeCount[1];

        graph.printVertices(os);
        
        //Calculate the density of the graph using D=(2*edgeCount)/(vertCount*(vertCount-1))
        float density = calcDensity(vertCount, edgeCount);
        System.out.println(edgeCount);

        //Generate the commands to add random edges to the graph 
        //until the density is one of a range of values
        String commandList = "";
        while (density < desiredDensity) {
            //Randomly add edges until density = desiredDensity
            density = calcDensity(vertCount, edgeCount);
            Integer v1 = (int)(Math.random() * vertexRange);
            Integer v2 = (int)(Math.random() * vertexRange);
            if (v1 != v2) {
                graph.addEdge(v1.toString(), v2.toString());
                edgeCount++;
                commandList += "AE " + v1 + " " + v2 + "\n";
            }
        }
        System.out.println(edgeCount);
        
        
        //Do this for various densities (0.1, 0.2, 0.3 ... 1.0)
        
        //Once each level of density is correct, add an arbitrary number of edges to the graph
        //Measure how long this takes for each density level using millitime (lab 2)
        long startTime = System.nanoTime();
        int numEdgesToAdd = 500;
        for (int i = 0; i < numEdgesToAdd; i++) {
            Integer v1 = (int)(Math.random() * vertexRange);
            Integer v2 = (int)(Math.random() * vertexRange);
            if (v1 != v2) {
                graph.addEdge(v1.toString(), v2.toString());
                edgeCount++;
                commandList += "AE " + v1 + " " + v2 + "\n";
            }
        }
        long endTime = System.nanoTime();
        double estimatedTime = ((double)(endTime - startTime)) / Math.pow(10, 9);
        System.out.println(estimatedTime + "s");
        
        //Do this for both adjMat and indMat
    }
    
    static float calcDensity(int vertCount, int edgeCount) {
        float density = (2 * edgeCount) / (float)(vertCount * (vertCount - 1));
        return density;
    }
    
    static int[] loadFile(String filename, String implementationType, int sampleSize) {
        
        int[] vertEdgeCount = new int[2];
        
        // determine which implementation to test
        switch(implementationType) {
            case "adjmat":
                graph = new AdjMatrix<String>();
                break;
            case "indmat":
                graph = new IndMatrix<String>();
                break;
            default:
                System.err.println("Unknown implementation type.");
        }        
        
        // if file specified, then load file
        if (filename != null) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(filename));
                
                String line;
                String delimiter = " ";
                String[] tokens;
                String srcLabel, tarLabel;
                
                int counter = 0;
                ArrayList<String> existingVertices = new ArrayList<String>();
                while ((line = reader.readLine()) != null && counter < sampleSize) {
                    tokens = line.split(delimiter);
                    srcLabel = tokens[0];
                    tarLabel = tokens[1];
                    if (!existingVertices.contains(srcLabel)) {
                        existingVertices.add(srcLabel);
                        vertEdgeCount[0]++;
                    }
                    if (!existingVertices.contains(tarLabel)) {
                        existingVertices.add(tarLabel);
                        vertEdgeCount[0]++;
                    }
                    graph.addVertex(srcLabel);
                    graph.addVertex(tarLabel);
                    graph.addEdge(srcLabel, tarLabel);
                    vertEdgeCount[1]++;
                    counter++;
                }
                reader.close();
            }
            catch (FileNotFoundException ex) {
                System.err.println("File " + filename + " not found.");
            }
            catch(IOException ex) {
                System.err.println("Cannot open file " + filename);
            }
        }
        return vertEdgeCount;
    }
}
