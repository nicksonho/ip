package nixchats;

/**
 * Represents a deadline task.
 */
public class DeadlineTask extends Task {
    private final String by;

    /**
     * Constructs a DeadlineTask object.
     * @param description Description of the task.
     * @param isDone Whether the task is done or not.
     * @param by Date by which the task must be completed.
     */
    public DeadlineTask(String description, boolean isDone, String by) {
        super(description, isDone);
        this.by = by;
    }

    public String getBy() {
        return by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}
