package nixchats;

public class DeadlineTask extends Task {

    String byTime;

    public DeadlineTask(String description, String byTime) {
        super(description);
        this.byTime = byTime;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + this.byTime + ")";
    }
}
