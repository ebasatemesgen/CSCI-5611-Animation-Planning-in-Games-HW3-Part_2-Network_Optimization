import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ParseNetworks {
   public static ArrayList<NeuralNetwork> fromFile(String fileName)
         throws IOException {
      ArrayList<NeuralNetwork> networks = new ArrayList<>();
      BufferedReader reader = new BufferedReader(new FileReader(fileName));
      String line;
      NeuralNetwork network = null;
      boolean inNetwork = false;

      while ((line = reader.readLine()) != null) {
         line = line.trim(); // Trim the line to remove leading and trailing spaces
         if (line.isEmpty())
            continue; // Skip empty lines

         // System.out.println(line);
         if (line.startsWith("Layers:")) {
            if (inNetwork) {
               networks.add(network); // add the existing network to the ArrayList
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
            boolean relu = Boolean
                  .parseBoolean(reader.readLine().split(": ")[1].trim());
            network.addLayer(new Layer(weights, biases, relu));
         }
         if (line.startsWith("Example_Output:")) {
            double[] exampleOutput = parseExampleOutput(line);
            if (network != null) {
               network.setExampleOutput(exampleOutput);
            }
         }

         if (line.startsWith("Example_Input:")) {
            double[] exampleInput = parseExampleInput(line);
            if (network != null) {
               network.setExampleInput(exampleInput);
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

   private static double[] parseExampleInput(String line) { // Just a duplicate of the above method (Could be removed)
      String inputStr = line.substring(line.indexOf(":") + 1).trim();
      return parseBiases(inputStr);
   }

   // Parses a 2D array of weights from a line in the file.
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

   // Parses a 1D array of biases from a line in the file.
   private static double[] parseBiases(String biasesLine) {
      biasesLine = biasesLine.substring(biasesLine.indexOf("[[") + 2,
            biasesLine.lastIndexOf("]]"));
      return Arrays.stream(biasesLine.split("\\], \\["))
            .mapToDouble(Double::parseDouble).toArray();
   }
}
