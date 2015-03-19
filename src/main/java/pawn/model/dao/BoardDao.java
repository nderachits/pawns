package pawn.model.dao;

import pawn.model.Board;

import java.util.Collection;

/**
 * Created by Mikalai_Dzerachyts on 2/18/2015.
 */
public interface BoardDao {
    String newGameId();
    Board loadBoardById(String gameId);
    Collection<String> allGames();
}
