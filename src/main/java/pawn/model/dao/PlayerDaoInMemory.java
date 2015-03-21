package pawn.model.dao;

import pawn.model.Player;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * User: nike
 * Date: 3/21/15
 */
public class PlayerDaoInMemory implements PlayerDao {

    private static Map<String, Player> players = new LinkedHashMap<>();
    private static int nextId = 1;

    @Override
    public String create(String name1) {
        String id =  "p"+nextId++;
        Player player = new Player(name1);
        players.put(id, player);
        return id;
    }

    @Override
    public Player loadById(String playerId) {
        return players.get(playerId);
    }
}
