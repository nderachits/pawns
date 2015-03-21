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

    public GameDto(Game game) {
        this.gameId = game.getGameId();
        this.cells = game.cells();
    }

    public String getGameId() {
        return gameId;
    }

    public Cell[] getCells() {
        return cells;
    }
}
