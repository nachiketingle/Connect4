package NeuralNetworkBot;

import BaseGame.Piece;

public class Node {
    Piece[][] board;
    int column;

    Node(Piece[][] board, int column) {
        this.board = board;
        this.column = column;
    }
}
