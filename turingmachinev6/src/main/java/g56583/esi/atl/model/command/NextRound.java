package g56583.esi.atl.model.command;

import g56583.esi.atl.model.GameFacade;
import g56583.esi.atl.model.Code;

/**
 * The NextRound class implements the Command interface and represents
 * the action of moving to the next round in the game. It stores the previous
 * state before the command is executed, allowing for undo and redo operations.
 */
public class NextRound implements Command {
    private GameFacade game;
    private Code previousUserInput;
    private int previousValidatorCount;
    private int previousRound;
    private int previousScore;

    /**
     * Constructs a NextRound command with the specified game.
     *
     * @param game The GameFacade instance managing the game logic.
     */
    public NextRound(GameFacade game) {
        this.game = game;
    }

    /**
     * Executes the command by moving the game to the next round.
     * Stores the previous state for undo/redo.
     */
    @Override
    public void execute() {
        previousUserInput = game.getCurrentUserInput();
        previousValidatorCount = game.getValidatorCount();
        previousRound = game.getCurrentRound();
        previousScore = game.getScore();

        game.setCurrentUserInput(null);
        game.resetValidatorCount();
        game.incrementCurrentRound();
    }

    /**
     * Undoes the command by reverting the game state to the previous round.
     */
    @Override
    public void undo() {
        game.setCurrentUserInput(previousUserInput);
        game.setValidatorCount(previousValidatorCount);
        game.setCurrentRound(previousRound);
        game.setScore(previousScore);
    }

    /**
     * Redoes the command by moving the game forward to the next round again.
     */
    @Override
    public void redo() {
        execute();
    }
}
