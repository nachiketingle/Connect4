package NeuralNetworkBot;

import BaseGame.Bot;
import BaseGame.Piece;

import java.io.*;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


public class NNBot extends Bot {

    InputNeuron[] inputNeurons;
    OutputNeuron[] outputNeurons;
    Neuron[][] allNeurons;
    ArrayList<Node> moves;
    String file;

    public NNBot(boolean isYellow, String file) {
        this.isYellow = isYellow;
        this.file = file;
        createNetwork();
        moves = new ArrayList<>();
        //System.out.println(outputNeurons[0].calculateValue());
    }

    @Override
    public int playPiece(Piece[][] origBoard) {
        System.out.println();
        System.out.println("NNBot Playing");
        Piece[][] board;


        // Set the values
        loadInputNeurons(origBoard, isYellow);

        // Calculate values
        double value, maxValue = Double.MIN_VALUE;
        int column = 1;
        OutputNeuron n;
        for(int i = 0; i < outputNeurons.length; i++) {
            n = outputNeurons[i];
            board = copyBoard(origBoard);
            if(addPiece(board, n.column, new Piece(isYellow)) == -1)
                continue;

            value = n.calculateValue();
            System.out.println("Value: " + value + "\tColumn: " + n.column);
            if(value > maxValue) {
                maxValue = value;
                column = n.column;
            }
        }

        moves.add(new Node(copyBoard(origBoard), column));

        System.out.println("Max Value: " + maxValue);
        System.out.println("Column: " + column);

        return column;
    }

    @Override
    public void updateWin(boolean yellowWin) {
        System.out.println("Is Yellow: " + isYellow + "\t\tWin: " + (yellowWin == isYellow));
        for(Node move : moves) {
            // Set the values
            loadInputNeurons(move.board, isYellow);

            OutputNeuron n = outputNeurons[move.column - 1];
            n.backpropogation((yellowWin == isYellow) ? 1 : 0);
        }
        try {
            saveNetworkToFile(file);
        } catch (Exception ignored) {

        }
    }

    private void loadInputNeurons(Piece[][] board, boolean isYellow) {
        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board[i].length; j++) {
                inputNeurons[i * j + j].setValue(board, i, j);
            }
        }

        // The last neuron represents whose turn it is
        inputNeurons[inputNeurons.length - 1].value = isYellow ? 1 : -1;
    }

    private void createNetwork() {
        // Initialize arrays
        inputNeurons = new InputNeuron[6 * 7 + 1];
        outputNeurons = new OutputNeuron[7];
        allNeurons = new Neuron[4][];
        allNeurons[0] = inputNeurons;
        allNeurons[allNeurons.length - 1] = outputNeurons;

        // Hidden layers
        allNeurons[1] = new Neuron[10];
        allNeurons[2] = new Neuron[10];

        // Create the input neurons
        for(int i = 0; i < inputNeurons.length; i++) {
            inputNeurons[i] = new InputNeuron(i);
        }

        // Create the hidden layer neurons
        for(int i = 1; i < allNeurons.length - 1; i++) {
            for(int j = 0; j < allNeurons[i].length; j++) {
                allNeurons[i][j] = new Neuron(i, j);
            }
        }

        // Create the output neurons
        for(int i = 0; i < outputNeurons.length; i++) {
            outputNeurons[i] = new OutputNeuron(i + 1, allNeurons.length - 1);
        }

        try {
            loadNetworkFromFile(file);
            //saveNetworkToFile("testFile2.csv");
        } catch (Exception e) {
            System.out.println("An error occurred");
        }
    }

    private void loadNetworkFromFile(String filename) {
        try {
            // File format: layer-index, layer-index, weight
            BufferedReader csvReader = new BufferedReader(new FileReader(filename));
            System.out.println("Loading data from file: " + filename);
            String row;
            String[] data, left, right;
            int[] leftVals = new int[2];
            int[] rightVals = new int[2];
            double weight;
            Neuron leftNeuron, rightNeuron;
            while ((row = csvReader.readLine()) != null) {
                if (row.length() == 0)
                    break;

                // Get the edge data
                data = row.split(",");

                // Left neuron
                left = data[0].split("-");
                leftVals[0] = Integer.parseInt(left[0]);
                leftVals[1] = Integer.parseInt(left[1]);
                leftNeuron = allNeurons[leftVals[0]][leftVals[1]];

                // Right neuron
                right = data[1].split("-");
                rightVals[0] = Integer.parseInt(right[0]);
                rightVals[1] = Integer.parseInt(right[1]);
                rightNeuron = allNeurons[rightVals[0]][rightVals[1]];

                // Edge weight
                weight = Double.parseDouble(data[2]);

                Edge e = new Edge(leftNeuron, rightNeuron);
                e.weight = weight;
            }
            csvReader.close();

        } catch (Exception exception) {
            System.out.println(exception.toString());
            System.out.println("Unable to open/find file: " + filename);
            // If unable to open/find a file, use random weight with values from -1, 1
            Random random = new Random();
            Neuron[] firstLayer, secondLayer;
            Neuron firstNeuron, secondNeuron;
            Edge e;
            for (int layer = 0; layer < allNeurons.length - 1; layer++) {
                firstLayer = allNeurons[layer];
                secondLayer = allNeurons[layer + 1];
                for (int i = 0; i < firstLayer.length; i++) {
                    firstNeuron = firstLayer[i];
                    for (int j = 0; j < secondLayer.length; j++) {
                        secondNeuron = secondLayer[j];
                        e = new Edge(firstNeuron, secondNeuron);
                        e.weight = random.nextDouble() * 2 - 1;
                    }
                }
            }
        }
    }

    /**
     * Writes our network to the given file
     * @param filename String of file to write the network to
     * @throws IOException An error occurs with the file
     */
    private void saveNetworkToFile(String filename) throws IOException {

        // File format: layer-index, layer-index, weight

        FileWriter csvWriter = new FileWriter(filename);
        ArrayList<Edge> edges;
        Neuron left, right;
        for(int layer = 0; layer < allNeurons.length - 1; layer++) {
            for(int i = 0; i < allNeurons[layer].length; i++) {
                left = allNeurons[layer][i];
                edges = left.outgoingEdges;
                for(Edge e : edges) {
                    right = e.rightNeuron;
                    csvWriter.append(left.toEdgeString());
                    csvWriter.append(",");
                    csvWriter.append(right.toEdgeString());
                    csvWriter.append(",");
                    csvWriter.append(String.valueOf(e.weight));
                    csvWriter.append("\n");
                }
            }
            csvWriter.flush();
        }
        csvWriter.close();
    }
}
