package nixchats;

public class EventTask extends Task {

    String fromTime;
    String toTime;

    public EventTask(String description, String fromTime, String toTime) {
        super(description);
        this.fromTime = fromTime;
        this.toTime = toTime;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + this.fromTime + " to: "  + this.toTime + ")";
    }
}
