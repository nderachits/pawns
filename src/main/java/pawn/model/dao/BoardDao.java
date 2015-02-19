package pawn.model.dao;

import pawn.model.Board;

/**
 * Created by Mikalai_Dzerachyts on 2/18/2015.
 */
public interface BoardDao {
    Board loadBoard();
    void newGame();
}
