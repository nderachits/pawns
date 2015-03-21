package pawn.model.dao;

import pawn.model.Player;

/**
 * User: nike
 * Date: 3/21/15
 */
public interface PlayerDao {

    String create(String name1);

    Player loadById(String playerId);
}
