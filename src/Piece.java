public class Piece {
    String name;
    int val;
    char rep;

    Piece(boolean isYellow) {
        name = isYellow ? "YELLOW" : "RED";
        rep = isYellow ? 'Y' : 'R';
        val = isYellow ? 1 : -1;
    }

    public String toString() {
        return name;
    }
}
