package BaseGame;

import MinmaxBot.MMBot;

import java.util.Scanner;

public class Game {

    Piece[][] board;  // null=Empty Space
    boolean turn;   // True = Yellow, False = Red
    Scanner scanner;

    enum PieceState {
        SUCCESS,
        FILLED,
        OUT_OF_RANGE,
        WINNER,
    }

    Game() {
        turn = false;
        board = new Piece[6][7];
    }

    public void run() {
        Helpers.printBoard(board);

        Scanner scanner = new Scanner(System.in);
        String input;
        int col;
        String piece;

        while (true) {
            piece = turn ? "YELLOW" : "RED";
            System.out.print(piece + " player, pick a column: ");
            input = scanner.nextLine();

            // Check if input is exit
            if(input.equals("exit") || input.equals("q")) {
                System.out.println("Leaving game");
                return;
            }

            // If the input is a column, play
            try {
                col = Integer.parseInt(input);
            } catch (Exception e) {
                System.out.println("Not a valid input");
                continue;
            }

            PieceState state = addPiece(col);
            switch (state) {
                case SUCCESS:
                    Helpers.printBoard(board);
                    break;
                case WINNER:
                    Helpers.printBoard(board);
                    System.out.println(turn ? "RED" : "YELLOW" + " is the Winner!!!");
                    return;
                case OUT_OF_RANGE:
                case FILLED:
                    System.out.println("Unable to add piece to column: " + col);
            }
        }
    }

    public void run(Bot bot) {
        Helpers.printBoard(board);
        scanner = Main.scanner;

        String input;
        int col;
        String piece;

        while (true) {
            piece = turn ? "YELLOW" : "RED";

            System.out.print(piece + " player, pick a column: ");

            // User plays a piece
            if(turn != bot.isYellow) {
                input = scanner.nextLine();

                // Check if input is exit
                if (input.equals("exit") || input.equals("q")) {
                    System.out.println("Leaving game");
                    return;
                }

                // If the input is a column, play
                try {
                    col = Integer.parseInt(input);
                } catch (Exception e) {
                    System.out.println("Not a valid input");
                    continue;
                }
            }
            else {
                // Bot plays a piece
                col = bot.playPiece(board);
                System.out.println(col);
            }

            PieceState state = addPiece(col);
            switch (state) {
                case SUCCESS:
                    Helpers.printBoard(board);
                    break;
                case WINNER:
                    Helpers.printBoard(board);
                    System.out.println((turn ? "RED" : "YELLOW") + " is the Winner!!!");
                    return;
                case OUT_OF_RANGE:
                case FILLED:
                    System.out.println("Unable to add piece to column: " + col);
            }
        }
    }

    public void run(Bot yellowBot, Bot redBot) {
        Helpers.printBoard(board);

        Scanner scanner = new Scanner(System.in);
        String input;
        int col;
        String piece;

        while (true) {
            piece = turn ? "YELLOW" : "RED";

            System.out.print(piece + " player, pick a column: ");

            // User plays a piece
            if(!turn) {
                // Bot plays a piece
                col = redBot.playPiece(board);
            }
            else {
                // Bot plays a piece
                col = yellowBot.playPiece(board);
            }
            System.out.println(col);

            PieceState state = addPiece(col);
            switch (state) {
                case SUCCESS:
                    Helpers.printBoard(board);
                    if(filledBoard()) {
                        System.out.println("Tied!!!");
                        return;
                    }
                    break;
                case WINNER:
                    Helpers.printBoard(board);
                    System.out.println((turn ? "RED" : "YELLOW") + " is the Winner!!!");
                    return;
                case OUT_OF_RANGE:
                case FILLED:
                    System.out.println("Unable to add piece to column: " + col);
            }
        }
    }

    /**
     * Adds a piece to the given row, if possible. If successful, return ture
     * @param col Col to add the piece
     * @return [true] if successfully added
     */
    public PieceState addPiece(int col) {
        col = col - 1;
        if(col < 0 || col >= board[0].length) {
            return PieceState.OUT_OF_RANGE;
        }

        // Check the top
        if(board[0][col] != null)
            return PieceState.FILLED;

        // Check the middle
        for(int i = 0; i < board.length - 1; i++) {
            if(board[i+1][col] != null) {
                return setPiece(i, col);
            }
        }

        return setPiece(board.length - 1, col);
    }

    private PieceState setPiece(int row, int col) {
        board[row][col] = new Piece(turn);
        turn = !turn;
        return Helpers.checkWinner(board, row, col) ? PieceState.WINNER : PieceState.SUCCESS;
    }


    private boolean filledBoard() {
        for(Piece[] row : board) {
            for(Piece piece : row) {
                if(piece == null) {
                    return false;
                }
            }
        }
        return true;
    }


}
