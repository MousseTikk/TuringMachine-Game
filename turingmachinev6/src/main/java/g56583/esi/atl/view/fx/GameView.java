package g56583.esi.atl.view.fx;

import g56583.esi.atl.controller.fx.MainController;
import g56583.esi.atl.model.OO.Observer;
import g56583.esi.atl.model.Problem;
import g56583.esi.atl.model.validator.GenericValidator;
import g56583.esi.atl.model.validator.Validator;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.List;

/**
 * The GameView class represents the main game interface, displaying
 * the game state and allowing interaction with the user through various controls.
 * It extends BorderPane and implements the Observer interface to update
 * the view in response to changes in the game model.
 */
public class GameView extends BorderPane implements Observer {
    private final MainController controller;
    private HBox validatorsContainer;
    private Label scoreValueLabel;
    private ResultGrid resultGrid;

    /**
     * Constructs a GameView object and initializes the game interface.
     *
     * @param controller the MainController that manages the game logic and interaction
     */
    public GameView(MainController controller) {
        this.controller = controller;
        initializeComponents();
    }

    /**
     * Initializes the components of the game view, including the top,
     * middle, and bottom sections of the interface.
     */
    private void initializeComponents() {
        createTopSection();
        createMiddleSection();
        createBottomSection();
        updateValidatorsDisplay();
    }

    /**
     * Creates the top section of the game view, which includes the menu bar and game title.
     */
    private void createTopSection() {
        VBox topContainer = new VBox(10);
        topContainer.setBackground(new Background(new BackgroundFill(Color.GREEN, new CornerRadii(5), Insets.EMPTY)));
        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Options");
        MenuItem drawItem = new MenuItem("Draw");
        MenuItem leaveItem = new MenuItem("Leave");
        drawItem.setOnAction(e -> controller.handleDraw());
        leaveItem.setOnAction(e -> controller.handleLeave());
        menu.getItems().addAll(drawItem, leaveItem);
        menuBar.getMenus().add(menu);

        topContainer.getChildren().add(menuBar);
        topContainer.setAlignment(Pos.CENTER);
        topContainer.setPadding(new Insets(10));

        Label gameTitle = new Label("Turing Machine Game");
        gameTitle.getStyleClass().add("label-title");
        topContainer.getChildren().add(gameTitle);

        this.setTop(topContainer);
    }

