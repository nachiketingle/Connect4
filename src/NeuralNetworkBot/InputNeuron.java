package NeuralNetworkBot;

import BaseGame.Piece;

import java.util.ArrayList;

public class InputNeuron extends Neuron {

    int row;
    int col;

    InputNeuron(int row, int col, Piece piece) {
        this.row = row;
        this.col = col;
        layer = 0;
        if(piece == null) {
            value = 0;
        }
        else {
            value = piece.isYellow ? 1 : 0.5;
        }

        this.outgoingEdges = new ArrayList<Edge>();
    }

}
