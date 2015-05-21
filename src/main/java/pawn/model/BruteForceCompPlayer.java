package pawn.model;

import java.util.List;

/**
 * User: nike
 * Date: 5/20/15
 */
public class BruteForceCompPlayer implements CompPlayer {

    private Game game;
    private int pauseMs;

    public BruteForceCompPlayer(Game game) {
        this.game = game;
    }

    public BruteForceCompPlayer(Game game, int pauseMs) {
        this.game = game;
        this.pauseMs = pauseMs;
    }

    @Override
    public void nextMove(boolean playerColor) {
        pause();
        Cell[] cells = game.cells();

        Move move = bestNextMoveRec(cells, playerColor, playerColor, null);
        if(move != null) {
            game.saveMove(move.from, move.to, playerColor ? Game.COMPUTER_WHITE : Game.COMPUTER_BLACK);
        }
    }

    private static void saveMove(Cell[] cells, int from, int to) {
        cells[to] = cells[from];
        cells[from] = Cell.empty;
    }

    public static Move bestNextMoveRec(Cell[] cells, boolean nextMoveColor, boolean playerColor, Move origMove) {
        List<Move> moveOptions = Game.getMoveOptionsFor(cells, nextMoveColor);
        if(moveOptions.size() == 0) {
            if(origMove != null) {
                origMove.won = playerColor != nextMoveColor;
            }
        } else {
            for (Move move : moveOptions) {
                Cell fromVal = cells[move.from];
                Cell toVal = cells[move.to];
                saveMove(cells, move.from, move.to);
                bestNextMoveRec(cells, !nextMoveColor, playerColor, move);
                cells[move.from] = fromVal;
                cells[move.to] = toVal;
            }

            if(playerColor == nextMoveColor) {
                Move bestMove = null;
                for (Move move : moveOptions) {
                    if (bestMove == null || move.won) { //todo: choose randomly if no win move
                        bestMove = move;
                    }
                }
                if(origMove != null) {
                    origMove.won = bestMove.won;
                } else {
                    return bestMove;
                }
            } else {
                boolean won = true;
                for (Move move : moveOptions) {
                    won &= move.won;
                }
                if(origMove != null) {
                    origMove.won = won;
                }
            }
        }
        return null;
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
