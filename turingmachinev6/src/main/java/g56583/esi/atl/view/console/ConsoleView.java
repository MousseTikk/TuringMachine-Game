package g56583.esi.atl.view.console;

import g56583.esi.atl.model.GameFacade;
import g56583.esi.atl.model.OO.Observable;
import g56583.esi.atl.model.Problem;
import g56583.esi.atl.model.validator.GenericValidator;

import java.util.Scanner;

/**
 * The ConsoleView class provides a text-based user interface for interacting with the Turing Machine game.
 * It extends the Observable class to notify observers of changes in the game state.
 */
public class ConsoleView extends Observable {
    private GameFacade game;
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Sets the game instance for the console view.
     *
     * @param game the GameFacade instance representing the game model
     */
    public void setGame(GameFacade game) {
        this.game = game;
    }

    /**
     * Displays a welcome message to the user.
     */
    public void displayWelcomeMessage() {
        System.out.println("\033[0;34m" + "Welcome to the Turing Machine Game!" + "\033[0m");
    }

    /**
     * Displays the list of available problems that the user can choose from.
     */
    public void displayProblems() {
        System.out.println("\033[0;32m" + "Available Problems:" + "\033[0m");
        int index = 0;
        for (Problem problem : game.getProblems()) {
            System.out.println("\033[0;33m" + index + ": " + problem + "\033[0m");
            index++;
        }
    }

    /**
     * Prompts the user to choose a problem by entering a number, or -1 for a random problem.
     *
     * @return the user's choice as an integer
     */
    public int getUserProblemChoice() {
        System.out.println("Choose a problem (number) or -1 for a random problem:");
        return parseIntInput();
    }

    /**
     * Prompts the user to enter a command for interacting with the game.
     *
     * @return the command entered by the user as a string
     */
    public String getUserCommand() {
        System.out.println("Enter command ('enter <code>', 'choose <n>', 'check', 'abandon', 'leave', 'validators', 'next', 'undo', 'redo'):");
        return scanner.nextLine().trim();
    }

    /**
     * Displays the available validators for the current problem.
     */
    public void displayValidators() {
        if (game.getCurrentProblem() == null) {
            displayErrorMessage("No problem is currently selected.");
            return;
        }

        System.out.println("\033[0;35m" + "Available Validators:" + "\033[0m");
        game.getCurrentValidators().stream()
                .filter(v -> v instanceof GenericValidator)
                .map(v -> (GenericValidator) v)
                .forEach(v -> System.out.println("\033[0;36m" + "Validator " + v.getValidatorNo() + ": " + v.getDescription() + "\033[0m"));
    }

    /**
     * Displays the end game message based on whether the player has won or lost.
     *
     * @param hasWon true if the player has won the game, false otherwise
     */
    public void displayEndGame(boolean hasWon) {
        if (hasWon) {
            System.out.println("\033[0;36m" + "Congratulations! You have guessed the correct code!" + " in " + game.getScore() + " tries." + "\033[0m");
        } else {
            System.out.println("\033[0;31m" + "Game over! Better luck next time!" + "\033[0m");
        }
    }

    /**
     * Displays an error message to the user.
     *
     * @param message the error message to be displayed
     */
    public void displayErrorMessage(String message) {
        System.err.println("\033[0;31m" + message + "\033[0m");
    }

    /**
     * Displays a general message to the user.
     *
     * @param message the message to be displayed
     */
    public void displayMessage(String message) {
        System.out.println("\033[0;34m" + message + "\033[0m");
    }

    /**
     * Parses the user's input as an integer.
     * Continuously prompts the user until a valid integer is entered.
     *
     * @return the parsed integer
     */
    private int parseIntInput() {
        while (true) {
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                displayErrorMessage("Invalid input. Please enter a number.");
            }
        }
    }

    /**
     * Displays the current state of the game, including the current round and score.
     */
    public void displayCurrentState() {
        System.out.println("\033[1;33m" + "Round: " + game.getCurrentRound() + ", Score: " + game.getScore() + "\033[0m");
    }
}
