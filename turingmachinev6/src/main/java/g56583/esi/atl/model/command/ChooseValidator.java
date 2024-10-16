package g56583.esi.atl.model.command;

import g56583.esi.atl.model.GameFacade;
import g56583.esi.atl.model.validator.GenericValidator;

/**
 * The ChooseValidator class implements the Command interface and represents
 * the action of selecting a validator to validate the user's code in the game.
 * It stores the state before and after the command is executed, allowing for
 * undo and redo operations.
 */
public class ChooseValidator implements Command {
    private final GameFacade game;
    private final int validatorNo;
    private int previousValidatorCount;
    private int previousScore;
    private boolean validationResult;

    /**
     * Constructs a ChooseValidator command with the specified game and validator number.
     *
     * @param game        The GameFacade instance managing the game logic.
     * @param validatorNo The number of the validator to be chosen.
     */
    public ChooseValidator(GameFacade game, int validatorNo) {
        this.game = game;
        this.validatorNo = validatorNo;
    }

    /**
     * Executes the command by selecting a validator and performing the validation.
     * Updates the score and the count of selected validators.
     *
     * @throws IllegalStateException If the game is not active or if the maximum number
     *                               of validators has already been chosen.
     */
    @Override
    public void execute() {
        if (!game.isGameActive()) {
            throw new IllegalStateException("Game is not active.");
        }

        if (game.getValidatorCount() >= 3) {
            throw new IllegalStateException("Maximum of 3 validators already chosen for this code.");
        }

        GenericValidator validator = (GenericValidator) game.getCurrentProblem().getValidators().stream()
                .filter(v -> v instanceof GenericValidator && ((GenericValidator) v).getValidatorNo() == validatorNo)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Validator number " + validatorNo + " is not available."));

        previousValidatorCount = game.getValidatorCount();
        previousScore = game.getScore();

        validationResult = validator.validate(game.getCurrentUserInput(), game.getCurrentProblem().getCode());
        game.incrementScore(1);
        game.incrementValidatorCount();
    }

    /**
     * Undoes the command by reverting the validator count and score to their previous states.
     */
    @Override
    public void undo() {
        game.setValidatorCount(previousValidatorCount);
        game.setScore(previousScore);

        boolean[] results = game.getValidationResultsForRoundSafe(game.getCurrentRound());
        results[game.getValidatorIndexByNumber(validatorNo)] = false;

        game.notifyObservers("undoValidator:" + game.getCurrentRound() + ":" + game.getValidatorIndexByNumber(validatorNo));
    }

    /**
     * Redoes the command by restoring the validator count, score, and validation result.
     */
    @Override
    public void redo() {
        game.setValidatorCount(previousValidatorCount + 1);
        game.setScore(previousScore + 1);

        boolean[] results = game.getValidationResultsForRoundSafe(game.getCurrentRound());
        results[game.getValidatorIndexByNumber(validatorNo)] = validationResult;

        game.notifyObservers("redoValidator:" + game.getCurrentRound() + ":" + game.getValidatorIndexByNumber(validatorNo));
    }

    /**
     * Returns the result of the validation.
     *
     * @return true if the validation passed, false otherwise.
     */
    public boolean getValidationResult() {
        return validationResult;
    }
}
