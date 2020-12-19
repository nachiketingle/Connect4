package BaseGame;

public abstract class Bot {
    public boolean isYellow;
    public abstract int playPiece(Piece[][] origBoard);

    /**
     * Returns a copy of the given board.
     * @param board Board to copy from
     * @return Given board
     */
    protected Piece[][] copyBoard(Piece[][] board) {
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
    protected int immediateWin(Piece[][] origBoard, boolean isYellow) {
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
    protected int addPiece(Piece[][] board, int col, Piece piece) {
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

    public void updateWin(boolean yellowWin) {}

}
