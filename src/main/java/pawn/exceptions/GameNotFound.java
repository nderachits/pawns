package pawn.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * User: nike
 * Date: 3/18/15
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such game id")
public class GameNotFound extends RuntimeException {
    public GameNotFound(String message) {
        super(message);
    }
}
