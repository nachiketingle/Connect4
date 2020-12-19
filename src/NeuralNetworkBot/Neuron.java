package NeuralNetworkBot;

import java.util.ArrayList;

public class Neuron {
    static final double learningRate = 0.1;

    int layer;                          // Layer Neuron is in
    int index;                          // Index of the layer that neuron is in
    double value;                       // Value of the neuron
    ArrayList<Edge> incomingEdges;      // All incoming edges coming in from the left
    ArrayList<Edge> outgoingEdges;      // All outgoing edges going out from the right

    Neuron(int layer, int index) {
        this.layer = layer;
        this.index = index;
        incomingEdges = new ArrayList<>();
        outgoingEdges = new ArrayList<>();
    }

    public double calculateValue() {
        value = 0;
        for(Edge e : incomingEdges) {
            value += e.weight * e.leftNeuron.calculateValue();
        }
        value = 1 / (1 + Math.exp(-value));
        return value;
    }

    public String toEdgeString() {
        return layer + "-" + index;
    }

    public void backpropogation(double expected) {
        Edge e;
        double error = expected * derivative(value);
        for(int i = 0; i < incomingEdges.size(); i++) {
            e = incomingEdges.get(i);
            e.weight = e.weight + learningRate * error * e.leftNeuron.value;
            e.leftNeuron.backpropogation(error);
        }
    }

    public static double derivative(double output) {
        return output * (1 - output);
    }

}
