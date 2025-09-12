package nixchats;

/**
 * Represents an event task.
 */
public class EventTask extends Task {
    private final String from;
    private final String to;

    /**
     * Constructs an EventTask object.
     * @param description Description of the task.
     * @param isDone Whether the task is done or not.
     * @param from Start date of the task.
     * @param to End date of the task.
     */
    public EventTask(String description, boolean isDone, String from, String to) {
        super(description, isDone);
        assert from != null : "Event 'from' date cannot be null";
        assert to != null : "Event 'to' date cannot be null";
        assert !from.trim().isEmpty() : "Event 'from' date cannot be empty";
        assert !to.trim().isEmpty() : "Event 'to' date cannot be empty";
        this.from = from;
        this.to = to;
    }

    public String getFrom() {
        assert from != null : "From date should never be null after construction";
        return from;
    }

    public String getTo() {
        assert to != null : "To date should never be null after construction";
        return to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
