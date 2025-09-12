package nixchats;

/**
 * Represents a task.
 */
public abstract class Task {
    private final String description;
    private boolean isDone;

    /**
     * Constructs a Task object.
     * @param description Description of the task.
     * @param isDone Whether the task is done or not.
     */
    public Task(String description, boolean isDone) {
        assert description != null : "Task description cannot be null";
        assert !description.trim().isEmpty() : "Task description cannot be empty";
        this.description = description;
        this.isDone = isDone;
    }

    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
    }

    /**
     * Marks the task as done.
     */
    public void markAsDone() {
        isDone = true;
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + this.toString());
    }

    /**
     * Marks the task as not done.
     */
    public void unmarkAsNotDone() {
        isDone = false;
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  " + this.toString());
    }

    public String getDescription() {
        assert description != null : "Description should never be null after construction";
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
