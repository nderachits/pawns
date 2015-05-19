package pawn.model.dto;

import pawn.model.Cell;
import pawn.model.Game;

/**
 * User: nike
 * Date: 2/19/15
 */
public class GameDto {

    private final Cell[] cells;
    private final String gameId;
    private final boolean gameFinished;
    private final boolean nextMoveWhite;

    public GameDto(Game game) {
        this.gameId = game.getGameId();
        this.cells = game.cells();
        nextMoveWhite = game.isNextMoveWhite();
        gameFinished = game.isGameFinished();
    }

    public String getGameId() {
        return gameId;
    }

    public Cell[] getCells() {
        return cells;
    }

    public boolean isGameFinished() {
        return gameFinished;
    }

    public boolean isNextMoveWhite() {
        return nextMoveWhite;
    }
}
