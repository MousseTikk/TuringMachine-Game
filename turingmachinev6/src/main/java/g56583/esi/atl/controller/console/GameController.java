package g56583.esi.atl.controller.console;

import g56583.esi.atl.model.GameFacade;
import g56583.esi.atl.model.OO.Observer;
import g56583.esi.atl.view.console.ConsoleView;

import java.util.Arrays;

/**
 * The GameController class is responsible for managing the game logic and interactions
 * between the GameFacade (model) and the ConsoleView (view). It implements the Observer
 * interface to receive updates from the model and update the view accordingly.
 */
public class GameController implements Observer {
    private final GameFacade game;
    private final ConsoleView view;
    private boolean codeEntered = false;

    /**
     * Constructs a GameController with the specified GameFacade and ConsoleView.
     *
     * @param game  The GameFacade that handles the game logic.
     * @param view  The ConsoleView that handles the user interface.
     */
    public GameController(GameFacade game, ConsoleView view) {
        this.game = game;
        this.view = view;
        this.view.setGame(game);
        this.game.addObserver(this);
    }

    /**
     * Starts the game, managing the main game loop. The player can select a problem,
     * enter commands, and interact with the game until they choose to quit.
     */
    public void startGame() {
        view.displayWelcomeMessage();
        boolean keepPlaying = true;

        // Main game loop
        while (keepPlaying) {
            view.displayProblems();
            int choice = view.getUserProblemChoice();

            if (choice == -1) {
                game.startGameWithRandomProblem();
            } else {
                try {
                    game.startNewGame(choice);
                } catch (IllegalArgumentException e) {
                    view.displayErrorMessage("Invalid problem index. Please choose a valid number.");
                    continue;
                }
            }
            codeEntered = false;
            view.displayCurrentState();

            // Active game session
            while (game.isGameActive()) {
                String command = view.getUserCommand();
                processCommand(command);
            }

            // After the game session ends
            if (!game.isGameActive() && !askIfUserWantsToPlayAgain()) {
                keepPlaying = false; // Exit if the player does not want to continue
            }
        }
    }

    /**
     * Processes the command entered by the user.
     *
     * @param command The command string entered by the user.
     */
    private void processCommand(String command) {
        String[] cmdParts = command.split(" ");
        switch (cmdParts[0]) {
            case "enter" -> handleEnterCommand(cmdParts);
            case "choose" -> handleChooseCommand(cmdParts);
            case "check" -> handleCheckCommand();
            case "abandon" -> game.abandonGame();
            case "undo" -> {
                game.undo(game);
                resetCodeEnteredState();
                view.displayMessage("Last action undone.");
            }
            case "redo" -> {
                game.redo(game);
                resetCodeEnteredState();
                view.displayMessage("Last action redone.");
            }
            case "leave" -> System.exit(0);
            case "validators" -> view.displayValidators();
            case "next" -> {
                game.nextRound();
                codeEntered = false;
                view.displayCurrentState();
            }
            default -> view.displayErrorMessage("Invalid command or sequence of actions.");
        }
    }

    /**
     * Handles the "enter" command to submit a code.
     *
     * @param cmdParts The parts of the command, where the code is specified.
     */
    private void handleEnterCommand(String[] cmdParts) {
        if (!codeEntered) {
            try {
                String code = String.join("", Arrays.copyOfRange(cmdParts, 1, cmdParts.length));
                game.enterCode(code);
                codeEntered = true;
                view.displayMessage("Code entered successfully.");
            } catch (Exception e) {
                view.displayErrorMessage("Invalid code: " + e.getMessage());
            }
        } else {
            view.displayErrorMessage("Code already entered for this round.");
        }
    }

    /**
     * Handles the "choose" command to select a validator.
     *
     * @param cmdParts The parts of the command, where the validator number is specified.
     */
    private void handleChooseCommand(String[] cmdParts) {
        if (!codeEntered) {
            view.displayErrorMessage("Please enter a code before choosing a validator.");
            return;
        }
        handleValidatorChoice(cmdParts[1]);
    }

    /**
     * Handles the "check" command to verify the entered code.
     */
    private void handleCheckCommand() {
        if (!codeEntered) {
            view.displayErrorMessage("Please enter a code before checking.");
            return;
        }
        boolean hasWon = game.guessCode(game.getCurrentUserInput().getCode());
        view.displayEndGame(hasWon);
    }

    /**
     * Handles the selection of a validator by its number.
     *
     * @param validatorNumber The number of the validator to be selected.
     */
    private void handleValidatorChoice(String validatorNumber) {
        try {
            int validatorNo = Integer.parseInt(validatorNumber);
            if (game.chooseValidator(validatorNo)) {
                view.displayMessage("Validator " + validatorNo + " passed.");
            } else {
                view.displayErrorMessage("Validator " + validatorNo + " failed.");
            }
        } catch (NumberFormatException e) {
            view.displayErrorMessage("Invalid validator number format.");
        } catch (Exception e) {
            view.displayErrorMessage(e.getMessage());
        }
    }

    /**
     * Resets the state indicating whether a code has been entered in the current round.
     */
    private void resetCodeEnteredState() {
        codeEntered = game.getCurrentUserInput() != null;
    }

    /**
     * Asks the user if they want to play again after a game ends.
     *
     * @return true if the user wants to play again, false otherwise.
     */
    private boolean askIfUserWantsToPlayAgain() {
        view.displayMessage("Do you want to play again? (yes/no)");
        String response = view.getUserCommand().trim().toLowerCase();
        return response.equals("yes") || response.equals("y");
    }

    /**
     * Updates the view based on notifications from the GameFacade.
     *
     * @param update The update string sent by the GameFacade.
     */
    @Override
    public void update(String update) {
        switch (update) {
            case "codeEntered" -> view.displayMessage("Code entered successfully.");
            case "validatorPassed" -> view.displayMessage("Validator passed.");
            case "validatorFailed" -> view.displayMessage("Validator failed.");
            case "undoPerformed" -> view.displayMessage("Undo performed.");
            case "redoPerformed" -> view.displayMessage("Redo performed.");
            case "undoError" -> view.displayErrorMessage("Undo not possible.");
            case "redoError" -> view.displayErrorMessage("Redo not possible.");
            case "newGameStarted" -> view.displayMessage("New game started.");
            case "gameAbandoned" -> view.displayMessage("Game abandoned.");
        }
    }
}
