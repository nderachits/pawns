package pawn.webapp;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pawn.model.dao.BoardDaoInMemory;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * User: Mikalai_Dzerachyts
 * Date: 8/28/14
 */
public class HelloControllerStandaloneTest {

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        HelloController helloController = new HelloController();
        helloController.setBoardDao(new BoardDaoInMemory());
        mockMvc = MockMvcBuilders.standaloneSetup(helloController).build();
    }

    @Test
    public void rootShouldReturnHelloWorldString() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"));

    }
}
