package g56583.esi.atl.view.fx;

import g56583.esi.atl.controller.fx.MainController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 * CodeInputGrid represents the grid interface where users can input and test their code.
 */
public class CodeInputGrid extends VBox {
    private final MainController controller;

    /**
     * Constructs a CodeInputGrid with a reference to the MainController.
     *
     * @param controller the controller that handles the logic behind the UI
     */
    public CodeInputGrid(MainController controller) {
        super(10); // Spacing between elements in the VBox
        this.controller = controller;
        this.setAlignment(Pos.TOP_LEFT);
        this.setPadding(new Insets(15));
        this.getStyleClass().add("code-input-container");

        // Instruction label
        Label instructionLabel = new Label("Test a Code");
        instructionLabel.getStyleClass().add("instruction-label");
        this.getChildren().add(instructionLabel);

        // Label to prompt user to enter a code
        Label enterCodeLabel = new Label("Enter your code:");
        enterCodeLabel.getStyleClass().add("enter-code-label");
        this.getChildren().add(enterCodeLabel);

        // Grid to hold the buttons and labels
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(15));
        grid.setHgap(10);
        grid.setVgap(10);
        grid.getStyleClass().add("code-input-grid");

        // Add shape labels to the grid
        Label triangleLabel = new Label("▲");
        triangleLabel.setAlignment(Pos.CENTER);
        triangleLabel.getStyleClass().addAll("label-shape", "label-triangle");

        Label squareLabel = new Label("■");
        squareLabel.setAlignment(Pos.CENTER);
        squareLabel.getStyleClass().addAll("label-shape", "label-square");

        Label circleLabel = new Label("●");
        circleLabel.setAlignment(Pos.CENTER);
        circleLabel.getStyleClass().addAll("label-shape", "label-circle");

        grid.add(triangleLabel, 0, 0);
        grid.add(squareLabel, 1, 0);
        grid.add(circleLabel, 2, 0);

        // Create and add buttons to the grid
        Button[][] buttons = new Button[5][3];
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 3; col++) {
                Button button = new Button(String.valueOf(5 - row));
                button.getStyleClass().add("button-grid");
                final int fRow = row;
                final int fCol = col;
                button.setOnAction(e -> handleButtonSelection(buttons, fRow, fCol));
                grid.add(button, col, row + 1);
                buttons[row][col] = button;
            }
        }

        // Submit button to confirm code selection
        Button submitButton = new Button("Choose");
        submitButton.getStyleClass().add("button-submit");
        submitButton.setOnAction(e -> handleSubmit(buttons));
        GridPane.setColumnSpan(submitButton, 3);
        grid.add(submitButton, 0, 6, 3, 1);

        this.getChildren().add(grid);
    }

    /**
     * Handles button selection in the grid, ensuring only one button per column is selected.
     *
     * @param buttons The grid of buttons.
     * @param row     The row of the selected button.
     * @param col     The column of the selected button.
     */
    private void handleButtonSelection(Button[][] buttons, int row, int col) {
        // Clear the selection for the entire column
        for (int i = 0; i < 5; i++) {
            buttons[i][col].setStyle("");
        }
        // Highlight the selected button
        buttons[row][col].setStyle("-fx-background-color: gray;");
    }

    /**
     * Handles the submission of the selected code.
     *
     * @param buttons The grid of buttons.
     */
    private void handleSubmit(Button[][] buttons) {
        StringBuilder codeBuilder = new StringBuilder();
        boolean validCode = true;

        // Build the code string from the selected buttons
        for (int col = 0; col < 3; col++) {
            boolean found = false;
            for (int row = 0; row < 5; row++) {
                if ("-fx-background-color: gray;".equals(buttons[row][col].getStyle())) {
                    codeBuilder.append(5 - row);
                    found = true;
                    break;
                }
            }
            if (!found) {
                validCode = false;
                break;
            }
        }

        String code = codeBuilder.toString();

        // If valid code is entered, submit it, otherwise show an error
        if (validCode && code.length() == 3) {
            controller.enterCode(code);
            resetButtonStyles(buttons); // Reset the grid after submission
        } else {
            showError("Invalid Code", "Please select exactly three digits between 1 and 5.");
        }
    }

    /**
     * Resets the styles of all buttons in the grid to clear any selection.
     *
     * @param buttons The grid of buttons.
     */
    private void resetButtonStyles(Button[][] buttons) {
        // Clear all styles, removing any "gray" background
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 3; col++) {
                buttons[row][col].setStyle("");
            }
        }
    }

    /**
     * Displays an error alert with the specified title and content.
     *
     * @param title   The title of the alert.
     * @param content The content of the alert.
     */
    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
