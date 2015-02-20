package pawn.webapp;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pawn.model.Board;
import pawn.model.dao.BoardDao;
import pawn.model.dao.BoardDaoInMemory;
import pawn.model.dto.BoardDto;
import pawn.model.dto.MoveDto;

/**
 * User: nike
 * Date: 2/19/15
 */

@RestController
public class BoardService {

    @RequestMapping("/board")
    public BoardDto board() {
        Board board = new BoardDaoInMemory().loadBoard();
        return new BoardDto(board.cells());
    }

    @RequestMapping(value = "/move", method = RequestMethod.POST)
    public void move(@RequestBody MoveDto param) {
        Board board = new BoardDaoInMemory().loadBoard();
        board.saveMove(param.getFrom(), param.getTo());
    }

    @RequestMapping(value = "/newgame", method = RequestMethod.POST)
    public void newGame() {
        new BoardDaoInMemory().newGame();
    }
}
