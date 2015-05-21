package pawn.webapp;

import javax.validation.Valid;
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
import pawn.model.BruteForceCompPlayer;
import pawn.model.RandomCompPlayer;
import pawn.model.Game;
import pawn.model.dao.GameDao;
import pawn.webapp.forms.GameVsCompForm;

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

    @Autowired
    private GameWebService gameWebService;

    @RequestMapping("/")
    public String home(Model model) {
        model.addAttribute("pawnversion", version);
        model.addAttribute("games", gameDao.allGames());
        model.addAttribute("gameVsComp", new GameVsCompForm());
        return "home";
    }

    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public String newGame() {
        String gameId = gameDao.newGameId();
        Game game = gameDao.loadGameById(gameId);
        game.setMoveListener(gameWebService);
        game.setWhitePlayer(WebSecurityConfig.getCurrentUser());
        log.info("new game id: " + gameId);
        return "redirect:/game/"+gameId;
    }

    @RequestMapping(value = "/newvscomp", method = RequestMethod.POST)
    public String newVsComp(@Valid GameVsCompForm gameVsComp) {
        String gameId = gameDao.newGameId();
        Game game = gameDao.loadGameById(gameId);
        game.setMoveListener(gameWebService);
        if(gameVsComp.getColor().equalsIgnoreCase("white")) {
            game.setWhitePlayer(WebSecurityConfig.getCurrentUser());
            game.setBlackPlayerComp(new BruteForceCompPlayer(game));
        } else if(gameVsComp.getColor().equalsIgnoreCase("black")) {
            game.setBlackPlayer(WebSecurityConfig.getCurrentUser());
            game.setWhitePlayerComp(new BruteForceCompPlayer(game));
            startComp(game);
        } else {
            throw new IllegalStateException("Color is not set for playing with Computer");
        }

        log.info("new game id vs Comp: " + gameId);
        return "redirect:/game/"+gameId;
    }

    private void startComp(final Game game) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                game.moveByComputer();
            }
        }).start();
    }


    @RequestMapping(value = "/startcomp/{gameId}", method = RequestMethod.POST)
    public String startComp(@PathVariable String gameId, Model model) {
        Game game = gameDao.loadGameById(gameId);
        game.moveByComputer();
        log.info("start game id: " + gameId);
        return "redirect:/game/"+gameId;
    }

    @RequestMapping(value = "/newcompvscomp", method = RequestMethod.POST)
    public String newCompVsComp() {
        String gameId = gameDao.newGameId();
        Game game = gameDao.loadGameById(gameId);
        game.setMoveListener(gameWebService);
        game.setWhitePlayerComp(new BruteForceCompPlayer(game, 1000));
        game.setBlackPlayerComp(new BruteForceCompPlayer(game, 1000));
        startComp(game);
        log.info("new game id: " + gameId);
        return "redirect:/game/"+gameId;
    }

    @RequestMapping(value = "/join/{gameId}", method = RequestMethod.POST)
    public String join(@PathVariable String gameId, Model model) {
        Game game = gameDao.loadGameById(gameId);
        game.setBlackPlayer(WebSecurityConfig.getCurrentUser());
        log.info("join game id: " + gameId);
        return "redirect:/game/"+gameId;
    }

    @RequestMapping("/game/{gameId}")
    public String game(@PathVariable String gameId, Model model) {
        Game game = gameDao.loadGameById(gameId);
        model.addAttribute("pawnversion", version);
        model.addAttribute("game", game);
        model.addAttribute("joinAvailable", game.isJoinAvailableFor(WebSecurityConfig.getCurrentUser()));
        return "board";
    }

    public void setGameDao(GameDao gameDao) {
        this.gameDao = gameDao;
    }
}
