package nixchats;

import nixchats.util.DateFormatter;

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
        assert by != null : "Deadline 'by' date cannot be null";
        assert !by.trim().isEmpty() : "Deadline 'by' date cannot be empty";
        this.by = by;
    }

    public String getBy() {
        assert by != null : "By date should never be null after construction";
        return by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + DateFormatter.formatDate(by) + ")";
    }
}
