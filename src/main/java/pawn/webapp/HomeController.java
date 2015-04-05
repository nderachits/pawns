package pawn.webapp;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pawn.model.Game;
import pawn.model.dao.GameDao;

@Controller
@ComponentScan("pawn")
@EnableAutoConfiguration
public class HomeController {

    @Autowired
    private GameDao gameDao;

    @Value("${app.version}")
    private String version;

    private final Log log = LogFactory.getLog(getClass());

    @RequestMapping("/")
    public String home(Model model) {
        model.addAttribute("pawnversion", version);
        model.addAttribute("games", getGameDao().allGames());
        return "home";
    }

    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public String newGame() {
        String gameId = getGameDao().newGameId();
        log.info("new game id: "+gameId);
        return "redirect:/game/"+gameId;
    }

    @RequestMapping("/game/{gameId}")
    public String game(@PathVariable String gameId, Model model) {
        Game game = getGameDao().loadGameById(gameId);
        model.addAttribute("pawnversion", version);
        model.addAttribute("board", game);
        return "board";
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(HomeController.class, args);
    }

    @Bean
    public ServerProperties myServerProperties() {
        ServerProperties p = new ServerProperties();
        String portStr = System.getProperty("app.port");  // CloudBees Environment Variable for local port
        int port = (portStr != null) ? Integer.parseInt(portStr) : 8080;
        p.setPort(port);
        return p;
    }

    public GameDao getGameDao() {
        return gameDao;
    }

    public void setGameDao(GameDao gameDao) {
        this.gameDao = gameDao;
    }
}