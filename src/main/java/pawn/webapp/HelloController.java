package pawn.webapp;


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

@Controller
@ComponentScan
@EnableAutoConfiguration
public class HelloController {

    @Value("${app.version}")
    private String version;

    @RequestMapping("/")
    public String home(Model model) {
        model.addAttribute("pawnversion", version);
        return "home";
    }

    @RequestMapping("/game/{gameId}")
    public String game(@PathVariable String gameId, Model model) {
        model.addAttribute("pawnversion", version);
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
}