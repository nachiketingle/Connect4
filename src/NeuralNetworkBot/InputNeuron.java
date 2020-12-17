package NeuralNetworkBot;

import BaseGame.Piece;

import java.util.ArrayList;

public class InputNeuron extends Neuron {

    int row;
    int col;

    public InputNeuron(int index) {
        super(0, index);
        this.outgoingEdges = new ArrayList<Edge>();
        this.incomingEdges = null;
        this.value = 0.5;
    }

    @Override
    public double calculateValue() {
        return value;
    }

    public void setValue(Piece[][] board, int row, int col) {

    }
}
