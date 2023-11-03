import java.util.Arrays;

public class Layer {
   private double[][] weights;
   private double[] biases;
   private boolean relu;

   public Layer(double[][] weights, double[] biases, boolean relu) {
      this.weights = weights;
      this.biases = biases;
      this.relu = relu;
   }

   public int getNodeCount() { return weights[0].length; }

   // Method to compute the output of the layer
   // Uses either ReLU or no activation function
   public double[] computeOutput(double[] input) {
      double[] output = new double[biases.length];
      for (int i = 0; i < weights.length; i++) {
         for (int j = 0; j < input.length; j++) {
            output[i] += weights[i][j] * input[j];
         }
         output[i] += biases[i];
         // If we are supposed to do ReLU...
         if (relu && output[i] < 0) {
            output[i] = 0; // Applying ReLU activation function
         }
      }
      return output;
   }

   public double[] learn(double[] expected, double[] input, double[] bias,
         double alpha) {
      double diff;
      double[] predicted = this.computeOutput(input);
      for (int i = 0; i < weights.length; i++) {
         diff = expected[i] - predicted[i];
         for (int j = 0; j < input.length; j++) {
            bias[j] += diff * input[j];
         }
      }
      for (int i = 0; i < weights.length; i++) {
         for (int j = 0; j < bias.length; j++) {
            weights[i][j] += alpha * bias[j];
         }
      }
      return bias;
   }

   @Override
   public String toString() {
      return "Layer { weights = " + Arrays.deepToString(weights) + ", biases = "
            + Arrays.toString(biases) + ", relu = " + relu + " }";
   }
}
