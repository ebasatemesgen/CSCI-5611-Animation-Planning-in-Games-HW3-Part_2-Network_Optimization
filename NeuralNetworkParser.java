import java.util.Arrays;

public class NeuralNetworkParser {

    public static void main(String[] args) {
        String weightsLine = "[[-4.0, -5.0, 1.0, -2.0], [-5.0, 3.0, 0.0, -9.0], [-4.0, 9.0, -4.0, 5.0], [3.0, 5.0, 9.0, -2.0], [-8.0, -6.0, 1.0, 6.0]]";
        String biasesLine = "[[-3.0], [-7.0], [6.0], [-9.0], [9.0]]";

        double[][] weights = parseWeights(weightsLine);
        double[] biases = parseBiases(biasesLine);

        System.out.println("Weights: " + Arrays.deepToString(weights));
        System.out.println("Biases: " + Arrays.toString(biases));
    }

    private static double[][] parseWeights(String weightsLine) {
        String[] rows = weightsLine.replaceAll("\\[\\[", "")
                                   .replaceAll("\\]\\]", "")
                                   .split("\\], \\[");
        double[][] weights = new double[rows.length][];
        for (int i = 0; i < rows.length; i++) {
            weights[i] = Arrays.stream(rows[i].split(", "))
                               .mapToDouble(Double::parseDouble)
                               .toArray();
        }
        return weights;
    }

    private static double[] parseBiases(String biasesLine) {
        return Arrays.stream(biasesLine.replaceAll("\\[\\[", "")
                                       .replaceAll("\\]\\]", "")
                                       .split("\\], \\["))
                     .mapToDouble(Double::parseDouble)
                     .toArray();
    }
}
