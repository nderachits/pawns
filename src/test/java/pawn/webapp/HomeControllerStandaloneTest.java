package pawn.webapp;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pawn.model.dao.GameDaoInMemory;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * User: Mikalai_Dzerachyts
 * Date: 8/28/14
 */
public class HomeControllerStandaloneTest {

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        HomeController homeController = new HomeController();
        homeController.setGameDao(new GameDaoInMemory());
        mockMvc = MockMvcBuilders.standaloneSetup(homeController).build();
    }

    @Test
    public void rootShouldReturnHelloWorldString() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"));

    }
}
