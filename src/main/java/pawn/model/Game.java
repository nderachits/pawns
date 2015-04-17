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

    private boolean nextMoveWhite;

    public Game(String gameId) {
        this.gameId = gameId;
        cells = new Cell[]{ Cell.black,Cell.black,Cell.black,
                            Cell.empty,Cell.empty,Cell.empty,
                            Cell.white,Cell.white,Cell.white};
        nextMoveWhite = true;
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

    private void saveMove(int from, int to) {
        cells[to] = cells[from];
        cells[from] = Cell.empty;
        nextMoveWhite = !nextMoveWhite;
    }

    public void saveMove(int from, int to, String user1) {
        if(cells[from] == Cell.empty) {
            throw new IllegalStateException("Start cell of move is empty. from: "+from+", to: "+to);
        }
        if(user1 == null || (!user1.equals(whitePlayer) && !user1.equals(blackPlayer))) {
            throw new MoveNotAllowedException("User "+user1+" is not a player in game "+gameId);
        }
        if(user1.equals(getWhitePlayer()) && cellAt(from) != Cell.white ) {
            throw new MoveNotAllowedException("White player only allowed to move white pawns");
        }
        if(user1.equals(getBlackPlayer()) && cellAt(from) != Cell.black ) {
            throw new MoveNotAllowedException("Black player only allowed to move black pawns");
        }
        if(!isMyMoveNext(user1)) {
            throw new MoveNotAllowedException("It is opponents turn. You are "+user1+", but turn is "+(nextMoveWhite?"white":"black")+" player");
        }

        validateMove(from, to);
        saveMove(from, to);
    }

    private void validateMove(int from, int to) {
        int direction = cells[from] == Cell.black ? 1: -1;

        if(line(to)-line(from) != 1*direction) {
            throw new MoveNotAllowedException("Pawn moves one line forward");
        }
        if(cells[to] == Cell.empty) {
            if(column(to) != column(from)) {
                throw new MoveNotAllowedException("Pawn moves one cell forward when is not attacking");
            }
        } else if(cells[to] == cells[from]) {
            throw new MoveNotAllowedException("Pawn can not attack own pawns");
        } else if(Math.abs(column(to)-column(from)) != 1) {
            throw new MoveNotAllowedException("Pawn can attack in diagonal move");
        }

    }

    private int column(int to) {
        return to % 3;
    }

    private int line(int to) {
        return to / 3;
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

    public boolean isJoinAvailableFor(String user) {
        return (whitePlayer == null || blackPlayer == null) && !user.equals(whitePlayer) && !user.equals(blackPlayer) ;
    }

    public boolean isMyMoveNext(String user) {
        return (user.equals(whitePlayer) && nextMoveWhite) ||
                (user.equals(blackPlayer) && !nextMoveWhite);
    }
}
