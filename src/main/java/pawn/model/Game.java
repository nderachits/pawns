package pawn.model;

import pawn.exceptions.MoveNotAllowedException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mikalai_Dzerachyts on 2/18/2015.
 */
public class Game {

    public static final int PAWNS_IN_LINE = 3;
    public static final String COMPUTER_BLACK = "computerBlack";
    public static final String COMPUTER_WHITE = "computerWhite";

    private Cell[] cells;

    private String gameId;

    private String whitePlayer;
    private CompPlayer whitePlayerComp;

    private String blackPlayer;
    private CompPlayer blackPlayerComp;

    private boolean nextMoveWhite;
    private MoveListener moveListener;
    private boolean gameFinished;

    public Game(String gameId) {
        this.gameId = gameId;
        cells = new Cell[]{ Cell.black,Cell.black,Cell.black,
                            Cell.empty,Cell.empty,Cell.empty,
                            Cell.white,Cell.white,Cell.white};
        nextMoveWhite = true;
        gameFinished = false;
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

    public void saveMove(int from, int to, String user) {
        if(cells[from] == Cell.empty) {
            throw new IllegalStateException("Start cell of move is empty. from: "+from+", to: "+to);
        }
        if(user == null || (!user.equals(whitePlayer) && !user.equals(blackPlayer))) {
            throw new MoveNotAllowedException("User "+user+" is not a player in game "+gameId);
        }
        if(user.equals(getWhitePlayer()) && cellAt(from) != Cell.white ) {
            throw new MoveNotAllowedException("White player only allowed to move white pawns");
        }
        if(user.equals(getBlackPlayer()) && cellAt(from) != Cell.black ) {
            throw new MoveNotAllowedException("Black player only allowed to move black pawns");
        }
        if(!isMyMoveNext(user)) {
            throw new MoveNotAllowedException("It is opponents turn. You are "+user+", but turn is "+(nextMoveWhite?"white":"black")+" player");
        }

        String result = validateMove(from, to);
        if(result != null) {
            throw new MoveNotAllowedException(result);
        }
        saveMove(from, to);

        afterMove();
    }

    private void afterMove() {

        if(getMoveOptionsFor(nextMoveWhite).size() == 0) {
            gameFinished = true;
        } else if(isComputerMovesNext()) {
            moveByComputer();
            if(moveListener!=null) {
                moveListener.boardUpdated(gameId);
            }
        }
    }

    private boolean isComputerMovesNext() {
        return (nextMoveWhite && whitePlayerComp!=null) || (!nextMoveWhite && blackPlayerComp!=null);
    }

    public String validateMove(int from, int to) {
        int direction = cells[from] == Cell.black ? 1: -1;

        if(line(to)-line(from) != direction) {
            return "Pawn moves one line forward";
        }
        if(cells[to] == Cell.empty) {
            if(column(to) != column(from)) {
                return "Pawn moves one cell forward when is not attacking";
            }
        } else if(cells[to] == cells[from]) {
            return "Pawn can not attack own pawns";
        } else if(Math.abs(column(to)-column(from)) != 1) {
            return "Pawn can attack in diagonal move";
        }

        return null;
    }

    private int column(int to) {
        return to % PAWNS_IN_LINE;
    }

    private int line(int to) {
        return to / PAWNS_IN_LINE;
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

    public void setBlackPlayerComp(CompPlayer blackPlayerComp) {
        this.blackPlayerComp = blackPlayerComp;
        this.blackPlayer = COMPUTER_BLACK;
    }

    public CompPlayer getBlackPlayerComp() {
        return blackPlayerComp;
    }

    public void setWhitePlayerComp(CompPlayer whitePlayerComp) {
        this.whitePlayerComp = whitePlayerComp;
        this.whitePlayer = COMPUTER_WHITE;
    }

    public CompPlayer getWhitePlayerComp() {
        return whitePlayerComp;
    }

    public void moveByComputer() {
        if(nextMoveWhite) {
            if(whitePlayerComp != null) {
                whitePlayerComp.nextMove(nextMoveWhite);
            } else {
                throw new MoveNotAllowedException("It is white's move and it is not computer");
            }
        } else if (blackPlayer != null) {
            blackPlayerComp.nextMove(nextMoveWhite);
        } else {
            throw new MoveNotAllowedException("It is black's move and it is not computer");
        }
    }

    public List<Move> getMoveOptionsFor(boolean moveAsWhite) {
        Cell myPawn = moveAsWhite ? Cell.white : Cell.black;
        Cell[] cells = this.cells();
        List<Move> moveOptions = new ArrayList<>();
        for(int i=0; i<cells.length; i++ ) {
            if(cells[i].equals(myPawn)) {
                for(int j=0; j<cells.length; j++) {
                    if(this.validateMove(i, j) == null) {
                        moveOptions.add(new Move(i, j));
                    }
                }
            }
        }
        return moveOptions;
    }

    public void setMoveListener(MoveListener moveListener) {
        this.moveListener = moveListener;
    }

    public MoveListener getMoveListener() {
        return moveListener;
    }

    public boolean isGameFinished() {
        return gameFinished;
    }

    public boolean isNextMoveWhite() {
        return nextMoveWhite;
    }
}
