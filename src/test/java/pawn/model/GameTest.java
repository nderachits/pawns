package pawn.model;

import org.junit.Before;
import org.junit.Test;
import pawn.exceptions.MoveNotAllowedException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class GameTest {

    private Game game = new Game("g1");

    @Before
    public void setUp() throws Exception {
        game.setWhitePlayer("user1");
        game.setBlackPlayer("user2");
    }

    @Test
    public void shouldReturnBoard() throws Exception {
        assertFalse(game.isJoinAvailableFor("user1"));
        assertNotNull(game);
        assertEquals(9, game.size());
        assertArrayEquals(new Cell[]{
                Cell.black, Cell.black, Cell.black,
                Cell.empty, Cell.empty, Cell.empty,
                Cell.white, Cell.white, Cell.white}, game.cells());
        assertFalse(game.isGameFinished());
        assertTrue(game.isNextMoveWhite());

    }

    @Test
    public void moveUpdatesBoard() {
        game.saveMove(6, 3, "user1");
        assertArrayEquals(new Cell[]{
                Cell.black, Cell.black, Cell.black,
                Cell.white, Cell.empty, Cell.empty,
                Cell.empty, Cell.white, Cell.white}, game.cells());
        assertFalse(game.isNextMoveWhite());
    }

    @Test
    public void moveCallsListener() {
        class MoveListenerStub implements MoveListener{
            boolean boardUpdated=false;
            public void boardUpdated(String gameId) {
                boardUpdated = true;
            }
        }
        MoveListenerStub moveListenerStub = new MoveListenerStub();

        game.setMoveListener(moveListenerStub);
        game.setBlackPlayerComp(new RandomCompPlayer(game));
        game.saveMove(6, 3, "user1");
        assertThat(moveListenerStub.boardUpdated, is(true));
    }

    @Test
    public void attackRemovePawn() {
        game.saveMove(6, 3, "user1");
        game.saveMove(1, 3, "user2");
        assertArrayEquals(new Cell[]{
                Cell.black, Cell.empty, Cell.black,
                Cell.black, Cell.empty, Cell.empty,
                Cell.empty, Cell.white, Cell.white}, game.cells());
    }

    @Test(expected = IllegalStateException.class)
    public void throwExceptionWhenStartingCellOfMoveIsEmpty() {
        game.saveMove(3, 0, "user1");
    }

    @Test
    public void playersAreSaved() {
        game.setWhitePlayer("user1");
        assertEquals("user1", game.getWhitePlayer());
        game.setBlackPlayer("user2");
        assertEquals("user2", game.getBlackPlayer());
    }

    @Test(expected = IllegalStateException.class)
    public void gameBetweenUniquePlayers() {
        game.setWhitePlayer("user1");
        game.setBlackPlayer("user1");
    }

    @Test(expected = MoveNotAllowedException.class)
    public void moveThrowsForNonPlayer() {
        game.saveMove(6, 3, "user3");
    }

    @Test(expected = MoveNotAllowedException.class)
    public void whiteMoveThrowsForBlackPawn() {
        game.saveMove(0, 3, "user1");
    }

    @Test(expected = MoveNotAllowedException.class)
    public void blackMoveThrowsForWhitePawn() {
        game.saveMove(6, 3, "user2");
    }

    @Test
    public void gameWithOnePlayerIsJoinAvalable() {
        Game game2 = new Game("g2");
        game2.setBlackPlayer("user1");
        assertTrue(game2.isJoinAvailableFor("user2"));
        Game game3 = new Game("g3");
        game3.setWhitePlayer("user1");
        assertTrue(game3.isJoinAvailableFor("user2"));
        Game game4 = new Game("g4");
        game4.setWhitePlayer("user1");
        assertFalse(game4.isJoinAvailableFor("user1"));
    }

    @Test
    public void oneMoveAtATime() {
        assertTrue(game.isMyMoveNext("user1"));
        assertFalse(game.isMyMoveNext("user2"));
        game.saveMove(6, 3, "user1");
        assertTrue(game.isMyMoveNext("user2"));
        assertFalse(game.isMyMoveNext("user1"));
    }

    @Test(expected = MoveNotAllowedException.class)
    public void moveAtOpponentsTurnThrows() {
        game.saveMove(0, 3, "user2");
    }

    @Test(expected = MoveNotAllowedException.class)
    public void straitMoveOnlyForPawn() {
        game.saveMove(6, 4, "user1");
    }

    @Test(expected = MoveNotAllowedException.class)
    public void oneCellMoveOnlyForPawn() {
        game.saveMove(6, 0, "user1");
    }

    @Test(expected = MoveNotAllowedException.class)
    public void forwardMoveOnlyForPawn() {
        game.saveMove(6, 3, "user1");
        game.saveMove(2, 5, "user2");
        game.saveMove(3, 6, "user1");
    }

    @Test(expected = MoveNotAllowedException.class)
    public void noAttackingOwnPawns() {
        game.saveMove(6, 3, "user1");
        game.saveMove(2, 5, "user2");
        game.saveMove(7, 3, "user1");
    }

    @Test(expected = MoveNotAllowedException.class)
    public void attackIsDiagonalMove() {
        game.saveMove(6, 3, "user1");
        game.saveMove(0, 3, "user2");

    }

    @Test
    public void endOfgameIsIdentified() {
        game.saveMove(6, 3, "user1");
        game.saveMove(1, 4, "user2");
        game.saveMove(8, 5, "user1");
        assertTrue(game.isGameFinished());
    }
}


