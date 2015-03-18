package pawn.model.dao;

import pawn.model.Board;

import java.util.HashMap;
import java.util.Map;

/**
 * User: nike
 * Date: 2/19/15
 */
public class BoardDaoInMemory implements BoardDao {

    private static Board board = new Board();

    private static int nextGameId = 1;
    private static Map<String, Board> boards = new HashMap<>();

    @Override
    public Board loadBoard() {
        return board;
    }

    @Override
    public void newGame() {
        board = new Board();
    }

    @Override
    public String newGameWithId() {
        String gameId = "game"+nextGameId++;
        boards.put(gameId, new Board());
        return gameId;
    }

    @Override
    public Board loadBoardById(String gameId) {
        return boards.get(gameId);
    }
}