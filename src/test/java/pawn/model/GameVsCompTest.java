package pawn.model;

import org.junit.Before;
import org.junit.Test;
import pawn.exceptions.MoveNotAllowedException;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * User: nike
 * Date: 4/18/15
 */
public class GameVsCompTest {
    private Game game = new Game("g1");

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void gameVsComputerAsBlackPlayer() {
        RandomCompPlayer comp = new RandomCompPlayer(game);
        game.setBlackPlayerComp(comp);
        assertNotNull(game.getBlackPlayerComp());
    }

    @Test(expected = MoveNotAllowedException.class)
    public void throwsWhenTurnIsNotWhiteComp() {
        game.setBlackPlayerComp(new RandomCompPlayer(game));
        game.moveByComputer();
    }

    @Test
    public void gameVsComputerAsWhitePlayer() {
        Cell[] cells = game.cells();
        game.setWhitePlayerComp(new RandomCompPlayer(game));
        assertNotNull(game.getWhitePlayerComp());
        assertTrue(game.getMoveOptionsFor(true).size() > 0);
        game.moveByComputer();
        assertThat(cells, not(equalTo(game.cells())));
    }


    @Test
    public void gameSavesCompVsComp() {
        game.setWhitePlayerComp(new RandomCompPlayer(game));
        game.setBlackPlayerComp(new RandomCompPlayer(game));
        assertNotNull(game.getWhitePlayerComp());
        assertNotNull(game.getBlackPlayerComp());
    }

}
