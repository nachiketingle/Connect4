package BaseGame;

public class Helpers {

    public static boolean checkWinner(Piece[][] board, int row, int col) {
        int count;

        // Horizontal
        // East
        count = checkDirection(board, row, col, 0, 1);
        // West
        count += checkDirection(board, row, col, 0, -1) - 1;
        if(count >= 4)
            return true;

        // Vertical
        // North
        count = checkDirection(board, row, col, 1, 0);
        // South
        count += checkDirection(board, row, col, -1, 0) - 1;
        if(count >= 4)
            return true;

        // First Diagonal
        // North West
        count = checkDirection(board, row, col, 1, -1);
        // South East
        count += checkDirection(board, row, col, -1, 1) - 1;
        if(count >= 4)
            return true;

        // Second Diagonal
        // North East
        count = checkDirection(board, row, col, 1, 1);
        // South West
        count += checkDirection(board, row, col, -1, -1) - 1;
        return count >= 4;
    }

    private static int checkDirection(Piece[][] board, int row, int col, int rowDir, int colDir) {
        Piece piece = board[row][col];
        int count = 0;
        while(inBounds(board, row, col) && board[row][col] != null && board[row][col].val == piece.val) {
            count++;
            row += rowDir;
            col += colDir;
        }
        return count;
    }

    public static boolean inBounds(Piece[][] board, int row, int col) {
        return row >= 0 && row < board.length && col >= 0 && col < board[row].length;
    }

    /**
     * Prints out the current board state
     */
    public static void printBoard(Piece[][] board) {
        Piece piece;
        char spaceValue;

        System.out.println();

        // Print col numbers
        System.out.print("    ");
        for(int i = 0; i < board[0].length; i++) {
            System.out.print(" " + (i + 1) + "  ");
        }
        System.out.println();

        // Print board
        for(int row = 0; row < board.length; row++) {

            // Dividers
            for(int i = 0; i < board[0].length * 4 + 4; i++) {
                System.out.print("-");
            }
            System.out.println();

            System.out.print((row+1) + ": | ");
            for(int col = 0; col < board[row].length; col++) {
                piece = board[row][col];
                if(piece == null)
                    spaceValue = ' ';
                else
                    spaceValue = piece.rep;

                System.out.print(spaceValue + " | ");
            }
            System.out.println();

        }

        // Final Divider
        for(int i = 0; i < board[0].length * 4 + 4; i++) {
            System.out.print("-");
        }
        System.out.println();
    }
}
