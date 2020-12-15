package BaseGame;

public class Piece {
    public String name;
    public int val;
    public char rep;

    public Piece(boolean isYellow) {
        name = isYellow ? "YELLOW" : "RED";
        rep = isYellow ? 'Y' : 'R';
        val = isYellow ? 1 : -1;
    }

    public String toString() {
        return name;
    }
}
