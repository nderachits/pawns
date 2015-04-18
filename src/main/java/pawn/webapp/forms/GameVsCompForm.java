package pawn.webapp.forms;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * User: nike
 * Date: 4/18/15
 */
public class GameVsCompForm {

    @NotEmpty
    private String color;

    public void setColor(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}
