import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class NeuralNetwork {
    private final int numOfNeuron;
    private final Neuron[] neurons;
    private final double learningCoefficient;
    private final String[] species;

    public NeuralNetwork(int numOfNeuron, int numOfInputs, double learningCoefficient, String[] outputList) {
        this.numOfNeuron = numOfNeuron;
        this.neurons = new Neuron[numOfNeuron];
        this.learningCoefficient = learningCoefficient;
        this.species = outputList;
        for (int i = 0; i < numOfNeuron; i++){
            neurons[i] = new Neuron(numOfInputs);
        }
    }

    public double[][] Normalisation(ArrayList<String[]> inputs){
        double[][] normalizedInputs = new double[inputs.size()][inputs.get(0).length - 1];

        for (int i = 0; i < inputs.size(); i++){
            for (int j = 0; j < inputs.get(i).length - 1; j++){
                normalizedInputs[i][j] = (Double.valueOf(inputs.get(i)[j])) / 10;
            }
        }
        return normalizedInputs;
    }

    public String[] createOutputList(ArrayList<String[]> inputs){
        String[] outputList = new String[inputs.size()];
        for (int i = 0; i < inputs.size(); i++){
            outputList[i] = inputs.get(i)[inputs.get(i).length-1 ];
        }
        return outputList;
    }

    public void train(double[][] normalizedInputs, ArrayList<String[]> inputs, String[] expectedOutputs) {
        for (int i = 0; i < inputs.size(); i++) {
            double[] neuronOutputs = new double[numOfNeuron];
            for (int j = 0; j < numOfNeuron; j++) {
                neuronOutputs[j] = neurons[j].calculateOutput(normalizedInputs[i]);
            }

            double maxOutput = -1;
            int maxIndex = -1;
            for (int k = 0; k < numOfNeuron; k++) {
                if (neuronOutputs[k] > maxOutput) {
                    maxOutput = neuronOutputs[k];
                    maxIndex = k;
                }
            }

            if(!expectedOutputs[i].equals(species[maxIndex])){
                neurons[maxIndex].decreaseWeight(normalizedInputs[i], learningCoefficient);
                for(int a = 0; a < numOfNeuron; a++){
                    if(expectedOutputs[i].equals(species[a])){
                        neurons[a].increaseWeight(normalizedInputs[i], learningCoefficient);
                    }
                }
            }
        }
    }

    public String calculateAccuracy(double[][] normalizedInputs, ArrayList<String[]> inputs, String[] expectedOutputs){
        double correctPredict = 0;
        for (int i = 0; i < inputs.size(); i++) {
            double[] neuronOutputs = new double[numOfNeuron];
            for (int j = 0; j < numOfNeuron; j++) {
                neuronOutputs[j] = neurons[j].calculateOutput(normalizedInputs[i]);
            }

            double maxOutput = -1;
            int maxIndex = -1;
            for (int k = 0; k < numOfNeuron; k++) {
                if (neuronOutputs[k] > maxOutput) {
                    maxOutput = neuronOutputs[k];
                    maxIndex = k;
                }
            }
            if(expectedOutputs[i].equals(species[maxIndex])){
                correctPredict += 1;
            }
        }
        double accuracy = 100 * (correctPredict / inputs.size());
        String formattedAccuracy = String.format("%.2f ", accuracy);;
        return formattedAccuracy;
    }

    public static void qetAccuracy(ArrayList<String[]> inputs, int numOfNeuron, int numOfInputs, double learningCoefficient, String[] species, int epoch){
        NeuralNetwork neuralNetwork = new NeuralNetwork(numOfNeuron, numOfInputs, learningCoefficient, species);
        double[][] normalizedInputs = neuralNetwork.Normalisation(inputs);
        String[] expectedOutputs = neuralNetwork.createOutputList(inputs);

        for (int i = 0; i < epoch; i++){
            neuralNetwork.train(normalizedInputs, inputs, expectedOutputs);
        }
        String accuracy = neuralNetwork.calculateAccuracy(normalizedInputs, inputs, expectedOutputs);
        System.out.print(String.format("|   " + accuracy + " "));
    }

    public static void printAccuracyTable(double[] learningCoefficients, int[] epochs, ArrayList<String[]> inputs, String[] species) {
        System.out.print("     ");
        for (int i = 0; i < epochs.length; i++){
            if (epochs[i] > 99){
                System.out.print("| " + epochs[i] + " epoch");
            }else{
                System.out.print("| " + epochs[i] + " epoch ");
            }
        }
        System.out.println("|");
        for (double learningCoefficient : learningCoefficients) {
            if (learningCoefficient == 0.01){
                System.out.print(learningCoefficient + "0");
            }else{
                System.out.print(learningCoefficient);
            }
            for (int epoch : epochs) {
                NeuralNetwork.qetAccuracy(inputs, 3, 4, learningCoefficient, species, epoch); //numOf ları species.length ve inputs[0].length diye vermeliyim dimi
            }
            System.out.println("|");
        }
    }

    public static void main(String[] args) throws Exception {
        File file = new File(".\\iris.data");
        Scanner sc = new Scanner(file);
        ArrayList<String[]> inputs = new ArrayList<>();
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            inputs.add( line.split(","));
        }

        String[] species = {"Iris-setosa", "Iris-versicolor", "Iris-virginica"};

        double[] learningCoefficients = {0.005, 0.010, 0.025};
        int[] epochs = {20, 50, 100};

        printAccuracyTable(learningCoefficients, epochs, inputs, species);
    }
}