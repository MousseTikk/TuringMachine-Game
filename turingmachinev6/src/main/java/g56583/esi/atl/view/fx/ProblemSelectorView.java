package g56583.esi.atl.view.fx;

import g56583.esi.atl.controller.fx.MainController;
import g56583.esi.atl.model.Problem;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.*;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * ProblemSelectorView represents the view where the user can select a problem
 * to start a new game or choose a random problem.
 */
public class ProblemSelectorView extends VBox {
    private ComboBox<String> problemSelector; // Dropdown menu to select a problem
    private final MainController controller; // Controller to handle game logic and UI updates

    /**
     * Constructs a ProblemSelectorView with the specified controller.
     *
     * @param controller The main controller handling the game logic and interactions.
     */
    public ProblemSelectorView(MainController controller) {
        super(10);
        this.controller = controller;

        // Configure the main layout of the ProblemSelectorView
        this.setAlignment(Pos.CENTER); // Center all elements in the VBox
        this.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
        this.setPrefSize(1800, 1000);

        // Create and configure the title image
        ImageView titleImageView = new ImageView(new Image("file:src/main/resources/turing_machine.png"));
        titleImageView.setFitWidth(300);
        titleImageView.setPreserveRatio(true);
        this.getChildren().add(titleImageView);

        // Initialize components of the view
        initializeComponents();

        // Populate the ComboBox with available problems
        populateProblems();

        // Add action buttons (Play and Random) below the ComboBox
        addButtons();
    }

    /**
     * Initializes the components of the ProblemSelectorView, such as the ComboBox.
     */
    private void initializeComponents() {
        problemSelector = new ComboBox<>();
        problemSelector.setPrefWidth(300); // Set preferred width for the ComboBox
        problemSelector.setPromptText("Select a Problem"); // Set placeholder text

        // Add some margin to the ComboBox for spacing
        VBox.setMargin(problemSelector, new Insets(0, 0, 20, 0));

        // Add the ComboBox to the VBox
        this.getChildren().add(problemSelector);
    }

    /**
     * Populates the ComboBox with the list of problems available in the controller.
     */
    private void populateProblems() {
        for (int i = 0; i < controller.getProblems().size(); i++) {
            Problem problem = controller.getProblems().get(i);
            String problemText = formatProblemText(i, problem);
            problemSelector.getItems().add(problemText); // Add formatted problem text to the ComboBox
        }
    }

    /**
     * Formats the problem text to ensure it doesn't exceed a certain length.
     * If it does, the text is truncated and appended with ellipsis.
     *
     * @param index   The index of the problem in the list.
     * @param problem The Problem object to be formatted.
     * @return A formatted string representing the problem.
     */
    private String formatProblemText(int index, Problem problem) {
        String baseText = index + ": " + problem.toString();
        if (baseText.length() > 50) {
            return baseText.substring(0, 47) + "...";
        } else {
            return baseText;
        }
    }

    /**
     * Adds the Play and Random buttons below the ComboBox.
     * These buttons allow the user to start the game with a selected or random problem.
     */
    private void addButtons() {
        Button playButton = new Button("Play");
        playButton.setOnAction(e -> {
            int selectedIndex = problemSelector.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                controller.onPlaySelected(selectedIndex);
            } else {
                showAlert();
            }
        });

        Button randomButton = new Button("Random");
        randomButton.setOnAction(e -> controller.onRandomSelected()); // Start game with a random problem

        // Create an HBox to arrange the buttons horizontally
        HBox buttonLayout = new HBox(10, playButton, randomButton);
        buttonLayout.setAlignment(Pos.CENTER);

        // Add the HBox with buttons to the VBox
        this.getChildren().add(buttonLayout);
    }

    /**
     * Displays an alert if the user attempts to start a game without selecting a problem.
     */
    private void showAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText("Select a problem or press 'random'");
        alert.showAndWait(); // Show the alert and wait for user interaction
    }
}
