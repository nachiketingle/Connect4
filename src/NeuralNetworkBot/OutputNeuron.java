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

}
