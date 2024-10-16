package g56583.esi.atl.model.command;

import g56583.esi.atl.model.GameFacade;

import java.util.Stack;

/**
 * The CommandManager class manages the execution of commands and provides
 * the ability to undo and redo them. It maintains two stacks, one for undoable
 * commands and one for redo commands.
 */
public class CommandManager {
    private final Stack<Command> undoStack;
    private final Stack<Command> redoStack;
    private boolean canRedo;  // Flag to check if redo is allowed

    /**
     * Constructs a CommandManager with empty undo and redo stacks.
     */
    public CommandManager() {
        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();
        this.canRedo = false;
    }

    /**
     * Adds and executes a command. Clears the redo stack after executing a new command.
     *
     * @param command The command to be executed and added to the undo stack.
     */
    public void add(Command command) {
        command.execute();
        undoStack.push(command);
        redoStack.clear();
        canRedo = false;
    }

    /**
     * Undoes the last executed command and moves it to the redo stack.
     *
     * @param game The GameFacade instance used to notify observers.
     */
    public void undo(GameFacade game) {
        if (!undoStack.isEmpty()) {
            Command command = undoStack.pop();
            command.undo();
            redoStack.push(command);
            canRedo = true;
            game.notifyObservers("undoPerformed");
        } else {
            game.notifyObservers("undoError");
            game.showAlert("Undo Error", "No actions to undo!");
        }
    }

    /**
     * Redoes the last undone command and moves it back to the undo stack.
     *
     * @param game The GameFacade instance used to notify observers.
     */
    public void redo(GameFacade game) {
        if (canRedo && !redoStack.isEmpty()) {
            Command command = redoStack.pop();
            command.redo();
            undoStack.push(command);
            game.notifyObservers("redoPerformed");
        } else {
            game.notifyObservers("redoError");
            game.showAlert("Redo Error", "No actions to redo!");
        }
    }
}
