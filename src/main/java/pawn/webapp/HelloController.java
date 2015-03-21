package pawn.webapp;


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
import pawn.model.Board;
import pawn.model.dao.BoardDao;

@Controller
@ComponentScan("pawn")
@EnableAutoConfiguration
public class HelloController {

    @Autowired
    private BoardDao boardDao;

    @Value("${app.version}")
    private String version;

    @RequestMapping("/")
    public String home(Model model) {
        model.addAttribute("pawnversion", version);
        model.addAttribute("games", getBoardDao().allGames());
        return "home";
    }

    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public String newGame() {
        String gameId = getBoardDao().newGameId();
        return "redirect:/game/"+gameId;
    }

    @RequestMapping("/game/{gameId}")
    public String game(@PathVariable String gameId, Model model) {
        Board board = getBoardDao().loadBoardById(gameId);
        model.addAttribute("pawnversion", version);
        model.addAttribute("board", board);
        return "board";
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(HelloController.class, args);
    }

    @Bean
    public ServerProperties myServerProperties() {
        ServerProperties p = new ServerProperties();
        String portStr = System.getProperty("app.port");  // CloudBees Environment Variable for local port
        int port = (portStr != null) ? Integer.parseInt(portStr) : 8080;
        p.setPort(port);
        return p;
    }

    public BoardDao getBoardDao() {
        return boardDao;
    }

    public void setBoardDao(BoardDao boardDao) {
        this.boardDao = boardDao;
    }
}