package nixchats.command;

import nixchats.Task;
import nixchats.data.TaskList;

/**
 * Command to mark a task as done that can be undone.
 */
public class MarkTaskCommand implements UndoableCommand {
    private final TaskList taskList;
    private final int index;
    private boolean previousState;

    public MarkTaskCommand(TaskList taskList, int index) {
        this.taskList = taskList;
        this.index = index;
    }

    @Override
    public void execute() {
        Task task = taskList.getTask(index);
        previousState = task.isDone(); // Save the previous state
        task.markAsDone();
    }

    @Override
    public void undo() {
        Task task = taskList.getTask(index);
        if (previousState) {
            task.markAsDone();
        } else {
            task.unmarkAsNotDone();
        }
    }

    @Override
    public String getDescription() {
        return "mark task: " + taskList.getTask(index).getDescription();
    }
}
