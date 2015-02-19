package pawn.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class BoardTest {

    private Board board = new Board();

    @Test
    public void shouldReturnBoard() throws Exception {

        assertNotNull(board);
        assertEquals(9, board.size());
        assertArrayEquals(new Cell[]{
                Cell.black,Cell.black,Cell.black,
                Cell.empty,Cell.empty,Cell.empty,
                Cell.white,Cell.white,Cell.white}, board.cells());

    }

    @Test
    public void moveUpdatesBoard() {
        board.saveMove(6, 3);
        assertArrayEquals(new Cell[]{
                Cell.black, Cell.black, Cell.black,
                Cell.white, Cell.empty, Cell.empty,
                Cell.empty, Cell.white, Cell.white}, board.cells());
    }

    @Test
    public void attackRemovePawn() {
        board.saveMove(6, 3);
        board.saveMove(1, 3);
        assertArrayEquals(new Cell[]{
                Cell.black, Cell.empty, Cell.black,
                Cell.black, Cell.empty, Cell.empty,
                Cell.empty, Cell.white, Cell.white}, board.cells());
    }

}


