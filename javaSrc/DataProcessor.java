import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class DataProcessor {

    public static FriendshipGraph<String> graph;
    public static int vertexRange = 100;
    
    public static void main(String args[]) throws IOException{
        //Load up the facebook_combined.txt file
        //Take a subset of it so that it's faster to work with (1000 lines)
        PrintWriter pw = null;
        String filename = "facebook_combined.txt";
        ArrayList<String> initialData = loadFile(filename);
        int iterationsPerTest = 10;
        //For each scenario
        pw = new PrintWriter(new File("adjMatTestResults.csv"));
        for (int i = 1; i <= 3; i++) {
            System.out.println("\nScenario " + i + "\n-----------------");
            //Adjacency Matrix
            System.out.println("Testing Adjacency Matrix\n-------------------");
            testGraph(initialData, i, 0.05f, iterationsPerTest, pw, "adjmat");
        }
        pw.close();
        pw = new PrintWriter(new File("indMatTestResults.csv"));
        for (int i = 1; i <= 3; i++) {
            System.out.println("\nScenario " + i + "\n-----------------");
            //Incidence Matrix
            System.out.println("\nTesting Incidence Matrix\n-------------------");
            testGraph(initialData, i, 0.05f, iterationsPerTest, pw, "indmat");
            
        }
        pw.close();
        System.out.println("\nI'm finished!!!");
    }
    
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
    
    public static void testGraph(ArrayList<String> initData, int scenario, float increment, int numTests, PrintWriter pw, String graphType) {
        //Create the spreadsheet column headings
        StringBuilder results = new StringBuilder();
        GraphData graphData = loadMatrix(initData, graphType, vertexRange);
        results.append("Scenario " + scenario + "\n");
        results.append("Density, ");
        results.append("Time lapsed");
        results.append("\n");
        for (float i = 0.05f; i <= 1.001f; i += increment) {
            double avgEstimatedTime = 0.0f;
            System.out.println(String.format("Graph Density: %.2f, ", i));
            increaseDensityTo(i, graphData);
            for (int j = 0; j < numTests; j++) {
                //Once each level of density is correct, add an arbitrary number of edges to the graph
                //Measure how long this takes for each density level using nanoTime
                //Generate test data
                String testData = DataGenerator.generateData(vertexRange, 100, scenario);
                //System.out.println(testData);
                long startTime = System.nanoTime();
                processOperations(testData);
                long deltaTime = System.nanoTime() - startTime;
                double estimatedTime = (double) deltaTime / Math.pow(10, 9);
                graphData = loadMatrix(initData, graphType, vertexRange);
                avgEstimatedTime += estimatedTime;
            }
            avgEstimatedTime /= numTests;
            System.out.println(String.format("Time lapsed: %.10f s", avgEstimatedTime));
            results.append(String.format("%.2f, ", i));
            results.append(String.format("%.10f", avgEstimatedTime));
            results.append("\n");
        }
        pw.write(results.toString());
    }

    public static void increaseDensityTo(float desiredDensity, GraphData graphData) {
        //Add random edges to the graph until the density is one of a range of values
        while (graphData.calcDensity() < desiredDensity) {
            //Randomly add edges until density = desiredDensity
            String v1 = DataGenerator.randomStrNum(0, vertexRange);
            String v2 = DataGenerator.randomStrNum(0, vertexRange);
            if (v1 != v2) {
                graph.addEdge(v1, v2);
                graphData.addEdge(v1, v2);
            }
        }
    }

    public void removeEdges(ArrayList<String[]> edges, GraphData graphData) {
        for (int i = 0; i < edges.size(); i++) {
            String vertA = edges.get(i)[0];
            String vertB = edges.get(i)[1];
            graph.removeEdge(vertA, vertB);
            graphData.removeEdge(vertA, vertB);
        }
    }
    
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
