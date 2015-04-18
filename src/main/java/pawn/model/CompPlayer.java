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
        Cell myPawn = moveAsWhite ? Cell.white : Cell.black;
        Cell[] cells = game.cells();
        List<Move> moveOptions = new ArrayList<>();
        for(int i=0; i<cells.length; i++ ) {
            if(cells[i].equals(myPawn)) {
                for(int j=0; j<cells.length; j++) {
                    if(game.validateMove(i, j) == null) {
                        moveOptions.add(new Move(i, j));
                    }
                }
            }
        }
        if(moveOptions.size()>0) {
            int moveIndex = new Random().nextInt(moveOptions.size());
            Move optimalMove = moveOptions.get(moveIndex);
            game.saveMove(optimalMove.from, optimalMove.to, moveAsWhite ? Game.COMPUTER_WHITE : Game.COMPUTER_BLACK);
        }
    }

    final class Move {
        private final int from;
        private final int to;

        public Move(int from, int to) {
            this.from = from;
            this.to = to;
        }

    }
}
