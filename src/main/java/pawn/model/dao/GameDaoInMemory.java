package pawn.model.dao;

import org.springframework.stereotype.Repository;
import pawn.exceptions.GameNotFound;
import pawn.model.Game;

import java.util.*;

/**
 * User: nike
 * Date: 2/19/15
 */
@Repository("boardDao")
public class GameDaoInMemory implements GameDao {

    private static int nextGameId = 1;
    private static Map<String, Game> games = new LinkedHashMap<>();

    @Override
    public String newGameId() {
        String gameId = "g"+nextGameId++;
        games.put(gameId, new Game(gameId));
        return gameId;
    }

    @Override
    public Game loadGameById(String gameId) {
        Game game1 = games.get(gameId);
        if(game1 == null) {
            throw new GameNotFound("Game "+gameId+" is not found");
        }
        return game1;
    }

    public Collection<String> allGames() {
        return games.keySet();
    }
}