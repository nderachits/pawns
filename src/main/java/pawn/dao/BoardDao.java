package pawn.dao;

import pawn.dto.BoardDto;

/**
 * Created by Mikalai_Dzerachyts on 2/18/2015.
 */
public interface BoardDao {
    void saveMove(int from, int to);
    BoardDto loadBoard();
    void startNew();
}
