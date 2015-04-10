package pawn.webapp.forms;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.AssertTrue;

/**
 * User: nike
 * Date: 4/10/15
 */
public class UserForm {

    @NotBlank
    private String userName;

    @NotBlank
    private String password;

    @NotBlank
    private String passwordAgain;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordAgain() {
        return passwordAgain;
    }

    @AssertTrue
    public boolean isPasswordsMatches() {
        return password!=null && password.equals(passwordAgain);
    }

    public void setPasswordAgain(String passwordAgain) {
        this.passwordAgain = passwordAgain;
    }
}
