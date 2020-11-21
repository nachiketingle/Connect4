import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Game game = new Game();
        Scanner scanner = new Scanner(System.in);
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
            scanner.close();
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
                    scanner = new Scanner(System.in);
            }
        }

    }

    public static void oneBot(Game game) {
        game.run(new Bot());
    }

    public static void twoBots(Game game) throws InterruptedException {
        game.run(new Bot(false), new Bot(true));
    }
}
