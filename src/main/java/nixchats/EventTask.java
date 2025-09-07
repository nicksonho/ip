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
        this.from = from;
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
