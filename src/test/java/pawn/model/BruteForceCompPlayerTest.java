package pawn.model;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

/**
 * User: nike
 * Date: 5/20/15
 */
public class BruteForceCompPlayerTest {

    @Test()
    public void nextMoveIsOptimal() {

        for(int i=0; i<20; i++) {
            Game game = new Game("g1");
            Cell[] cells = game.cells();
            game.setWhitePlayerComp(new BruteForceCompPlayer(game));
            game.setBlackPlayerComp(new BruteForceCompPlayer(game));
            game.moveByComputer();

            assertTrue(game.isGameFinished());
            assertTrue(game.isNextMoveWhite());
        }
    }
}
