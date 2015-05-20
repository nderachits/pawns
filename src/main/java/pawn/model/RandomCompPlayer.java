package pawn.model;

import java.util.List;
import java.util.Random;

/**
 * User: nike
 * Date: 4/18/15
 */
public class RandomCompPlayer implements CompPlayer{
    private Game game;
    private int pauseMs;

    public RandomCompPlayer(Game game) {
        this.game = game;
    }

    public RandomCompPlayer(Game game, int pause) {
        this.game = game;
        this.pauseMs = pause;
    }

    public void nextMove(final boolean moveAsWhite) {
        pause();
        List<Move> moveOptions = game.getMoveOptionsFor(moveAsWhite);
        if(moveOptions.size() > 0) {
            int moveIndex = new Random().nextInt(moveOptions.size());
            Move optimalMove = moveOptions.get(moveIndex);
            game.saveMove(optimalMove.from, optimalMove.to, moveAsWhite ? Game.COMPUTER_WHITE : Game.COMPUTER_BLACK);
        }
    }

    private void pause() {
        if(pauseMs > 0) {
            try {
                Thread.sleep(pauseMs);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
