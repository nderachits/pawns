package pawn.model.dao;

import pawn.exceptions.GameNotFound;
import pawn.model.Board;

import java.util.*;

/**
 * User: nike
 * Date: 2/19/15
 */
public class BoardDaoInMemory implements BoardDao {

    private static int nextGameId = 1;
    private static Map<String, Board> boards = new LinkedHashMap<>();

    @Override
    public String newGameId() {
        String gameId = "g"+nextGameId++;
        boards.put(gameId, new Board());
        return gameId;
    }

    @Override
    public Board loadBoardById(String gameId) {
        Board board1 = boards.get(gameId);
        if(board1 == null) {
            throw new GameNotFound("Game "+gameId+" is not found");
        }
        return board1;
    }

    public Collection<String> allGames() {
        return boards.keySet();
    }
}