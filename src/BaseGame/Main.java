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
        game.run(new MMBot(true));
    }

    public static void twoBots(Game game) {
        //game.run(new MMBot(true), new MMBot(false));
        game.run(new NNBot(true), new MMBot(false));
    }

    public static void testNN() {
        NNBot bot = new NNBot(true);

    }
}
