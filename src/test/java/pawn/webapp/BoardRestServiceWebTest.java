package pawn.webapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.ConfigFileApplicationContextInitializer;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pawn.model.dto.MoveDto;
import pawn.model.Board;
import pawn.model.Cell;
import pawn.model.dao.BoardDao;
import pawn.model.dao.BoardDaoInMemory;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertArrayEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * User: nike
 * Date: 2/19/15
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = HelloController.class, initializers = ConfigFileApplicationContextInitializer.class)
public class BoardRestServiceWebTest {
    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    private String gameId;

    @Before
    public void setUp() throws Exception {
        BoardDao boardDao = new BoardDaoInMemory();
        gameId = boardDao.newGameId();
        mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void boardServiceReturnsBoard() throws Exception {
        mockMvc.perform(get("/board/"+gameId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cells").isArray())
                .andExpect(jsonPath("$.cells", hasSize(9)))
                .andExpect(jsonPath("$.cells", contains(
                        is(Cell.black.name()), is(Cell.black.name()), is(Cell.black.name()),
                        is(Cell.empty.name()), is(Cell.empty.name()), is(Cell.empty.name()),
                        is(Cell.white.name()), is(Cell.white.name()), is(Cell.white.name()))));
    }

    @Test
    public void modelChangesBoardJson() throws Exception {
        BoardDao boardDao = new BoardDaoInMemory();
        Board board = boardDao.loadBoardById(gameId);
        board.saveMove(7, 4);

        mockMvc.perform(get("/board/"+gameId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cells").isArray())
                .andExpect(jsonPath("$.cells", hasSize(9)))
                .andExpect(jsonPath("$.cells", contains(
                        is(Cell.black.name()), is(Cell.black.name()), is(Cell.black.name()),
                        is(Cell.empty.name()), is(Cell.white.name()), is(Cell.empty.name()),
                        is(Cell.white.name()), is(Cell.empty.name()), is(Cell.white.name()))));
    }

    @Test
    public void saveMoveViaRestChangesBoardModel() throws Exception {
        mockMvc.perform(post("/move/"+gameId)
                .contentType(MediaType.APPLICATION_JSON)
                .content( new ObjectMapper().writeValueAsString(new MoveDto(7,4))))
                .andExpect(status().isOk());

        BoardDao boardDao = new BoardDaoInMemory();
        Board board = boardDao.loadBoardById(gameId);
        assertArrayEquals(new Cell[]{
                Cell.black, Cell.black, Cell.black,
                Cell.empty, Cell.white, Cell.empty,
                Cell.white, Cell.empty, Cell.white}, board.cells());
    }

    @Test
    public void saveMoveViaRestChangesBoardJson() throws Exception {
        mockMvc.perform(post("/move/"+gameId)
                .contentType(MediaType.APPLICATION_JSON)
                .content( new ObjectMapper().writeValueAsString(new MoveDto(7,4))))
                .andExpect(status().isOk());

        mockMvc.perform(get("/board/"+gameId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cells").isArray())
                .andExpect(jsonPath("$.cells", hasSize(9)))
                .andExpect(jsonPath("$.cells", contains(
                        is(Cell.black.name()), is(Cell.black.name()), is(Cell.black.name()),
                        is(Cell.empty.name()), is(Cell.white.name()), is(Cell.empty.name()),
                        is(Cell.white.name()), is(Cell.empty.name()), is(Cell.white.name()))));
    }

    @Test
    public void newGameResetsBoardJson() throws Exception {
        Board board = new BoardDaoInMemory().loadBoardById(gameId);
        board.saveMove(7, 4);

        MvcResult mvcResult = mockMvc.perform(post("/newgameid")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        String gameId2 = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.gameId");

        Board anotherBoard = new BoardDaoInMemory().loadBoardById(gameId2);
        assertArrayEquals(new Cell[]{
                Cell.black, Cell.black, Cell.black,
                Cell.empty, Cell.empty, Cell.empty,
                Cell.white, Cell.white, Cell.white}, anotherBoard.cells());
    }

    @Test
    public void gameNotFoundThrowsError() throws Exception {
        mockMvc.perform(get("/board/game_not_exist"))
                .andExpect(status().isNotFound());
    }
}
