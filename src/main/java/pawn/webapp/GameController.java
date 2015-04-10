package pawn.webapp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pawn.model.Game;
import pawn.model.dao.GameDao;

/**
 * User: nike
 * Date: 4/11/15
 */
@Controller
public class GameController {

    private final Log log = LogFactory.getLog(getClass());

    @Value("${app.version}")
    private String version;

    @Autowired
    private GameDao gameDao;

    @Autowired
    private UserDetailsManager userDetailsManager;


    @RequestMapping("/")
    public String home(Model model) {
        model.addAttribute("pawnversion", version);
        model.addAttribute("games", gameDao.allGames());
        return "home";
    }

    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public String newGame() {
        String gameId = gameDao.newGameId();
        log.info("new game id: " + gameId);
        return "redirect:/game/"+gameId;
    }

    @RequestMapping("/game/{gameId}")
    public String game(@PathVariable String gameId, Model model) {
        Game game = gameDao.loadGameById(gameId);
        model.addAttribute("pawnversion", version);
        model.addAttribute("board", game);
        return "board";
    }

    public void setGameDao(GameDao gameDao) {
        this.gameDao = gameDao;
    }
}
