package pawn.webapp;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pawn.model.dao.BoardDao;
import pawn.model.dao.BoardDaoInMemory;
import pawn.model.dto.BoardDto;

/**
 * User: nike
 * Date: 2/19/15
 */

@RestController
public class BoardService {

    @RequestMapping("/board")
    public BoardDto board() {
        BoardDao boardDao = new BoardDaoInMemory();
        return new BoardDto(boardDao.loadBoard().cells());
    }
}
