package pawn.webapp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import pawn.model.Game;
import pawn.model.MoveListener;
import pawn.model.dao.GameDao;
import pawn.model.dto.GameDto;
import pawn.model.dto.MoveDto;

/**
 * User: nike
 * Date: 2/19/15
 */

@RestController
@EnableWebSocket
public class GameWebService extends TextWebSocketHandler implements WebSocketConfigurer, MoveListener {

    private Map<String, List<WebSocketSession>> sessionsMap = new HashMap<>();

    @Autowired
    private GameDao gameDao;

    @RequestMapping("/board/{gameId}")
    public GameDto boardById(@PathVariable String gameId) {
        Game game = getGameDao().loadGameById(gameId);
        return new GameDto(game);
    }

    @RequestMapping(value = "/move/{gameId}", method = RequestMethod.POST)
    public void move(@PathVariable String gameId, @RequestBody MoveDto param) throws JsonProcessingException {
        Game game = getGameDao().loadGameById(gameId);
        String user = WebSecurityConfig.getCurrentUser();
        game.saveMove(param.getFrom(), param.getTo(), user);
        System.out.println("Game: "+gameId+", move from "+param.getFrom()+" to "+param.getTo());
        sendAll(gameId);
    }

    //not used
    @RequestMapping(value = "/newgameid", method = RequestMethod.POST)
    public GameDto newGameId() throws JsonProcessingException {
        String gameId = getGameDao().newGameId();
        Game game = getGameDao().loadGameById(gameId);
        GameDto gameDto = new GameDto(game);
        return gameDto;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(this, "/websocket");
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        System.out.println("new session: "+session.getId());
        String gameId = extractGameId(session);
        System.out.println("gameId: "+ gameId);
        addSession(session, gameId);
        sendAll(gameId);
    }

    private String extractGameId(WebSocketSession session) {
        UriComponents components = UriComponentsBuilder.fromUri(session.getUri()).build();
        List<String> gameIdList = components.getQueryParams().get("gameId");
        if(gameIdList.size() != 1 ) {
            throw new IllegalStateException("Game id not found or not unique: "+gameIdList);
        }
        return gameIdList.get(0);
    }

    private void addSession(WebSocketSession session, String gameId) {
        List<WebSocketSession> sessionsList = sessionsMap.get(gameId);
        if(sessionsList==null) {
            sessionsList = new ArrayList<>();
        }
        sessionsList.add(session);
        sessionsMap.put(gameId, sessionsList);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        System.out.println("session end: " + session.getId());
        String gameId = extractGameId(session);
        removeSession(session, gameId);
    }

    private void removeSession(WebSocketSession session, String gameId) {
        List<WebSocketSession> sessionsList = sessionsMap.get(gameId);
        if(sessionsList!=null) {
            sessionsList.remove(session);
        }
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        System.out.println("websocket: "+message.getPayload());
    }

    public void sendAll(String gameId) throws JsonProcessingException {
        List<WebSocketSession> sessionsList = sessionsMap.get(gameId);
        if(CollectionUtils.isEmpty(sessionsList)) {
            return;
        }
        ObjectMapper mapper = new ObjectMapper();
        Game game = getGameDao().loadGameById(gameId);
        String text = mapper.writeValueAsString(new GameDto(game));
        System.out.println("text to send: " + text);
        for (WebSocketSession session : sessionsList) {
            try {
                session.sendMessage(new TextMessage(text));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void boardUpdated(String gameId) {
// comented as redundant
//        try {
//            sendAll(gameId);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public GameDao getGameDao() {
        return gameDao;
    }

    public void setGameDao(GameDao gameDao) {
        this.gameDao = gameDao;
    }

}
