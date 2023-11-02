import java.io.*;
import java.util.*;

public class NeuralNetworkReader {
    public static void main(String[] args) {
        String fileName = "networks.txt"; // Replace with your file path
        try {
            ArrayList<NeuralNetwork> networks = readNetworksFromFile(fileName);
            for (NeuralNetwork network : networks) {
                System.out.println(network);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<NeuralNetwork> readNetworksFromFile(String fileName)
            throws IOException {
        ArrayList<NeuralNetwork> networks = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;
        NeuralNetwork network = null;
        boolean inNetwork = false;
        int layerCounter = 1;

        while ((line = reader.readLine()) != null) {
            line = line.trim(); // Trim the line to remove leading and trailing spaces
            if (line.isEmpty())
                continue; // Skip empty lines

            if (line.startsWith("Layers:")) {
                if (inNetwork) {
                    networks.add(network); // add the existing network to the list
                }
                network = new NeuralNetwork();
                inNetwork = true;
                layerCounter = 1; // Reset the layer counter for the new network
            } else if (line.startsWith("Rows " + layerCounter)) {
                int rows = Integer.parseInt(line.split(": ")[1]);
                reader.readLine();
                line = reader.readLine().trim(); // Read the weights line
                double[][] weights = parseWeights(line);
                line = reader.readLine().trim(); // Read the biases line
                double[] biases = parseBiases(line);
                boolean relu = Boolean
                        .parseBoolean(reader.readLine().split(": ")[1].trim());
                network.addLayer(new Layer(weights, biases, relu));
                layerCounter++; // Increment the layer counter
            }

        }
        if (inNetwork && network != null) {
            networks.add(network);
        }
        reader.close();
        return networks;
    }

    private static double[][] parseWeights(String weightsLine) {
        String[] rows = weightsLine.substring(weightsLine.indexOf("[[") + 2,
                weightsLine.lastIndexOf("]]")).split("\\], \\[");
        double[][] weights = new double[rows.length][];
        for (int i = 0; i < rows.length; i++) {
            weights[i] = Arrays.stream(rows[i].split(", "))
                    .mapToDouble(Double::parseDouble).toArray();
        }
        return weights;
    }

    private static double[] parseBiases(String biasesLine) {
        biasesLine = biasesLine.substring(biasesLine.indexOf("[[") + 2,
                biasesLine.lastIndexOf("]]"));
        return Arrays.stream(biasesLine.split("\\], \\["))
                .mapToDouble(Double::parseDouble).toArray();
    }
}
