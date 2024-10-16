package g56583.esi.atl.controller.fx;

import g56583.esi.atl.model.GameFacade;
import g56583.esi.atl.model.Problem;
import g56583.esi.atl.model.validator.GenericValidator;
import g56583.esi.atl.model.validator.Validator;
import g56583.esi.atl.view.fx.GameView;
import g56583.esi.atl.view.fx.ProblemSelectorView;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.List;

/**
 * The MainController class is responsible for managing the game logic
 * and handling the interactions between the GameFacade (model) and the
 * JavaFX views (GameView and ProblemSelectorView). It controls the flow
 * of the game, including switching between views, handling user input,
 * and updating the UI.
 */
public class MainController {
    private final GameFacade game;
    private final Stage primaryStage;
    private final GameView gameView;
    private Scene primaryScene;

    /**
     * Constructs a MainController with the specified primary stage.
     * Initializes the GameFacade and the views, and sets up the initial scene.
     *
     * @param primaryStage The primary stage of the JavaFX application.
     */
    public MainController(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.game = new GameFacade("src/main/resources/known_problems.csv");
        this.gameView = new GameView(this);

        // Add observer to the game
        game.addObserver(gameView);

        ProblemSelectorView problemSelectorView = new ProblemSelectorView(this);
        this.primaryScene = new Scene(problemSelectorView, 1920, 1080);
    }

    /**
     * Sets the current scene to the provided scene.
     *
     * @param scene The scene to be set as the primary scene.
     */
    public void setScene(Scene scene) {
        this.primaryScene = scene;
    }

    /**
     * Switches the current view to the GameView, updating the scene root and displaying it.
     */
    public void switchToGameView() {
        primaryScene.setRoot(gameView);
        primaryStage.setScene(primaryScene);
        primaryStage.show();
    }

    /**
     * Switches the current view to the ProblemSelectorView, updating the scene root and displaying it.
     */
    public void switchToProblemSelectorView() {
        ProblemSelectorView problemSelectorView = new ProblemSelectorView(this);
        primaryScene.setRoot(problemSelectorView);
        primaryStage.setScene(primaryScene);
        primaryStage.show();
    }

    /**
     * Retrieves the list of problems from the GameFacade.
     *
     * @return A list of Problem objects.
     */
    public List<Problem> getProblems() {
        return game.getProblems();
    }

    /**
     * Retrieves the current problem being played.
     *
     * @return The current Problem object.
     */
    public Problem getCurrentProblem() {
        return game.getCurrentProblem();
    }

    /**
     * Handles the event when a problem is selected by the user.
     * Starts a new game with the selected problem and switches to the GameView.
     *
     * @param selectedIndex The index of the selected problem.
     */
    public void onPlaySelected(int selectedIndex) {
        game.startNewGame(selectedIndex);
        gameView.updateValidatorsDisplay();
        gameView.updateScore(game.getScore());
        switchToGameView();
    }

    /**
     * Handles the event when the user selects to play with a random problem.
     * Starts a new game with a randomly selected problem and switches to the GameView.
     */
    public void onRandomSelected() {
        game.startGameWithRandomProblem();
        gameView.updateValidatorsDisplay();
        gameView.updateScore(game.getScore());
        switchToGameView();
    }

    /**
     * Handles the event when the user chooses to move to the next round.
     * Advances the game to the next round and updates the GameView.
     */
    public void handleNextRound() {
        game.nextRound();
        gameView.updateValidatorsDisplay();
        gameView.updateScore(game.getScore());
    }

    /**
     * Handles the event when the user enters a code.
     * Submits the code to the GameFacade and updates the GameView with the results.
     *
     * @param code The code entered by the user.
     */
    public void enterCode(String code) {
        try {
            game.enterCode(code);
            gameView.addResult(game.getCurrentRound(), code, game.getCurrentValidators());
            gameView.updateScore(game.getScore());
        } catch (IllegalStateException e) {
            gameView.showAlert("Code Entry Error", "You have already entered a code for this round.");
        }
    }

    /**
     * Handles the undo operation, allowing the user to undo the last action.
     * Updates the GameView accordingly.
     */
    public void undo() {
        game.undo(game);
        gameView.updateValidatorsDisplay();
        gameView.updateScore(game.getScore());
        if (game.getCurrentUserInput() == null) {
            gameView.removeResult(game.getCurrentRound());
        }
    }

    /**
     * Handles the redo operation, allowing the user to redo an undone action.
     * Updates the GameView accordingly.
     */
    public void redo() {
        try {
            game.redo(game);
            gameView.updateValidatorsDisplay();
            gameView.updateScore(game.getScore());
            if (game.getCurrentUserInput() != null) {
                gameView.addResult(game.getCurrentRound(), game.getCurrentUserInput().getCode(), game.getCurrentValidators());
            }
        } catch (IllegalStateException e) {
            gameView.showAlert("Redo Error", e.getMessage());
        }
    }

    /**
     * Handles the check operation, where the user checks if their code guess is correct.
     * Displays the result and switches back to the ProblemSelectorView if the game ends.
     */
    public void check() {
        try {
            if (game.getCurrentUserInput() == null) {
                gameView.showAlert("Check Error", "No code entered.");
                return;
            }

            boolean hasWon = game.guessCode(game.getCurrentUserInput().getCode());
            gameView.showEndGameAlert(hasWon, game.getScore(), game.getCurrentRound());
            gameView.resetResultGrid();
            switchToProblemSelectorView();
        } catch (IllegalStateException e) {
            gameView.showAlert("Check Error", e.getMessage());
        }
    }

    /**
     * Validates a chosen validator during a specific round.
     * Updates the GameView with the result of the validation.
     *
     * @param round The round number in which the validator is chosen.
     * @param validatorIndex The index of the validator being validated.
     */
    public void validateValidator(int round, int validatorIndex) {
        if (round == game.getCurrentRound()) {
            try {
                Validator validator = game.getCurrentValidators().get(validatorIndex);
                int validatorNo = ((GenericValidator) validator).getValidatorNo();

                boolean validationResult = game.chooseValidator(validatorNo);

                boolean[] results = game.getValidationResultsForRoundSafe(round);
                results[validatorIndex] = validationResult;

                gameView.updateResult(round, validatorIndex, validationResult);
                gameView.updateScore(game.getScore());
            } catch (IllegalStateException e) {
                gameView.showAlert("Validator Error", e.getMessage());
            }
        }
    }

    /**
     * Handles the event when the user decides to draw or quit the game.
     * Resets the game state and switches back to the ProblemSelectorView.
     */
    public void handleDraw() {
        game.abandonGame();
        gameView.resetResultGrid();
        switchToProblemSelectorView();
    }

    /**
     * Handles the event when the user decides to leave the game.
     * Closes the primary stage of the application.
     */
    public void handleLeave() {
        primaryStage.close();
    }

    /**
     * Retrieves the GameFacade instance used by this controller.
     *
     * @return The GameFacade instance.
     */
    public GameFacade getGame() {
        return this.game;
    }
}
