package g56583.esi.atl.model.command;

/**
 * The Command interface defines the methods that all concrete command classes
 * must implement in order to perform actions, as well as undo and redo them.
 */
public interface Command {
    /**
     * Executes the command.
     */
    void execute();

    /**
     * Undoes the command, reverting any changes made by execute.
     */
    void undo();

    /**
     * Redoes the command, reapplying the changes made by execute.
     */
    void redo();
}
