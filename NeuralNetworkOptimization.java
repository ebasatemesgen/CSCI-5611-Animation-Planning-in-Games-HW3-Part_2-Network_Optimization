import java.io.FileWriter;
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

        // Attempt to open the file.
        try {
            networks = ParseNetworks.fromFile(fileName);
        } catch (IOException e) {
            // Quit if there is a problem
            e.printStackTrace();
            System.out.println("Error reading from file: " + fileName);
            System.exit(1);
        }

        // Attempt to write the solutions file.
        try {
            FileWriter outFile = new FileWriter("solutions.txt");
            int i;
            for (NeuralNetwork network : networks) {
                // Find the solution for each network.
                double[] optimum = network.cem();
                // Print the comma-separated results
                for (i = 0; i < optimum.length - 1; i++) {
                    outFile.write(optimum[i] + ", ");
                }
                outFile.write(optimum[i] + "\n");
            }
            outFile.close();
        } catch (IOException e) {
            // Quit if there is a problem
            e.printStackTrace();
            System.out.println("Error writing to file: solutions.txt");
            System.exit(2);
        }
    }
}
