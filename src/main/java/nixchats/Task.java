package nixchats;

public abstract class Task {
    protected String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
    }

    void markAsDone() {
        isDone = true;
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + this.toString());
    }

    void unmarkAsNotDone() {
        isDone = false;
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  " + this.toString());
    }

    // --- Added: minimal accessors for storage ---
    public String getDescription() {
        return description;
    }

    public boolean isDone() {
        return isDone;
    }

    // Package-private silent setter for decoding (no prints)
    void setDoneSilent(boolean done) {
        this.isDone = done;
    }

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
