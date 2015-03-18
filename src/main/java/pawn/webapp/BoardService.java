package pawn.webapp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import pawn.model.Board;
import pawn.model.dao.BoardDaoInMemory;
import pawn.model.dto.BoardDto;
import pawn.model.dto.GameDTO;
import pawn.model.dto.MoveDto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: nike
 * Date: 2/19/15
 */

@RestController
@EnableWebSocket
public class BoardService extends TextWebSocketHandler implements WebSocketConfigurer {

    private List<WebSocketSession> sessions = new ArrayList<>();

    @Deprecated
    @RequestMapping("/board")
    public BoardDto board() {
        Board board = new BoardDaoInMemory().loadBoard();
        return new BoardDto(board.cells());
    }

    @RequestMapping("/board/{gameId}")
    public BoardDto boardById(@PathVariable String gameId) {
        Board board = new BoardDaoInMemory().loadBoardById(gameId);
        return new BoardDto(board.cells());
    }

    @Deprecated
    @RequestMapping(value = "/move", method = RequestMethod.POST)
    public void move(@RequestBody MoveDto param) throws JsonProcessingException {
        Board board = new BoardDaoInMemory().loadBoard();
        board.saveMove(param.getFrom(), param.getTo());

        sendAll();
    }

    @RequestMapping(value = "/move/{gameId}", method = RequestMethod.POST)
    public void move(@PathVariable String gameId, @RequestBody MoveDto param) throws JsonProcessingException {
        Board board = new BoardDaoInMemory().loadBoardById(gameId);
        board.saveMove(param.getFrom(), param.getTo());
        sendAll();
    }

    @Deprecated
    @RequestMapping(value = "/newgame", method = RequestMethod.POST)
    public void newGame() throws JsonProcessingException {
        new BoardDaoInMemory().newGame();
        sendAll();
    }

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
        sessions.add(session);
        sendAll();
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        System.out.println("session end: "+session.getId());
        sessions.remove(session);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        System.out.println("websocket: "+message.getPayload());
    }

    public void sendAll() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Board board = new BoardDaoInMemory().loadBoard();
        String text = mapper.writeValueAsString(new BoardDto(board.cells()));
        System.out.println("text to send: " + text);
        for (WebSocketSession session : sessions) {
            try {
                session.sendMessage(new TextMessage(text));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
