package MinmaxBot;

import BaseGame.Piece;

import java.util.ArrayList;

public class CalculatedBoards {
    final int arrSize = 10000;
    ArrayList<Node>[] arr = new ArrayList[arrSize];

    class Node {
        Piece[][] board;
        long score;
        Node(Piece[][] board) {
            this.board = board;
        }
    }

    public long getScore(Piece[][] board) throws Exception {
        ArrayList<Node> list = arr[hashBoard(board) % arrSize];
        if(list == null) {
            //System.out.println("No list found");
            throw new Exception();
        }

        //System.out.println("List Size: " + list.size());
        int count = 0;
        Node currNode;
        Piece[][] currBoard;
        boolean foundBoard;
        for (Node node : list) {
            count++;
            // Find the corresponding board
            foundBoard = true;
            currNode = node;
            currBoard = currNode.board;

            // Check if the boards are the same
            for (int row = 0; row < currBoard.length; row++) {
                for (int col = 0; col < currBoard[row].length; col++) {
                    if(board[row][col] == null || currBoard[row][col] == null) {
                        if(board[row][col] == null && currBoard[row][col] == null)
                            continue;
                        else {
                            foundBoard = false;
                            break;
                        }
                    }
                    else if (board[row][col].val != currBoard[row][col].val) {
                        foundBoard = false;
                        break;
                    }
                }
                if (!foundBoard)
                    break;
            }

            // If we found the correct board, return the score
            if (foundBoard) {
                //System.out.println("Found a score: " + currNode.score + "\tafter: " + count + "\tsize: " + list.size());
                return currNode.score;
            }
        }
        //System.out.println("No board found");
        throw new Exception();
    }

    public void addBoard(Piece[][] board, long score) {
        int hash = hashBoard(board);
        int index = hash % arrSize;
        ArrayList<Node> list = arr[index];
        if(list == null) {
            arr[index] = new ArrayList<>();
            list = arr[index];
        }

        Node node = new Node(board);
        node.score = score;
        list.add(node);
        //System.out.println("List Size: " + list.size());
    }

    private int hashBoard(Piece[][] board) {
        int hash = 0;
        for(int row = 0; row < board.length; row++) {
            for(int col = 0; col < board[0].length; col++){
                hash += board[row][col] == null ? row * col : (board[row][col].val + 2) * col * 31 + Math.pow(row, 6);
            }
        }
        //System.out.println("Hash: " + hash);
        return hash;
    }

}
