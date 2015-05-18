package pawn.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * User: nike
 * Date: 4/18/15
 */
public class CompPlayer {
    private Game game;

    public CompPlayer(Game game) {
        this.game = game;
    }

    public void nextMove(final boolean moveAsWhite) {
        List<Move> moveOptions = game.getMoveOptionsFor(moveAsWhite);
        if(moveOptions.size() > 0) {
            int moveIndex = new Random().nextInt(moveOptions.size());
            Move optimalMove = moveOptions.get(moveIndex);
            game.saveMove(optimalMove.from, optimalMove.to, moveAsWhite ? Game.COMPUTER_WHITE : Game.COMPUTER_BLACK);
        }
    }

}
