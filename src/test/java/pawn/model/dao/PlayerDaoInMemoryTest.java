package pawn.model.dao;

import org.junit.Test;
import pawn.model.Player;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

/**
 * User: nike
 * Date: 3/21/15
 */
public class PlayerDaoInMemoryTest {

    private PlayerDao playerDao = new PlayerDaoInMemory();

    @Test
    public void signupCreatesUniquePlayers() {
        String playerId = playerDao.create("Name1");
        assertNotNull(playerId);
        String player2Id = playerDao.create("Name2");
        assertNotNull(player2Id);
        assertNotEquals(player2Id, playerId);
    }

    @Test
    public void signupSavesName() {
        String playerId = playerDao.create("Name1");
        assertNotNull(playerId);
        Player player = playerDao.loadById(playerId);
        assertNotNull(player);
        assertEquals("Name1", player.getName());
    }
}
