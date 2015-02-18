package pawn.dao;

import org.junit.Test;
import pawn.dto.Board;
import pawn.dto.Cell;

import static org.junit.Assert.*;

public class BoardDaoTest {

    private BoardDao boardDao = new BoardDaoInMemory();

    @Test
    public void shouldReturnBoard() throws Exception {

        Board board = boardDao.loadBoard();
        assertNotNull(board);
        assertEquals(9, board.size());
        assertArrayEquals(new Cell[]{
                Cell.black,Cell.black,Cell.black,
                Cell.empty,Cell.empty,Cell.empty,
                Cell.white,Cell.white,Cell.white}, board.cells());

    }

}

class BoardDaoInMemory implements BoardDao {

    private static Board board = new Board();

    @Override
    public Board loadBoard() {
        return board;
    }
}

