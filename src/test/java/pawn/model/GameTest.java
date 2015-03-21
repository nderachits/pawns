package pawn.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class GameTest {

    private Game game = new Game("g1");

    @Test
    public void shouldReturnBoard() throws Exception {

        assertNotNull(game);
        assertEquals(9, game.size());
        assertArrayEquals(new Cell[]{
                Cell.black,Cell.black,Cell.black,
                Cell.empty,Cell.empty,Cell.empty,
                Cell.white,Cell.white,Cell.white}, game.cells());

    }

    @Test
    public void moveUpdatesBoard() {
        game.saveMove(6, 3);
        assertArrayEquals(new Cell[]{
                Cell.black, Cell.black, Cell.black,
                Cell.white, Cell.empty, Cell.empty,
                Cell.empty, Cell.white, Cell.white}, game.cells());
    }

    @Test
    public void attackRemovePawn() {
        game.saveMove(6, 3);
        game.saveMove(1, 3);
        assertArrayEquals(new Cell[]{
                Cell.black, Cell.empty, Cell.black,
                Cell.black, Cell.empty, Cell.empty,
                Cell.empty, Cell.white, Cell.white}, game.cells());
    }

    @Test(expected = IllegalStateException.class)
    public void throwExceptionWhenStartingCellOfMoveIsEmpty() {
        game.saveMove(3, 0);
    }

}


