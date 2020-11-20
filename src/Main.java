public class Main {

    public static void main(String[] args) throws InterruptedException {
        Game game = new Game();
        game.run(new Bot(true), new Bot(false));
    }
}
