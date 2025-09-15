package nixchats.command;

import nixchats.Task;
import nixchats.data.TaskList;

/**
 * Command to unmark a task that can be undone.
 */
public class UnmarkTaskCommand implements UndoableCommand {
    private final TaskList taskList;
    private final int index;
    private boolean isPreviousState;

    public UnmarkTaskCommand(TaskList taskList, int index) {
        this.taskList = taskList;
        this.index = index;
    }

    @Override
    public void execute() {
        Task task = taskList.getTask(index);
        isPreviousState = task.isDone(); // Save the previous state
        task.unmarkAsNotDone();
    }

    @Override
    public void undo() {
        Task task = taskList.getTask(index);
        if (isPreviousState) {
            task.markAsDone();
        } else {
            task.unmarkAsNotDone();
        }
    }

    @Override
    public String getDescription() {
        return "unmark task: " + taskList.getTask(index).getDescription();
    }
}
