package pawn.webapp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import pawn.model.Board;
import pawn.model.dao.BoardDaoInMemory;
import pawn.model.dto.BoardDto;
import pawn.model.dto.GameDTO;
import pawn.model.dto.MoveDto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: nike
 * Date: 2/19/15
 */

@RestController
@EnableWebSocket
public class BoardService extends TextWebSocketHandler implements WebSocketConfigurer {

    private Map<String, List<WebSocketSession>> sessionsMap = new HashMap<>();

    @RequestMapping("/board/{gameId}")
    public BoardDto boardById(@PathVariable String gameId) {
        Board board = new BoardDaoInMemory().loadBoardById(gameId);
        return new BoardDto(board.cells());
    }

    @RequestMapping(value = "/move/{gameId}", method = RequestMethod.POST)
    public void move(@PathVariable String gameId, @RequestBody MoveDto param) throws JsonProcessingException {
        Board board = new BoardDaoInMemory().loadBoardById(gameId);
        board.saveMove(param.getFrom(), param.getTo());
        sendAll(gameId);
    }

    //not used
    @RequestMapping(value = "/newgameid", method = RequestMethod.POST)
    public GameDTO newGameId() throws JsonProcessingException {
        String gameId = new BoardDaoInMemory().newGameId();
        return new GameDTO(gameId);
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
        System.out.println("session end: "+session.getId());
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
        Board board = new BoardDaoInMemory().loadBoardById(gameId);
        String text = mapper.writeValueAsString(new BoardDto(board.cells()));
        System.out.println("text to send: " + text);
        for (WebSocketSession session : sessionsList) {
            try {
                session.sendMessage(new TextMessage(text));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
