package nixchats;

public abstract class Task {
    private final String description;
    private boolean isDone;

    public Task(String description, boolean isDone) {
        this.description = description;
        this.isDone = isDone;
    }

    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
    }

    public void markAsDone() {
        isDone = true;
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + this.toString());
    }

    public void unmarkAsNotDone() {
        isDone = false;
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  " + this.toString());
    }

    public String getDescription() {
        return description;
    }

    public boolean isDone() {
        return isDone;
    }

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
