package pawn.model.dao;

import pawn.model.Game;

import java.util.Collection;

/**
 * Created by Mikalai_Dzerachyts on 2/18/2015.
 */
public interface GameDao {
    String newGameId();
    Game loadGameById(String gameId);
    Collection<String> allGames();
}
