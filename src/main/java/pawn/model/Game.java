package pawn.model;

import pawn.exceptions.MoveNotAllowedException;

/**
 * Created by Mikalai_Dzerachyts on 2/18/2015.
 */
public class Game {

    public static final int PAWNS_IN_LINE = 3;

    private Cell[] cells;

    private String gameId;

    private String whitePlayer;

    private String blackPlayer;

    public Game(String gameId) {
        this.gameId = gameId;
        cells = new Cell[]{ Cell.black,Cell.black,Cell.black,
                            Cell.empty,Cell.empty,Cell.empty,
                            Cell.white,Cell.white,Cell.white};
    }

    public int size() {
        return PAWNS_IN_LINE*PAWNS_IN_LINE;
    }

    public Cell cellAt(int index) {
        return cells[index];
    }

    public Cell[] cells() {
        return cells.clone();
    }

    public String getGameId() {
        return gameId;
    }

    public void saveMove(int from, int to) {
        if(cells[from] == Cell.empty) {
            throw new IllegalStateException("Start cell of move is empty. from: "+from+", to: "+to);
        }
        cells[to] = cells[from];
        cells[from] = Cell.empty;
    }

    public void saveMove(int from, int to, String user1) {
        if(user1 == null || (!user1.equals(whitePlayer) && !user1.equals(blackPlayer))) {
            throw new MoveNotAllowedException("User "+user1+" is not a player in game "+gameId);
        }
        saveMove(from, to);
    }

    public String getWhitePlayer() {
        return whitePlayer;
    }

    public void setWhitePlayer(String whitePlayer) {
        if(whitePlayer != null && whitePlayer.equals(blackPlayer)) {
            throw new IllegalStateException("Players are not unique");
        }
        this.whitePlayer = whitePlayer;
    }

    public String getBlackPlayer() {
        return blackPlayer;
    }

    public void setBlackPlayer(String blackPlayer) {
        if(blackPlayer != null && blackPlayer.equals(whitePlayer)) {
            throw new IllegalStateException("Players are not unique");
        }
        this.blackPlayer = blackPlayer;
    }

}
