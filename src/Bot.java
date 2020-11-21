import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Random;

public class Bot {
    boolean isYellow = true;
    Random rand = new Random();
    final int maxDepth = 5;
    int largestHeight;
    int moveDepth = -1;

    class Move {
        int depth;
        int col;
        Move(int col, int depth) {
            this.depth = depth;
            this.col = col;
        }
    }

    Bot() {
        isYellow = true;
    }

    Bot(boolean _isYellow) {
        this.isYellow = _isYellow;
    }

    public int playPiece(Piece[][] origBoard) {
        Piece[][] board = copyBoard(origBoard);
        largestHeight = 0;
        Instant start = Instant.now();
        Move move = playTree(board, maxDepth);
        Instant end = Instant.now();

        if(move.col == -1) {
            System.out.println("Choosing randomly");
            return rand.nextInt(board[0].length) + 1;
        }
        System.out.println();
        System.out.println("Move was from depth: " + (maxDepth - move.depth));
        System.out.println("Column: " + move.col);
        System.out.println("Time Spent Calculating: " + Duration.between(start, end));
        System.out.println("Deepest Path: " + largestHeight);
        return move.col;
    }

    private Move playTree(Piece[][] origBoard, int depth) {
        if(largestHeight < maxDepth - depth)
            largestHeight = maxDepth - depth;

        if(depth == 0 ){//|| depth <= moveDepth) {
            return new Move(-1, depth);
        }

        Piece[][] board = copyBoard(origBoard);
        int col;

        // Check for an immediate win
        col = immediateWin(origBoard, isYellow);
        if(col > 0) {
            //System.out.println("Found win at depth: " + depth + " with col: " + col);
            return new Move(col, depth);
        }

        // Check to stop a win
        col = immediateWin(origBoard, !isYellow);
        if(col > 0) {
            return new Move(col, depth);
        }

        // Play a random spot that does not cause an immediate loss
        ArrayList<Integer> botColumns = new ArrayList<>();
        ArrayList<Integer> playerColumns = new ArrayList<>();
        for(int i = 1; i <= board[0].length; i++) {
            botColumns.add(i);
            playerColumns.add(i);
        }

        // Simulate each type of play
        ArrayList<Move> moves = new ArrayList<>();
        Piece botPiece = new Piece(isYellow);
        Piece playerPiece = new Piece(!isYellow);
        Move move;
        int botCol, playerCol;
        while(!botColumns.isEmpty()) {
            botCol = botColumns.remove(rand.nextInt(botColumns.size()));
            while (!playerColumns.isEmpty()) {
                board = copyBoard(origBoard);
                playerCol = playerColumns.remove(rand.nextInt(playerColumns.size()));
                addPiece(board, botCol, botPiece);
                addPiece(board, playerCol, playerPiece);
                move = playTree(board, depth - 1);
                if(move.col != -1) {
                    moveDepth = move.depth;
                    move.col = botCol;
                    moves.add(move);
                }
            }
        }


        if(moves.isEmpty()) {
            return new Move(-1, depth);
        }
        Move greatestDepth = new Move(-1, -1);
        for(Move curr : moves) {
            if(curr.depth > greatestDepth.depth && curr.col != -1)
                greatestDepth = curr;
        }
        return greatestDepth;
    }

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
     * @param piece Piece to put in
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


    private boolean setPiece(Piece[][] board, Piece piece, int row, int col) {
        board[row][col] = piece;
        return true;
    }
}
