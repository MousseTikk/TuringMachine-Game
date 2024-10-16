package g56583.esi.atl.view.fx;

import g56583.esi.atl.model.validator.Validator;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * The ResultGrid class represents the grid where the results of each round
 * and the validation by each validator are displayed.
 * It extends GridPane to organize the results in a grid format.
 */
public class ResultGrid extends GridPane {
    private int currentRow = 1;
    private final Map<Integer, Integer> roundRowMap = new HashMap<>();
    private final BiConsumer<Integer, Integer> validationClickHandler;

    /**
     * Constructs a ResultGrid object and initializes the grid layout.
     *
     * @param validationClickHandler a handler function to manage clicks on validation buttons
     */
    public ResultGrid(BiConsumer<Integer, Integer> validationClickHandler) {
        this.validationClickHandler = validationClickHandler;
        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(15));
        this.setHgap(10);
        this.setVgap(10);
        this.getStyleClass().add("result-grid");
        initializeGrid();
    }

    /**
     * Initializes the grid with headers for rounds, codes, and validators.
     */
    public void initializeGrid() {
        // Header
        Label roundLabel = new Label("Round");
        Label codeLabel = new Label("Code");
        Label aLabel = new Label("A");
        Label bLabel = new Label("B");
        Label cLabel = new Label("C");
        Label dLabel = new Label("D");
        Label eLabel = new Label("E");
        Label fLabel = new Label("F");
        this.add(roundLabel, 0, 0);
        this.add(codeLabel, 1, 0);
        this.add(aLabel, 2, 0);
        this.add(bLabel, 3, 0);
        this.add(cLabel, 4, 0);
        this.add(dLabel, 5, 0);
        this.add(eLabel, 6, 0);
        this.add(fLabel, 7, 0);
        setHalignment(roundLabel, HPos.CENTER);
        setHalignment(codeLabel, HPos.CENTER);
        setHalignment(aLabel, HPos.CENTER);
        setHalignment(bLabel, HPos.CENTER);
        setHalignment(cLabel, HPos.CENTER);
        setHalignment(dLabel, HPos.CENTER);
        setHalignment(eLabel, HPos.CENTER);
        setHalignment(fLabel, HPos.CENTER);
    }

    /**
     * Adds a result for a specific round to the grid, including the code and validation buttons.
     *
     * @param round             the round number
     * @param code              the code entered in the round
     * @param currentValidators the list of validators for the current problem
     */
    public void addResult(int round, String code, List<Validator> currentValidators) {
        if (!roundRowMap.containsKey(round)) {
            Label roundLabel = new Label(String.valueOf(round));
            roundLabel.getStyleClass().add("result-grid-round");
            Label codeLabel = new Label(code);
            codeLabel.getStyleClass().add("result-grid-code");

            this.add(roundLabel, 0, currentRow);
            this.add(codeLabel, 1, currentRow);

            for (int i = 0; i < currentValidators.size(); i++) {
                Button validateButton = new Button(" ");
                int currentRound = round;
                int validatorIndex = i;
                validateButton.setOnAction(e -> validationClickHandler.accept(currentRound, validatorIndex));
                this.add(validateButton, i + 2, currentRow);
            }

            roundRowMap.put(round, currentRow);
            currentRow++;
        }
    }

    /**
     * Updates the result of a specific validator in a specific round in the grid.
     *
     * @param round          the round number
     * @param validatorIndex the index of the validator
     * @param result         the result of the validation (true for success, false for failure)
     */
    public void updateResult(int round, int validatorIndex, boolean result) {
        Integer row = roundRowMap.get(round);
        if (row != null && validatorIndex < 6) {
            this.getChildren().removeIf(node -> GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == validatorIndex + 2 && node instanceof Rectangle);

            Rectangle resultRect = new Rectangle(20, 20);
            resultRect.setFill(result ? Color.GREEN : Color.RED);
            this.add(resultRect, validatorIndex + 2, row);
        }
    }

    /**
     * Removes the results of a specific round from the grid.
     *
     * @param round the round number to be removed
     */
    public void removeResult(int round) {
        Integer row = roundRowMap.get(round);
        if (row != null) {
            this.getChildren().removeIf(node -> GridPane.getRowIndex(node) == row);
            roundRowMap.remove(round);
        }
    }

    /**
     * Undoes the validation of a specific validator in a specific round,
     * removing the result and restoring the validation button.
     *
     * @param round          the round number
     * @param validatorIndex the index of the validator
     */
    public void undoValidation(int round, int validatorIndex) {
        Integer row = roundRowMap.get(round);
        if (row != null && validatorIndex < 6) {
            this.getChildren().removeIf(node -> GridPane.getRowIndex(node) == row
                    && GridPane.getColumnIndex(node) == validatorIndex + 2
                    && node instanceof Rectangle);

            Button validateButton = new Button(" ");
            validateButton.setOnAction(e -> validationClickHandler.accept(round, validatorIndex));
            this.add(validateButton, validatorIndex + 2, row);
        }
    }
}
