package pawn.model.dao;

import pawn.model.Board;

/**
 * User: nike
 * Date: 2/19/15
 */
public class BoardDaoInMemory implements BoardDao {

    private static Board board = new Board();

    @Override
    public Board loadBoard() {
        return board;
    }

    @Override
    public void newGame() {
        board = new Board();
    }
}