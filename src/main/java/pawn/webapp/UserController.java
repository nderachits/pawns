package pawn.webapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pawn.webapp.forms.UserForm;

import javax.validation.Valid;
import java.util.Arrays;

/**
 * User: nike
 * Date: 4/11/15
 */
@Controller
public class UserController {

    @Autowired
    private UserDetailsManager userDetailsManager;

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register(Model model) {
        model.addAttribute("form", new UserForm());
        return "register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String registerHandler(@Valid UserForm form, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return null;
        }
        UserDetails user = new User(form.getUserName(), form.getPassword(), Arrays.<GrantedAuthority>asList(new SimpleGrantedAuthority("ROLE_USER")));
        userDetailsManager.createUser(user);
        return "redirect:/";
    }


    public void setUserDetailsManager(UserDetailsManager userDetailsManager) {
        this.userDetailsManager = userDetailsManager;
    }
}
