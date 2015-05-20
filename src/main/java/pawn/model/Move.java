package pawn.model;

/**
 * User: nike
 * Date: 5/19/15
 */
final public class Move {
    public final int from;
    public final int to;
    public int lostNumber;
    public int wonNumber;

    public Move(int from, int to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return "Move{" +
                "from=" + from +
                ", to=" + to +
                ", lostNumber=" + lostNumber +
                ", wonNumber=" + wonNumber +
                '}';
    }
}
