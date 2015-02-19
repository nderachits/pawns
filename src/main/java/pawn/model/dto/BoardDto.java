package pawn.model.dto;

import pawn.model.Cell;

/**
 * User: nike
 * Date: 2/19/15
 */
public class BoardDto {

    private final Cell[] cells;

    public BoardDto(Cell[] cells) {
        this.cells = cells;
    }

    public Cell[] getCells() {
        return cells;
    }
}
