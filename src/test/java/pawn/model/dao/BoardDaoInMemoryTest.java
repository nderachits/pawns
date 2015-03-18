package pawn.model.dao;

import org.junit.Test;
import pawn.exceptions.GameNotFound;
import pawn.model.Board;
import pawn.model.Cell;

import static org.junit.Assert.*;

public class BoardDaoInMemoryTest {

    private BoardDao boardDao = new BoardDaoInMemory();

    @Test
    public void loadBoardReturnsTheSameBoard() {
        String gameId = boardDao.newGameId();
        Board board = boardDao.loadBoardById(gameId);
        board.saveMove(7,4);
        assertArrayEquals(board.cells(), boardDao.loadBoardById(gameId).cells());
    }

    @Test
    public void daoInMemoryStoresBoard() throws Exception {
        String gameId = boardDao.newGameId();
        Board board = boardDao.loadBoardById(gameId);
        board.saveMove(1, 4);
        BoardDao anotherBoardDao = new BoardDaoInMemory();
        assertArrayEquals(board.cells(), anotherBoardDao.loadBoardById(gameId).cells());
    }

    @Test
    public void newGameResetsField() {
        String gameId = boardDao.newGameId();
        Board board = boardDao.loadBoardById(gameId);
        board.saveMove(1, 4);

        Board board2 = boardDao.loadBoardById(boardDao.newGameId());
        assertArrayEquals(new Cell[]{
                Cell.black, Cell.empty, Cell.black,
                Cell.empty, Cell.black, Cell.empty,
                Cell.white, Cell.white, Cell.white}, board.cells());
        assertArrayEquals(new Cell[]{
                Cell.black, Cell.black, Cell.black,
                Cell.empty, Cell.empty, Cell.empty,
                Cell.white, Cell.white, Cell.white}, board2.cells());

    }

    @Test
    public void loadNewGameByCode() {
        String gameId = boardDao.newGameId();
        assertNotNull(gameId);
        Board board = boardDao.loadBoardById(gameId);
        assertNotNull(board);
    }

    @Test
    public void newGameCreatesUniqueIds() {
        String gameId1 = boardDao.newGameId();
        String gameId2 = boardDao.newGameId();
        assertNotEquals(gameId1, gameId2);
        Board board1 = boardDao.loadBoardById(gameId1);
        Board board2 = boardDao.loadBoardById(gameId2);
        assertNotEquals(board1, board2);
    }

    @Test(expected = GameNotFound.class)
    public void gameNotFoundThrowsException() {
        boardDao.loadBoardById("game_not_exists");
    }
}
