public class Neuron  {
    private double[] weights;

    public Neuron(int numOfWeights) {
        this.weights = new double[numOfWeights];
        for (int i = 0; i < numOfWeights; i++){
            this.weights[i] = Math.random();
        }
    }

    public double calculateOutput(double[] inputs){
        double output = 0;
        for (int i = 0; i < inputs.length; i++){
            output  += inputs[i] * weights[i];
        }
        return output;
    }

    public void increaseWeight(double[] inputs, double learningCoefficient){
        for (int i = 0; i < inputs.length; i++){
            this.weights[i] += learningCoefficient * inputs[i];
        }
    }

    public void decreaseWeight(double[] inputs, double learningCoefficient){
        for (int i = 0; i < inputs.length; i++){
            this.weights[i] -= learningCoefficient * inputs[i];
        }
    }
}