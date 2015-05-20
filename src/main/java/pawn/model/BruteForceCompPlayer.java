package pawn.model;

import java.util.List;

/**
 * User: nike
 * Date: 5/20/15
 */
public class BruteForceCompPlayer implements CompPlayer {

    private Game game;

    public BruteForceCompPlayer(Game game) {
        this.game = game;
    }

    @Override
    public void nextMove(boolean playerColor) {
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
                if (playerColor != nextMoveColor) {
                    origMove.wonNumber++;
                } else {
                    origMove.lostNumber++;
                }
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
                    if (bestMove == null || move.wonNumber > 0) {
                        bestMove = move;
                    }
                }
                if(origMove != null) {
                    origMove.wonNumber += bestMove.wonNumber > 0 ? 1 : 0;
                } else {
                    return bestMove;
                }
            } else {
                boolean anyWon = false;
                for (Move move : moveOptions) {
                    if(move.lostNumber == 0) {
                        anyWon = true;
                    }
                }
                if(origMove != null) {
                    origMove.lostNumber += anyWon ? 1 : 0;
                }

            }

        }
        return null;
    }
}
