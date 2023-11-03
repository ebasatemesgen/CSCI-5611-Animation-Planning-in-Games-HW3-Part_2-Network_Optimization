import java.io.IOException;
import java.util.ArrayList;

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
            //// Part 1
            // double[] networkOutput = network
            //         .computeOutput(network.getExampleInput());
            // System.out.println("Network output for the given example input: "
            //         + Arrays.toString(networkOutput));
            // System.out.println("Example output from file: "
            //         + Arrays.toString(network.getExampleOutput()));

            // Uncomment the following lines to check if the computed output matches the example output
            // boolean match = network.compareOutputWithExample(networkOutput);
            // 
            // System.out.println("Does the computed output match the example output? " + match);

            double[] optimalInput = network.findOptimalInput();
            System.out.println("Optimal input from Random Search: [");
            for (double val : optimalInput) {
                System.out.println("\t" + val);
            }
            System.out.println("]");

            double[] optimum = network.cem();
            System.out.println("Optimal input from Gaussian: [");
            for (double val : optimum) {
                System.out.println("\t" + val);
            }
            System.out.println("]");

            System.out.println("Vector length from Random Search: "
                    + vectorLength(network.computeOutput(optimalInput)));
            System.out.println("Vector length from Gaussian: "
                    + vectorLength(network.computeOutput(optimum)));
        }
    }

    public static double vectorLength(double[] vec) {
        double sum = 0;
        for (double x : vec) {
            sum += x * x;
        }
        return Math.sqrt(sum);
    }
}
