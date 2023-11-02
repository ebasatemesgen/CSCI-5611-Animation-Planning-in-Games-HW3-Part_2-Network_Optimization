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

   @Override
   public String toString() {
      return "Layer { weights = " + Arrays.deepToString(weights) + ", biases = "
            + Arrays.toString(biases) + ", relu = " + relu + " }";
   }
}
