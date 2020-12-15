package NeuralNetworkBot;

import java.util.ArrayList;

public class OutputNeuron extends Neuron {

    int column;

    OutputNeuron(int column, int layer) {
        this.column = column;
        this.layer = layer;
        this.incomingEdges = new ArrayList<Edge>();
    }

}
