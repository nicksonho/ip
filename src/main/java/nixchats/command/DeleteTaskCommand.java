package nixchats.command;

import nixchats.Task;
import nixchats.data.TaskList;

/**
 * Command to delete a task that can be undone.
 */
public class DeleteTaskCommand implements UndoableCommand {
    private final TaskList taskList;
    private final int index;
    private Task deletedTask;

    public DeleteTaskCommand(TaskList taskList, int index) {
        this.taskList = taskList;
        this.index = index;
    }

    @Override
    public void execute() {
        // Save the task before deleting so we can restore it
        deletedTask = taskList.getTask(index);
        taskList.deleteTask(index, true); // Show deletion message
    }

    @Override
    public void undo() {
        if (deletedTask != null) {
            // Insert the task back at its original position
            taskList.insertTask(index, deletedTask);
        }
    }

    @Override
    public String getDescription() {
        return "delete task: " + (deletedTask != null ? deletedTask.getDescription() : "task at index " + index);
    }
}
