package MinmaxBot;

import BaseGame.Helpers;
import BaseGame.Piece;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Random;

public class Bot {
    public boolean isYellow;
    Random rand = new Random();
    static final int maxDepth = 8;
    int largestHeight, maxHeight;

    class Move {
        int depth;
        int col;
        Move(int col, int depth) {
            this.depth = depth;
            this.col = col;
        }
    }

    public Bot() {
        isYellow = true;
    }

    public Bot(boolean _isYellow) {
        this.isYellow = _isYellow;
    }

    public int playPiece(Piece[][] origBoard) {
        Piece[][] board = copyBoard(origBoard);
        largestHeight = 0;
        Instant start = Instant.now();
        Move move = playMinMax(board, maxDepth);
        Instant end = Instant.now();

        if(move.col == -1) {
            System.out.println("Choosing randomly");
            return rand.nextInt(board[0].length) + 1;
        }
        System.out.println();
        System.out.println("Column: " + move.col);
        System.out.println("Time Spent Calculating: " + Duration.between(start, end));
        System.out.println("Deepest Path: " + largestHeight);
        return move.col;
    }

    private Move playMinMax(Piece[][] origBoard, int maxDepth) {
        int col;
        // Check for an immediate win
        col = immediateWin(origBoard, isYellow);
        if(col > 0) {
            System.out.println("\nFound immediate win");
            return new Move(col, maxDepth);
        }

        // Check to stop a win
        col = immediateWin(origBoard, !isYellow);
        if(col > 0) {
            System.out.println("\nFound immediate loss");
            return new Move(col, maxDepth);
        }


        // Get a copy of the board
        Piece[][] board = copyBoard(origBoard);
        ArrayList<Integer> cols = new ArrayList<>();
        for(int i = 1; i <= board[0].length; i++) {
            cols.add(i);
        }


        CalculatedBoards boards = new CalculatedBoards();
        long bestScore = isYellow ? Long.MIN_VALUE : Long.MAX_VALUE, score;
        Move bestMove = null;
        System.out.println();
        while(!cols.isEmpty()) {
            col = cols.remove(rand.nextInt(cols.size()));
            board = copyBoard(origBoard);
            Piece piece = new Piece(isYellow);
            if(addPiece(board, col, piece) == -1) {
                continue;
            }

            maxHeight = -1;

            Instant start = Instant.now();
            score = minMax(board, maxDepth, bestScore, !isYellow, boards);
            Instant end = Instant.now();
            System.out.println("Score: " + score + "\tCol: " + col + "\tTime: " + Duration.between(start, end));
            if(isYellow && bestScore < score) {
                System.out.println("New Best for Yellow!");
                bestScore = score;
                bestMove = new Move(col, maxDepth);
            }
            else if(!isYellow && bestScore > score) {
                System.out.println("New Best for Red!");
                bestScore = score;
                bestMove = new Move(col, maxDepth);
            }
        }
        System.out.println("Best Score: " + bestScore);
        return bestMove == null ? new Move(-1, maxDepth) : bestMove;
    }

    private long minMax(Piece[][] origBoard, int depth, long prevBest, boolean isYellow, CalculatedBoards boards) {
        if(largestHeight < maxDepth - depth)
            largestHeight = maxDepth - depth;

        if(depth == 0) {
            return 0;
        }


        long score = getScore(depth, isYellow);
        /*if((!isYellow && score < prevBest) || (isYellow && score > prevBest)) {
            return 0;
        }

         */



        Piece[][] board = copyBoard(origBoard);
        int col;

        // Attempt to get score from calculated boards
        try {
            score = boards.getScore(board);
            //System.out.println("Score found at depth: " + depth);
            return score;
        } catch (Exception ignored) { }

        // Check for an immediate win
        col = immediateWin(board, isYellow);
        if(col > 0) {
            return getScore(depth, isYellow);
        }

        long bestScore = isYellow ? Long.MIN_VALUE : Long.MAX_VALUE;
        for(col = 1; col <= board[0].length; col++ ) {
            board = copyBoard(origBoard);
            Piece piece = new Piece(isYellow);
            if(addPiece(board, col, piece) == -1) {
                continue;
            }
            //score = minMax(board, depth - 1, !isYellow);

            // Add the new board to the calculated ones
            score = minMax(board, depth - 1, bestScore, !isYellow, boards);
            boards.addBoard(board, score);

            if(isYellow && bestScore < score) {
                bestScore = score;
            }
            else if(!isYellow && bestScore > score) {
                bestScore = score;
            }

        }

        return bestScore;
    }

    private long getScore(int depth, boolean isYellow) {
        /*(long) Math.pow(depth, maxDepth + 1)*/
        return (long) depth * (isYellow ? 1 : -1);
    }

    /**
     * Returns a copy of the given board.
     * @param board Board to copy from
     * @return Given board
     */
    private Piece[][] copyBoard(Piece[][] board) {
        Piece[][] newBoard = new Piece[board.length][];
        for(int i = 0; i < board.length; i++) {
            newBoard[i] = new Piece[board[i].length];
            for(int j = 0; j < board[i].length; j++) {
                newBoard[i][j] = board[i][j];
            }
        }

        return newBoard;
    }

    /**
     * Checks if adding a piece of given color can cause a win
     * @param origBoard Board to add piece to
     * @param isYellow  Color of piece to add
     * @return  Column in which to add piece for immediate win. -1 if no such column exists
     */
    private int immediateWin(Piece[][] origBoard, boolean isYellow) {
        Piece piece = new Piece(isYellow);
        Piece[][] board;
        int row;

        for(int col = 1; col <= origBoard[0].length; col++) {
            board = copyBoard(origBoard);
            if((row = addPiece(board, col, piece)) >= 0) {
                if(Helpers.checkWinner(board, row, col-1)) {
                    return col;
                }
            }
        }
        return -1;
    }

    /**
     * Adds the piece to the given column
     * @param board Board to add piece to
     * @param col Column to put  piece in
     * @param piece BaseGame.Piece to put in
     * @return Row that the piece landed in, -1 if unable to
     */
    private int addPiece(Piece[][] board, int col, Piece piece) {
        col = col - 1;
        if(col < 0 || col >= board[0].length) {
            return -1;
        }

        // Check the top
        if(board[0][col] != null)
            return -1;

        // Check the middle
        for(int i = 0; i < board.length - 1; i++) {
            if(board[i+1][col] != null && setPiece(board, piece, i, col)) {
                return i;
            }
        }

        setPiece(board,piece, board.length - 1, col);
        return board.length - 1;
    }


    /**
     * Puts the given piece in the board at the row and column
     * @param board Board to put piece in
     * @param piece Piece to put in
     * @param row Row to insert
     * @param col Column to insert
     * @return Always returns [true]
     */
    private boolean setPiece(Piece[][] board, Piece piece, int row, int col) {
        board[row][col] = piece;
        return true;
    }
}