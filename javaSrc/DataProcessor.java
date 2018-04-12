import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Data Processor for testing the AdjMatrix and IndMatrix implementations of
 * the friendship graph
 * 
 * @author Joshua Tencic, Mia Turner, 2018
 */
public class DataProcessor {

    public static FriendshipGraph<String> graph;
    public static final int VERTEX_RANGE = 100;
    public static final int ITERATIONS_PER_TEST = 100;
    private static final int COMMANDS_PER_TEST = 50;
    
    public static void main(String args[]) throws IOException{
        PrintWriter pw = null;
        //Load up the facebook_combined.txt file and
        //Take a subset of it so that it's faster to work with (1000 lines)
        String filename = "facebook_combined.txt";
        ArrayList<String> initialData = loadFile(filename);
        //Adjacency Matrix
        pw = new PrintWriter(new File("adjMatTestResults.csv"));
        System.out.println("Testing Adjacency Matrix\n-------------------");
        //For each scenario
        for (int i = 1; i <= 3; i++) {
            System.out.println("\nScenario " + i + "\n-----------------");
            testGraph(initialData, i, 0.05f, pw, "adjmat");
        }
        pw.close();
        //Incidence Matrix
        pw = new PrintWriter(new File("indMatTestResults.csv"));
        System.out.println("\nTesting Incidence Matrix\n-------------------");
        //For each scenario
        for (int i = 1; i <= 3; i++) {
            System.out.println("\nScenario " + i + "\n-----------");
            testGraph(initialData, i, 0.05f, pw, "indmat");
        }
        pw.close();
        System.out.println("\nI'm finished!!!");
    }
    
    /*
     * Load the file and store it in a local variable for reuse later
     * @param filename The name of the file to be loaded
     */
    static ArrayList<String> loadFile(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        ArrayList<String> data = new ArrayList<String>();
        while ((line = reader.readLine()) != null) {
            data.add(line);
        }
        reader.close();
        return data;
    }
    
    /*
     * Use the data loaded from the file to reset and repopulate the graph 
     * to it's initial state
     */
    public static GraphData loadMatrix(ArrayList<String> data, String implementationType, int sampleSize) {
        GraphData graphData = new GraphData();
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
    
        String line;
        String delimiter = " ";
        String[] tokens;
        String srcLabel, tarLabel;
        
        int counter = 0;
        while ((line = data.get(counter)) != null && counter < sampleSize) {
            tokens = line.split(delimiter);
            srcLabel = tokens[0];
            tarLabel = tokens[1];
            graph.addVertex(srcLabel);
            graph.addVertex(tarLabel);
            graph.addEdge(srcLabel, tarLabel);
            graphData.addVertex(srcLabel);
            graphData.addVertex(tarLabel);
            graphData.addEdge(srcLabel, tarLabel);
            counter++;
        }
        return graphData;
    }
    
    /*
     * Run tests on the graph at each density required, and add 
     * that data to the output file
     */
    public static void testGraph(ArrayList<String> initData, int scenario, float increment, PrintWriter pw, String graphType) {
        //Create the data column headings
        StringBuilder results = new StringBuilder();
        results.append("Scenario " + scenario + "\n");
        results.append("Density, ");
        results.append("Time lapsed");
        results.append("\n");
        //Iterate through all the densities
        for (float i = 0f; i <= 1.001f; i += increment) {
            System.out.println(String.format("Graph Density: %.2f, ", i));
            double avgEstimatedTime = 0.0f;
            //Once each level of density is correct, add an arbitrary number of edges to the graph
            for (int j = 0; j < ITERATIONS_PER_TEST; j++) {
                //Reset the graphData and matrix
                GraphData graphData = loadMatrix(initData, graphType, VERTEX_RANGE);
                //Bring the density to the required amount
                increaseDensityTo(i, graphData);
                //Generate test data
                String testData = DataGenerator.generateTestData(COMMANDS_PER_TEST, scenario);
                //Measure how long this takes for each density level using nanoTime
                long startTime = System.nanoTime();
                //Run the generated test data
                processOperations(testData);
                long deltaTime = System.nanoTime() - startTime;
                double estimatedTime = (double) deltaTime / Math.pow(10, 9);
                avgEstimatedTime += estimatedTime;
            }
            //Average out the results
            avgEstimatedTime /= ITERATIONS_PER_TEST;
            System.out.println(String.format("Time lapsed: %.10f s", avgEstimatedTime));
            //Append results
            results.append(String.format("%.2f, ", i));
            results.append(String.format("%.10f", avgEstimatedTime));
            results.append("\n");
        }
        //Write to the file
        pw.write(results.toString());
    }

    /*
     * Add random edges to the graph until the density is the desired amount
     */
    public static void increaseDensityTo(float desiredDensity, GraphData graphData) {
        while (graphData.calcDensity() < desiredDensity) {
            //Randomly add edges until density = desiredDensity
            String v1 = DataGenerator.randomStrNum(0, VERTEX_RANGE);
            String v2 = DataGenerator.randomStrNum(0, VERTEX_RANGE);
            if (v1 != v2) {
                graph.addEdge(v1, v2);
                graphData.addEdge(v1, v2);
            }
        }
    }

    /*
     * Run the commands stored in the commandlist
     */
    public static void processOperations(String commandList) {
        String[] lines = commandList.split("\n");
        for (int i = 0; i < lines.length; i++) {
            String[] tokens = lines[i].split(" ");

            // check if there is at least an operation command
            if (tokens.length < 1) {
                System.err.println("not enough tokens.");
                return;
            }

            String command = tokens[0];
            
            try {
                // determine which operation to execute
                switch (command.toUpperCase()) {
                    // add vertex
                    case "AV":
                        if (tokens.length == 2) {
                            graph.addVertex(tokens[1]);
                        }
                        else {
                            System.err.println("incorrect number of tokens.");
                        }
                        break;
                    // add edge
                    case "AE":
                        if (tokens.length == 3) {
                            graph.addEdge(tokens[1], tokens[2]);
                        }
                        else {
                            System.err.println("incorrect number of tokens.");
                        }
                        break;                                    
                    // neighbourhood
                    case "N":
                        if (tokens.length == 2) {
                            graph.neighbours(tokens[1]);
                        }
                        else {
                            System.err.println("incorrect number of tokens.");
                        }

                        break;
                    // remove vertex
                    case "RV":
                        if (tokens.length == 2) {
                            graph.removeVertex(tokens[1]);
                        }
                        else {
                            System.err.println("incorrect number of tokens.");
                        }
                        break;
                    // remove edge
                    case "RE":
                        if (tokens.length == 3) {
                            graph.removeEdge(tokens[1], tokens[2]);
                        }
                        else {
                            System.err.println("incorrect number of tokens.");
                        }
                        break;      
                    // compute shortest path distance
                    case "S":
                        if (tokens.length == 3) {
                            graph.shortestPathDistance(tokens[1], tokens[2]);
                        }
                        else {
                            System.err.println("incorrect number of tokens.");
                        }
                        break;
                    default:
                        System.err.println("Unknown command.");
                } // end of switch()
            } 
            catch (IllegalArgumentException e) {
                System.err.println(e.getMessage());
            }
        }
    }
     
}
