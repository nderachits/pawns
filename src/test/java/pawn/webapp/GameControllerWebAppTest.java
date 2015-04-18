package pawn.webapp;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.ConfigFileApplicationContextInitializer;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pawn.model.Game;
import pawn.model.dao.GameDaoInMemory;

import java.util.Arrays;

/**
 * User: Mikalai_Dzerachyts
 * Date: 8/28/14
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = PawnApplication.class, initializers = ConfigFileApplicationContextInitializer.class)
public class GameControllerWebAppTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private Environment environment;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private UserDetailsManager userDetailsManager;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void testGreeting() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Pawns home")))
                .andExpect(xpath("//input[@type='submit'][@value='New game']").exists())
                .andExpect(xpath("//input[@type='submit'][@value='New vs Computer']").exists())
                .andExpect(xpath("//input[@type='submit'][@value='New Computer vs Computer']").exists());
    }

    @Test
    public void newGameRedirectsToGamePage() throws Exception {
        authenticateAs("user1");

        MvcResult mvcResult = mockMvc.perform(post("/new"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/game/*")).andReturn();

        String gameId = mvcResult.getResponse().getRedirectedUrl().substring("/game/".length());
        Game game = new GameDaoInMemory().loadGameById(gameId);
        assertNotNull(game);
        assertEquals("user1", game.getWhitePlayer());
    }

    private void authenticateAs(String user) {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, "password",
                Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    public void joinAddsTheSecondPlayer() throws Exception{
        authenticateAs("user1");
        MvcResult mvcResult = mockMvc.perform(post("/new"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/game/*")).andReturn();

        String gameId = mvcResult.getResponse().getRedirectedUrl().substring("/game/".length());

        authenticateAs("user2");
        MvcResult mvcResult2 =mockMvc.perform(post("/join/" + gameId))
                .andExpect(status().is3xxRedirection())
                .andReturn();
        String gameIdFromJoin = mvcResult.getResponse().getRedirectedUrl().substring("/game/".length());

        assertEquals(gameId, gameIdFromJoin);

        Game game = new GameDaoInMemory().loadGameById(gameId);
        assertNotNull(game);
        assertEquals("user1", game.getWhitePlayer());
        assertEquals("user2", game.getBlackPlayer());
    }

    @Test
    public void registerCreatesNewUser() throws Exception{
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/register")
                    .param("userName", "newUser1")
                    .param("password", "password")
                    .param("passwordAgain", "password"))
                .andExpect(status().is3xxRedirection());


        UserDetails userDetails = userDetailsManager.loadUserByUsername("newUser1");
        assertNotNull(userDetails);
    }

    @Test
    public void propertiesAreAvailableForTest() {
        assertNotNull(environment.getProperty("app.version"));
    }

}
