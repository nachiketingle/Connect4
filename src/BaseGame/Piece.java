package BaseGame;

public class Piece {
    public String name;
    public int val;
    public char rep;
    public boolean isYellow;

    public Piece(boolean isYellow) {
        name = isYellow ? "YELLOW" : "RED";
        rep = isYellow ? 'Y' : 'R';
        val = isYellow ? 1 : -1;
        this.isYellow = isYellow;
    }

    public String toString() {
        return name;
    }
}
