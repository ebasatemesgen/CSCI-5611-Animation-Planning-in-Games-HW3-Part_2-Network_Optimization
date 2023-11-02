import java.io.*;
import java.util.*;
import java.util.regex.*;

public class NeuralNetworkOptimization {

    // A class representing a layer of the neural network
    public static class Layer {
        double[][] weights;
        double[] biases;
        boolean relu;

    public Layer(double[][] weights, double[] biases, boolean relu) {
        this.weights = weights;
        this.biases = biases;
        this.relu = relu;
    }

        // Method to compute the output of the layer
        public double[] computeOutput(double[] input) {
            double[] output = new double[biases.length];
            for (int i = 0; i < weights.length; i++) {
                for (int j = 0; j < input.length; j++) {
                    output[i] += weights[i][j] * input[j];
                }
                output[i] += biases[i];
                if (relu && output[i] < 0) {
                    output[i] = 0; // Applying ReLU activation function
                }
            }
            return output;
        }
    }

    // A class to represent the neural network
    public static class NeuralNetwork {
        List<Layer> layers = new ArrayList<>();
        
        double[] exampleOutput;

        public void addLayer(Layer layer) {
            layers.add(layer);
        }

        // Method to compute the output of the network
        public double[] computeOutput(double[] input) {
            double[] output = input;
            for (Layer layer : layers) {
                output = layer.computeOutput(output);
            }
            return output;
        }

        // Optimization algorithm to minimize the output magnitude
        public double[] findOptimalInput() {
            // For simplicity, use random search for optimization
            Random rand = new Random();
            double[] bestInput = new double[layers.get(0).weights[0].length];
            double bestMagnitude = Double.MAX_VALUE;
            for (int i = 0; i < 10000; i++) { // Perform 10000 random searches
                double[] input = new double[bestInput.length];
                for (int j = 0; j < input.length; j++) {
                    input[j] = rand.nextGaussian(); // Random Gaussian values for input
                }
                double[] output = computeOutput(input);
                double magnitude = Arrays.stream(output).map(Math::abs).sum(); // Sum of absolute values
                if (magnitude < bestMagnitude) {
                    bestMagnitude = magnitude;
                    bestInput = input;
                }
            }
            return bestInput;
        }

    // To set the example output
    public void setExampleOutput(double[] exampleOutput) {
        this.exampleOutput = exampleOutput;
    }

    // To Compare with Example Output from Our Network (I don't expect it to match but incase it does)
    public boolean compareOutputWithExample(double[] computedOutput) {
        return Arrays.equals(computedOutput, this.exampleOutput);
    }



    }
    public static void main(String[] args) {
        // Assume "networks.txt" is in the current working directory
        // Or provide the full path to the file
        String fileName = "networks.txt";
        try {
            List<NeuralNetwork> networks = parseNetworksFromFile(fileName);
            for (NeuralNetwork network : networks) {
                double[] optimalInput = network.findOptimalInput();
                // System.out.println("Optimal input for the network: " + Arrays.toString(optimalInput));
                double[] networkOutput = network.computeOutput(optimalInput);
                System.out.println("Network output for the optimal input: " + Arrays.toString(networkOutput));
                boolean match = network.compareOutputWithExample(networkOutput);
                            System.out.println("Example output from file: " + Arrays.toString(network.exampleOutput));
                // System.out.println("Does the computed output match the example output? " + match);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<NeuralNetwork> parseNetworksFromFile(String fileName) throws IOException {
        List<NeuralNetwork> networks = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;
        NeuralNetwork network = null;
        boolean inNetwork = false;

        while ((line = reader.readLine()) != null) {
            line = line.trim(); // Trim the line to remove leading and trailing spaces
            if (line.isEmpty()) continue; // Skip empty lines


            // System.out.println(line);
            if (line.startsWith("Layers:")) {
                if (inNetwork) {
                    networks.add(network); // add the existing network to the list
                }
                network = new NeuralNetwork();
                inNetwork = true;
                // layerIndex is not used beyond this point
            } else if (line.startsWith("Rows")) {
                // Skip the cols line, as we don't actually need it for the parsing
                reader.readLine();
                line = reader.readLine().trim(); // Read the weights line
                double[][] weights = parseWeights(line);
                line = reader.readLine().trim(); // Read the biases line
                double[] biases = parseBiases(line);
                boolean relu = Boolean.parseBoolean(reader.readLine().split(": ")[1].trim());
                network.addLayer(new Layer(weights, biases, relu));
            }
            if (line.startsWith("Example_Output:")) {
                double[] exampleOutput = parseExampleOutput(line);
                if (network != null) {
                    network.setExampleOutput(exampleOutput);
                }
            }
        }
        if (inNetwork && network != null) {
            networks.add(network); 
        }
        reader.close();
        return networks;
    }
    private static double[] parseExampleOutput(String line) {
        String outputStr = line.substring(line.indexOf(":") + 1).trim();
        return parseBiases(outputStr); // Same parsing logic as biases
    }
    private static double[][] parseWeights(String weightsLine) {
        String[] rows = weightsLine.substring(weightsLine.indexOf("[[") + 2, weightsLine.lastIndexOf("]]")).split("\\], \\[");
        double[][] weights = new double[rows.length][];
        for (int i = 0; i < rows.length; i++) {
            weights[i] = Arrays.stream(rows[i].split(", "))
                               .mapToDouble(Double::parseDouble)
                               .toArray();
        }
        return weights;
    }

    private static double[] parseBiases(String biasesLine) {
        biasesLine = biasesLine.substring(biasesLine.indexOf("[[") + 2, biasesLine.lastIndexOf("]]"));
        return Arrays.stream(biasesLine.split("\\], \\["))
                     .mapToDouble(Double::parseDouble)
                     .toArray();
    }
}


