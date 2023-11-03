import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class NeuralNetworkOptimization {
    public static void main(String[] args) {
        // Make space for the networks
        ArrayList<NeuralNetwork> networks = null;
        // Use the default file...
        String fileName = "networks.txt";
        if (args.length > 0) {
            // ...unless the user passes one in.
            fileName = args[0];
        }

        try {
            networks = ParseNetworks.fromFile(fileName);
        } catch (IOException e) {
            // Quit if there is a problem
            e.printStackTrace();
            System.exit(1);
        }

        for (NeuralNetwork network : networks) {
            //double[] optimalInput = network.findOptimalInput();
            // System.out.println("Optimal input for the network: " +
            // Arrays.toString(optimalInput));
            double[] networkOutput = network
                    .computeOutput(network.getExampleInput());
            System.out.println("Network output for the given example input: "
                    + Arrays.toString(networkOutput));
            System.out.println("Example output from file: "
                    + Arrays.toString(network.getExampleOutput()));

            // Uncomment the following lines to check if the computed output matches the example output
            // boolean match = network.compareOutputWithExample(networkOutput);
            // 
            // System.out.println("Does the computed output match the example output? " + match);
        }
    }
}
