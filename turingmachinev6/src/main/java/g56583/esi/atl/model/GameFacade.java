package g56583.esi.atl.model;

import g56583.esi.atl.model.OO.Observable;
import g56583.esi.atl.model.command.*;
import g56583.esi.atl.model.validator.GenericValidator;
import g56583.esi.atl.model.validator.Validator;

import java.util.*;

public class GameFacade extends Observable {
    private final List<Problem> problems;
    private Problem currentProblem;
    private Code currentUserInput;
    private boolean gameActive;
    private int validatorCount;
    private int currentRound;
    private int score;
    private final CommandManager commandManager = new CommandManager();
    private final Map<Integer, boolean[]> validationResultsByRound = new HashMap<>();

    public GameFacade(String problemsFilePath) {
        CSVReader csvReader = new CSVReader();
        this.problems = csvReader.readProblems(problemsFilePath);
        this.gameActive = false;
    }

    public void startNewGame(int problemIndex) {
        if (problemIndex < 0 || problemIndex >= problems.size()) {
            throw new IllegalArgumentException("Invalid problem index.");
        }
        setupGame(problemIndex);
        notifyObservers("newGameStarted");  // Notifie qu'un nouveau jeu a été démarré
    }
    public void startGameWithRandomProblem() {
        int problemIndex = new Random().nextInt(problems.size());
        setupGame(problemIndex);
    }

    private void setupGame(int problemIndex) {
        this.currentProblem = problems.get(problemIndex);
        this.currentUserInput = null;
        this.gameActive = true;
        this.validatorCount = 0;
        this.currentRound = 1;
        this.score = 0;
    }

    public void enterCode(String code) {
        if (currentUserInput != null) {
            throw new IllegalStateException("Code already entered for this round.");
        }
        Command command = new EnterCode(this, code);
        commandManager.add(command);
        notifyObservers("codeEntered");
    }
    public boolean chooseValidator(int validatorNo) {
        if (validatorCount >= 3) {
            throw new IllegalStateException("Maximum of 3 validators already chosen for this code.");
        }

        GenericValidator validator = (GenericValidator) currentProblem.getValidators().stream()
                .filter(v -> v instanceof GenericValidator && ((GenericValidator) v).getValidatorNo() == validatorNo)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Validator number " + validatorNo + " is not available."));

        ChooseValidator command = new ChooseValidator(this, validatorNo);
        commandManager.add(command);

        boolean[] results = validationResultsByRound.computeIfAbsent(currentRound, k -> new boolean[getCurrentValidators().size()]);
        results[getValidatorIndexByNumber(validatorNo)] = command.getValidationResult();

        notifyObservers("chooseValidator");
        return command.getValidationResult();
    }

    public int getValidatorIndexByNumber(int validatorNo) {
        for (int i = 0; i < currentProblem.getValidators().size(); i++) {
            GenericValidator validator = (GenericValidator) currentProblem.getValidators().get(i);
            if (validator.getValidatorNo() == validatorNo) {
                return i;
            }
        }
        throw new IllegalArgumentException("Validator number " + validatorNo + " is not available.");
    }
    public boolean guessCode(String guess) {
        if (guess == null || guess.isEmpty()) {
            throw new IllegalStateException("No code entered.");
        }

        if (!gameActive) {
            throw new IllegalStateException("Game is not active.");
        }

        Code guessedCode = new Code(guess);
        boolean result = currentProblem.isCorrectGuess(guessedCode);
        gameActive = !result;
        return result;
    }

    public void nextRound() {
        Command command = new NextRound(this);
        commandManager.add(command);
    }

    public void undo(GameFacade game) {
        commandManager.undo(this);
    }

    public void redo(GameFacade game) {
        commandManager.redo(this);
        // Notify observers to ensure the UI is correctly updated after redo
        notifyObservers("redoPerformed");
    }
    // Getters and Setters for the necessary fields
    public boolean isGameActive() {
        return gameActive;
    }

    public Code getCurrentUserInput() {
        return currentUserInput;
    }

    public int getValidatorCount() {
        return validatorCount;
    }

    public void setCurrentUserInput(Code code) {
        this.currentUserInput = code;
    }

    public void resetValidatorCount() {
        this.validatorCount = 0;
    }

    public void incrementValidatorCount() {
        this.validatorCount++;
    }

    public Problem getCurrentProblem() {
        return currentProblem;
    }

    public void incrementCurrentRound() {
        this.currentRound++;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public void setGameActive(boolean active) {
        this.gameActive = active;
    }

    public int getCurrentRound() {
        return currentRound;
    }
    public void setValidatorCount(int count) {
        this.validatorCount = count;
    }
    public void setCurrentRound(int round) {
        this.currentRound = round;
    }
    public List<Problem> getProblems() {
        return problems;
    }
    public void abandonGame() {
        this.currentProblem = null;
        this.currentUserInput = null;
        this.gameActive = false;
        notifyObservers("gameAbandoned");  // Notifie que le jeu a été abandonné
    }
    public List<Validator> getCurrentValidators() {
        return currentProblem != null ? currentProblem.getValidators() : Collections.emptyList();
    }
    public void incrementScore(int value) {
        this.score += value;
    }
    public boolean[] getValidationResultsForRoundSafe(int round) {
        return validationResultsByRound.getOrDefault(round, new boolean[getCurrentValidators().size()]);
    }
    public void showAlert(String title, String content) {
        // Method to display an alert in the UI
        notifyObservers("showAlert:" + title + ":" + content);
    }



}
