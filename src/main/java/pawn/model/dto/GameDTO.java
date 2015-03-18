package pawn.model.dto;

/**
 * User: nike
 * Date: 3/18/15
 */
public class GameDTO {
    private String gameId;

    public GameDTO() {
    }

    public GameDTO(String gameId) {
        this.gameId = gameId;
    }

    public String getGameId() {
        return gameId;
    }
}
