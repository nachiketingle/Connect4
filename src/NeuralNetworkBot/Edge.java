package NeuralNetworkBot;

public class Edge {
    Neuron leftNeuron;
    Neuron rightNeuron;
    double weight;

    Edge(Neuron leftNeuron, Neuron rightNeuron) {
        this.leftNeuron = leftNeuron;
        this.rightNeuron = rightNeuron;
        leftNeuron.outgoingEdges.add(this);
        rightNeuron.incomingEdges.add(this);
    }

}
