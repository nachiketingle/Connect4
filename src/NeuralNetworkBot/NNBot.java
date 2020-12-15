package NeuralNetworkBot;

import BaseGame.Bot;
import BaseGame.Piece;


public class NNBot extends Bot {

    InputNeuron[] inputNeurons;
    Neuron[][] allNeurons;


    NNBot() {
        inputNeurons = new InputNeuron[6 * 7];
        allNeurons = new Neuron[4][];
    }

    @Override
    public int playPiece(Piece[][] origBoard) {
        return 0;
    }

    private void loadNetworkFromFile(String filename) {

    }

    private void saveNetworkToFile(String filename) {

    }
}
