package pawn.model.dao;

import org.junit.Test;
import pawn.exceptions.GameNotFound;
import pawn.model.Game;
import pawn.model.Cell;

import static org.junit.Assert.*;

public class GameDaoInMemoryTest {

    private GameDao gameDao = new GameDaoInMemory();

    @Test
    public void loadBoardReturnsTheSameBoard() {
        String gameId = gameDao.newGameId();
        Game game = gameDao.loadGameById(gameId);
        game.setWhitePlayer("user1");
        game.saveMove(7,4, "user1");
        assertArrayEquals(game.cells(), gameDao.loadGameById(gameId).cells());
    }

    @Test
    public void daoInMemoryStoresBoard() throws Exception {
        String gameId = gameDao.newGameId();
        Game game = gameDao.loadGameById(gameId);
        game.setWhitePlayer("user1");
        game.saveMove(1, 4, "user1");
        GameDao anotherGameDao = new GameDaoInMemory();
        assertArrayEquals(game.cells(), anotherGameDao.loadGameById(gameId).cells());
    }

    @Test
    public void newGameResetsField() {
        String gameId = gameDao.newGameId();
        Game game = gameDao.loadGameById(gameId);
        game.setWhitePlayer("user1");
        game.saveMove(1, 4, "user1");

        Game game2 = gameDao.loadGameById(gameDao.newGameId());
        assertArrayEquals(new Cell[]{
                Cell.black, Cell.empty, Cell.black,
                Cell.empty, Cell.black, Cell.empty,
                Cell.white, Cell.white, Cell.white}, game.cells());
        assertArrayEquals(new Cell[]{
                Cell.black, Cell.black, Cell.black,
                Cell.empty, Cell.empty, Cell.empty,
                Cell.white, Cell.white, Cell.white}, game2.cells());

    }

    @Test
    public void loadNewGameById() {
        String gameId = gameDao.newGameId();
        assertNotNull(gameId);
        Game game = gameDao.loadGameById(gameId);
        assertNotNull(game);
    }

    @Test
    public void newGameCreatesUniqueIds() {
        String gameId1 = gameDao.newGameId();
        String gameId2 = gameDao.newGameId();
        assertNotEquals(gameId1, gameId2);
        Game game1 = gameDao.loadGameById(gameId1);
        Game game2 = gameDao.loadGameById(gameId2);
        assertNotEquals(game1, game2);
        assertEquals(gameId1, game1.getGameId());
        assertEquals(gameId2, game2.getGameId());
    }

    @Test(expected = GameNotFound.class)
    public void gameNotFoundThrowsException() {
        gameDao.loadGameById("game_not_exists");
    }
}
