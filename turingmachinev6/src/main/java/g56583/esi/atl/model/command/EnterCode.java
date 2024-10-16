package g56583.esi.atl.model.command;

import g56583.esi.atl.model.Code;
import g56583.esi.atl.model.GameFacade;

/**
 * The EnterCode class implements the Command interface and represents
 * the action of entering a code in the game. It stores the previous state
 * before the command is executed, allowing for undo and redo operations.
 */
public class EnterCode implements Command {
    private final GameFacade game;
    private final String code;
    private Code previousCode;

    /**
     * Constructs an EnterCode command with the specified game and code.
     *
     * @param game The GameFacade instance managing the game logic.
     * @param code The code to be entered.
     */
    public EnterCode(GameFacade game, String code) {
        this.game = game;
        this.code = code;
    }

    /**
     * Executes the command by entering the code in the game and resetting
     * the validator count. Stores the previous state for undo/redo.
     *
     * @throws IllegalStateException If the game is not active.
     */
    @Override
    public void execute() {
        if (!game.isGameActive()) {
            throw new IllegalStateException("Game is not active.");
        }
        previousCode = game.getCurrentUserInput();
        game.setCurrentUserInput(new Code(code));
        game.resetValidatorCount();
    }

    /**
     * Undoes the command by reverting the code and validator count to their previous states.
     */
    @Override
    public void undo() {
        game.setCurrentUserInput(previousCode);
        game.resetValidatorCount();
    }

    /**
     * Redoes the command by re-entering the code and resetting the validator count.
     */
    @Override
    public void redo() {
        execute();
    }
}
