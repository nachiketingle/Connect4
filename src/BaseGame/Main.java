package BaseGame;

import MinmaxBot.MMBot;
import NeuralNetworkBot.InputNeuron;
import NeuralNetworkBot.NNBot;
import NeuralNetworkBot.Neuron;

import java.util.Scanner;

public class Main {

    static Scanner scanner;

    public static void main(String[] args) {
        //testNN();

        runGame();

    }

    public static void runGame() {
        Game game = new Game();
        scanner = new Scanner(System.in);
        boolean chosen = false;
        int players;
        while (!chosen) {
            System.out.println("How many players? (0, 1, 2)");
            try {
                players = Integer.parseInt(scanner.nextLine());
                chosen = true;
            } catch (NumberFormatException e) {
                System.out.println("Please put a valid number");
                chosen = false;
                continue;
            }

            switch (players) {
                case 0:
                    twoBots(game);
                    break;
                case 1:
                    oneBot(game);
                    break;
                case 2:
                    game.run();
                    break;
                default:
                    System.out.println("Please put one of the three choices");
                    chosen = false;
            }
        }
        scanner.close();
    }

    public static void oneBot(Game game) {

        //game.run(new MMBot(true));
        game.run(new NNBot(true, "test.csv"));
    }

    public static int twoBots(Game game) {
        //game.run(new MMBot(true), new MMBot(false));
        //return game.run(new NNBot(true, "test.csv"), new MMBot(false));
        return game.run(new NNBot(true, "test.csv"), new MMBot(false));
    }

    public static void testNN() {
        Game game;
        int redWins = 0, yellowWins = 0, ties = 0, totalGames = 0;
        int result;
        for(int i = 0; i < 50; i++) {
            game = new Game();
            result = twoBots(game);
            switch (result) {
                case 1:
                    yellowWins++;
                    break;
                case -1:
                    redWins++;
                    break;
                case 0:
                    ties++;
                    break;
            }

            totalGames++;
        }

        System.out.println("RED WINS: " + redWins + "\tYELLOW WINS: " + yellowWins);
        System.out.println("TIES: " + ties);
        System.out.println("TOTAL: " + totalGames);

    }
}
