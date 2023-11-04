import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

   // Referencing from the wiki article:
   // https://en.wikipedia.org/wiki/Cross-entropy_method
   public double[] cem() {
      Random r = new Random();

      // Simulation Parameters
      int maxIters = 10000;
      int iter = 0;
      double epsilon = 0.00001; // How close should our variance get?
      int sampleSize = 100;
      int keep = 10; // Of the best samples

      // Data Structures
      double[] bestMeans = new double[layers.get(0).getNodeCount()];
      Arrays.fill(bestMeans, 0.0);
      double[] variance = new double[bestMeans.length];
      Arrays.fill(variance, keep);
      double[][] samples = new double[sampleSize][bestMeans.length];
      ArrayList<Double> magnitudes;

      // Variables for intermediary math
      double[] temp;
      int minIndex;
      double meanSum, varSum;
      double varTotal = Double.MAX_VALUE;
      double oldVarTotal = Double.MIN_VALUE;

      // While we aren't bored and there are still changes in variance...
      while (iter < maxIters && Math.abs(varTotal - oldVarTotal) > epsilon) {
         magnitudes = new ArrayList<Double>();
         for (int i = 0; i < samples.length; i++) {
            // Generate random inputs from a gaussian distribution.
            for (int j = 0; j < bestMeans.length; j++) {
               samples[i][j] = r.nextGaussian() * Math.sqrt(variance[j])
                     + bestMeans[j];
            }
            // Calculate how far away we are from the goal of an all zero output.
            magnitudes.add(Arrays.stream(this.computeOutput(samples[i]))
                  .map(Math::abs).sum()); // Sum of absolute values
         }
         // Do some sorting for the top performers.
         for (int i = 0; i < keep; i++) {
            minIndex = magnitudes.indexOf(Collections.min(magnitudes));
            temp = samples[minIndex];
            samples[minIndex] = samples[i];
            samples[i] = temp;
            magnitudes.set(minIndex, magnitudes.get(i));
            magnitudes.set(i, Double.MAX_VALUE);
         }
         // Keep track of the total change.
         oldVarTotal = varTotal;
         varTotal = 0;
         // Find the means and variance of the best results.
         for (int i = 0; i < bestMeans.length; i++) {
            meanSum = 0;
            varSum = 0;
            for (int j = 0; j < keep; j++) {
               meanSum += samples[j][i];
            }
            bestMeans[i] = meanSum / keep;
            for (int j = 0; j < keep; j++) {
               varSum += Math.pow(samples[j][i] - bestMeans[i], 2);
            }
            variance[i] = varSum / keep;
            varTotal += variance[i];
         }
         iter++;
      }
      return bestMeans;
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

   public double[] getExampleOutput() { return this.exampleOutput; }

   public double[] getExampleInput() { return this.exampleInput; }

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
