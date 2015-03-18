package pawn.model.dao;

import org.junit.Test;
import pawn.model.Board;
import pawn.model.Cell;

import static org.junit.Assert.*;

public class BoardDaoInMemoryTest {

    private BoardDao boardDao = new BoardDaoInMemory();

    @Test
    public void loadBoardReturnsTheSameBoard() {
        Board board = boardDao.loadBoard();
        board.saveMove(7,4);
        assertArrayEquals(board.cells(), boardDao.loadBoard().cells());
    }

    @Test
    public void daoInMemoryStoresBoard() throws Exception {
        Board board = boardDao.loadBoard();
        board.saveMove(1, 4);
        BoardDao anotherBoardDao = new BoardDaoInMemory();
        assertArrayEquals(board.cells(), anotherBoardDao.loadBoard().cells());
    }

    @Test
    public void newGameResetsField() {
        Board board = boardDao.loadBoard();
        board.saveMove(1, 4);
        boardDao.newGame();
        board = boardDao.loadBoard();
        assertArrayEquals(new Cell[]{
                Cell.black, Cell.black, Cell.black,
                Cell.empty, Cell.empty, Cell.empty,
                Cell.white, Cell.white, Cell.white}, board.cells());

    }

    @Test
    public void loadNewGameByCode() {
        String gameId = boardDao.newGameWithId();
        assertNotNull(gameId);
        Board board = boardDao.loadBoardById(gameId);
        assertNotNull(board);
    }

    @Test
    public void newGameCreatesUniqueIds() {
        String gameId1 = boardDao.newGameWithId();
        String gameId2 = boardDao.newGameWithId();
        assertNotEquals(gameId1, gameId2);
        Board board1 = boardDao.loadBoardById(gameId1);
        Board board2 = boardDao.loadBoardById(gameId2);
        assertNotEquals(board1, board2);
    }
}
