package NeuralNetworkBot;

import java.util.ArrayList;

public class OutputNeuron extends Neuron {

    int column;

    OutputNeuron(int column, int layer) {
        super(layer, column - 1);
        this.column = column;
        this.incomingEdges = new ArrayList<Edge>();
        this.outgoingEdges = null;
    }

    public void backpropogation(double expected) {
        Edge e;
        double error = (expected - value) * derivative(value);
        for(int i = 0; i < incomingEdges.size(); i++) {
            e = incomingEdges.get(i);
            e.weight = e.weight + learningRate * error * e.leftNeuron.value;
            e.leftNeuron.backpropogation(e.weight * error);
        }
    }

}