    /**
     * Creates the middle section of the game view, which includes the validators,
     * code input, and buttons for controlling the game.
     */
    private void createMiddleSection() {
        VBox middleContainer = new VBox(20);
        middleContainer.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, new CornerRadii(5), Insets.EMPTY)));
        middleContainer.setAlignment(Pos.CENTER);
        middleContainer.setPadding(new Insets(15));

        validatorsContainer = new HBox(10);
        validatorsContainer.setAlignment(Pos.CENTER);
        validatorsContainer.setPadding(new Insets(10));
        middleContainer.getChildren().add(validatorsContainer);

        HBox mainContentContainer = new HBox(20);
        mainContentContainer.setAlignment(Pos.CENTER);
        mainContentContainer.setPadding(new Insets(15));

        VBox codeInputContainer = new VBox(10);
        codeInputContainer.setAlignment(Pos.TOP_LEFT);
        codeInputContainer.setPadding(new Insets(15));
        codeInputContainer.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, new CornerRadii(5), Insets.EMPTY)));

        CodeInputGrid codeInputGrid = new CodeInputGrid(controller);
        codeInputContainer.getChildren().add(codeInputGrid);

        VBox buttonContainer = new VBox(10);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.setPadding(new Insets(0, 20, 0, 20));
        Button nextRoundButton = new Button("Next Round");
        nextRoundButton.getStyleClass().add("button-custom");
        nextRoundButton.setOnAction(e -> controller.handleNextRound());
        Button undoButton = new Button("Undo");
        undoButton.getStyleClass().add("button-custom");
        undoButton.setOnAction(e -> controller.undo());
        Button redoButton = new Button("Redo");
        redoButton.getStyleClass().add("button-custom");
        redoButton.setOnAction(e -> controller.redo());
        Button checkButton = new Button("Check");
        checkButton.getStyleClass().add("button-custom");
        checkButton.setOnAction(e -> controller.check());
        Label scoreLabel = new Label("Score:");
        scoreLabel.getStyleClass().add("label-score");
        scoreValueLabel = new Label("0");
        scoreValueLabel.getStyleClass().add("label-score-value");

        buttonContainer.getChildren().addAll(nextRoundButton, undoButton, redoButton, checkButton, scoreLabel, scoreValueLabel);
        mainContentContainer.getChildren().addAll(codeInputContainer, buttonContainer);

        resultGrid = new ResultGrid(this::handleValidationClick);
        ScrollPane scrollPane = new ScrollPane(resultGrid);
        scrollPane.setFitToWidth(true);
        scrollPane.setMaxHeight(600);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        mainContentContainer.getChildren().add(scrollPane);

        middleContainer.getChildren().add(mainContentContainer);
        this.setCenter(middleContainer);
    }

    /**
     * Creates the bottom section of the game view.
     */
    private void createBottomSection() {
        HBox bottomContainer = new HBox(20);
        bottomContainer.setAlignment(Pos.CENTER);
        bottomContainer.setPadding(new Insets(15));
        bottomContainer.setBackground(new Background(new BackgroundFill(Color.GREEN, new CornerRadii(5), Insets.EMPTY)));

        this.setBottom(bottomContainer);
    }

    /**
     * Handles clicks on validator buttons, triggering validation for the specified round and validator.
     *
     * @param round          the round in which the validator is selected
     * @param validatorIndex the index of the validator being selected
     */
    private void handleValidationClick(int round, int validatorIndex) {
        controller.validateValidator(round, validatorIndex);
    }

    /**
     * Updates the display of the validators based on the current problem.
     */
    public void updateValidatorsDisplay() {
        validatorsContainer.getChildren().clear();
        Problem currentProblem = controller.getCurrentProblem();
        if (currentProblem != null) {
            List<Validator> validators = currentProblem.getValidators();
            for (int i = 0; i < validators.size(); i++) {
                String robotImagePath = "file:src/main/resources/robot" + (char) ('A' + i) + ".png";

                GenericValidator genericValidator = (GenericValidator) validators.get(i);
                String cardImagePath = "file:src/main/resources/card" + genericValidator.getValidatorNo() + ".png";

                ValidatorView validatorView = new ValidatorView(robotImagePath, cardImagePath);
                validatorView.setMaxWidth(150);
                validatorsContainer.getChildren().add(validatorView);
            }
        }
    }

    /**
     * Adds the result of a round to the result grid.
     *
     * @param round             the round number
     * @param code              the code entered during the round
     * @param currentValidators the list of validators for the current problem
     */
    public void addResult(int round, String code, List<Validator> currentValidators) {
        resultGrid.addResult(round, code, currentValidators);
    }

    /**
     * Displays an end game alert, showing whether the player has won or lost,
     * along with the final score and the number of rounds played.
     *
     * @param hasWon whether the player has won the game
     * @param score  the final score
     * @param rounds the number of rounds played
     */
    public void showEndGameAlert(boolean hasWon, int score, int rounds) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(hasWon ? "You win!" : "You lost");
        alert.setHeaderText(null);
        alert.setContentText(String.format("Score: %d\nRounds: %d", score, rounds));
        alert.showAndWait();
    }

    /**
     * Updates the view based on the notifications received from the observed game model.
     *
     * @param notification the notification message received from the game model
     */
    @Override
    public void update(String notification) {
        if (notification.startsWith("undoValidator:") || notification.startsWith("redoValidator:")) {
            String[] parts = notification.split(":");
            int round = Integer.parseInt(parts[1]);
            int validatorIndex = Integer.parseInt(parts[2]);
            if (notification.startsWith("undoValidator:")) {
                resultGrid.undoValidation(round, validatorIndex);
            } else if (notification.startsWith("redoValidator:")) {
                boolean[] results = controller.getGame().getValidationResultsForRoundSafe(round);
                resultGrid.updateResult(round, validatorIndex, results[validatorIndex]);
            }
        } else if (notification.startsWith("showAlert:")) {
            String[] parts = notification.split(":");
            showAlert(parts[1], parts[2]);
        } else {
            updateValidatorsDisplay();
        }
    }

    /**
     * Displays an alert with a specified title and content.
     *
     * @param title   the title of the alert
     * @param content the content of the alert
     */
    public void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Updates the result of a specific validator in a specific round.
     *
     * @param round          the round number
     * @param validatorIndex the index of the validator
     * @param result         the result of the validation (true for success, false for failure)
     */
    public void updateResult(int round, int validatorIndex, boolean result) {
        resultGrid.updateResult(round, validatorIndex, result);
    }

    /**
     * Removes the result of a specific round from the result grid.
     *
     * @param round the round number to be removed
     */
    public void removeResult(int round) {
        resultGrid.removeResult(round);
    }

    /**
     * Updates the score displayed in the game view.
     *
     * @param score the new score to be displayed
     */
    public void updateScore(int score) {
        scoreValueLabel.setText(String.valueOf(score));
    }

    /**
     * Resets the result grid, clearing all results.
     */
    public void resetResultGrid() {
        resultGrid.getChildren().clear();
        resultGrid.initializeGrid();
    }
}
