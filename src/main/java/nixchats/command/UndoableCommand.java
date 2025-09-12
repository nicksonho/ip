package nixchats.command;

/**
 * Represents a command that can be undone.
 */
public interface UndoableCommand {
    /**
     * Executes the command.
     */
    void execute();

    /**
     * Undoes the command, restoring the previous state.
     */
    void undo();

    /**
     * Returns a description of what this command does.
     * @return String description of the command
     */
    String getDescription();
}
