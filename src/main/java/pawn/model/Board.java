package pawn.model;

/**
 * Created by Mikalai_Dzerachyts on 2/18/2015.
 */
public class Board {

    public static final int PAWNS_IN_LINE = 3;

    private Cell[] cells;

    public Board() {
        cells = new Cell[]{ Cell.black,Cell.black,Cell.black,
                            Cell.empty,Cell.empty,Cell.empty,
                            Cell.white,Cell.white,Cell.white};
    }

    public int size() {
        return PAWNS_IN_LINE*PAWNS_IN_LINE;
    }

    public Cell cellAt(int index) {
        return cells[index];
    }

    public Cell[] cells() {
        return cells.clone();
    }

    public void saveMove(int from, int to) {
        if(cells[from] == Cell.empty) {
            throw new IllegalStateException("Start cell of move is empty. from: "+from+", to: "+to);
        }
        cells[to] = cells[from];
        cells[from] = Cell.empty;
    }
}
