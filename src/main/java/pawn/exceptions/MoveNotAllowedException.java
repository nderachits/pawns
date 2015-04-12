package pawn.exceptions;

/**
 * User: nike
 * Date: 4/13/15
 */
public class MoveNotAllowedException extends RuntimeException {
    public MoveNotAllowedException(String message) {
        super(message);
    }
}
