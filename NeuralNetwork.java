import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class NeuralNetwork {
   private ArrayList<Layer> layers;
   private double[] exampleOutput;
   private double[] exampleInput;

   public NeuralNetwork() { layers = new ArrayList<Layer>(); }

   public void addLayer(Layer layer) { layers.add(layer); }

   public void addLayer(double[][] weights, double[] biases, boolean relu) {
      layers.add(new Layer(weights, biases, relu));
   }

   // Method to compute the output (prediction) of the network
   public double[] computeOutput(double[] input) {
      double[] output = input;
      for (Layer layer : layers) {
         output = layer.computeOutput(output);
      }
      return output;
   }

   // To Compare with Example Output from Our Network (I don't expect it to match but incase it does)
   public boolean compareOutputWithExample(double[] computedOutput) {
      return Arrays.equals(computedOutput, this.exampleOutput);
   }

   // Optimization algorithm to minimize the output magnitude
   public double[] findOptimalInput() {
      // For simplicity, use random search for optimization
      Random rand = new Random();
      double[] bestInput = new double[layers.get(0).getNodeCount()];
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

   public double[] getExampleOutput() { return this.exampleOutput; }

   public double[] getExampleInput() { return this.exampleInput; }

   public void learn(double[] expected, double[] input, double alpha) {
      ArrayList<double[]> inputs = new ArrayList<double[]>();
      inputs.add(input); // The original input needs to be in the rotation...
      double[] bias = new double[input.length];
      Arrays.fill(bias, 0.0);
      for (int i = 0; i < layers.size() - 1; i++) {
         inputs.add(layers.get(i).computeOutput(inputs.get(i)));
      }
      for (int i = inputs.size(); i >= 0; i--) {
         bias = this.layers.get(i).learn(expected, inputs.get(i), bias, alpha);
      }
   }

   public void setExampleInput(double[] exampleInput) {
      this.exampleInput = exampleInput;
   }

   public void setExampleOutput(double[] exampleOutput) {
      this.exampleOutput = exampleOutput;
   }

   @Override
   public String toString() {
      return "NeuralNetwork { layers = " + layers + " }";
   }
}
