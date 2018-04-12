public class DataGenerator {
    
    public static String generateData(int vertexRange, int edgesToEdit, int scenario) {
        StringBuilder testData = new StringBuilder();
        switch (scenario) {
            case 1: { 
                testData.append(scenario1(edgesToEdit, vertexRange));
                break;
            }
            case 2: {
                testData.append(scenario2(edgesToEdit, vertexRange));
                break;
            }
            case 3: {
                testData.append(scenario3(edgesToEdit, vertexRange));
                break;
            }
            default: {
                System.out.println("Invalid scenario. Choose [1, 2, 3]");
            }
        }
        return testData.toString();
    }
    
    public static String scenario1(int numEdgesToAdd, int vertexRange) {
        StringBuilder commandList = new StringBuilder();

        for (int i = 0; i < numEdgesToAdd; i++) {
            String v1 = randomStrNum(vertexRange, vertexRange * 2);
            String v2 = randomStrNum(vertexRange, vertexRange * 2);
            if (v1 != v2) {
                commandList.append("AV " + v1 + "\n");
                commandList.append("AV " + v2 + "\n");
                commandList.append("AE " + v1 + " " + v2 + "\n");
            }
        }
        return commandList.toString();
    }

    public static String scenario2(int vertexRange, int tests) {
        StringBuilder commandList = new StringBuilder();

        for (int i = 0; i < tests; i++) {
            String v1 = randomStrNum(0, vertexRange);
            String v2 = randomStrNum(0, vertexRange);
            if (v1 != v2) {
                commandList.append("N " + v1 + "\n");
                commandList.append("S " + v1 + " " + v2 + "\n");
            }
        }
        
        return commandList.toString();
    }

    public static String scenario3(int numEdgesToRemove, int vertexRange) {
        StringBuilder commandList = new StringBuilder();
        
        for (int i = 0; i < numEdgesToRemove; i++) {
            String v1 = randomStrNum(0, vertexRange);
            String v2 = randomStrNum(0, vertexRange);
            if (v1 != v2) {
                commandList.append("RE " + v1 + " " + v2 + "\n");
                commandList.append("RV " + v1 + "\n");
                commandList.append("RV " + v2 + "\n");
            }
        }
        
        return commandList.toString();

    }
    
    public static String randomStrNum(int min, int max) {
        int range = max - min;
        int num = (int) (Math.random() * range) + min;
        return ((Integer) num).toString();
    }

}

