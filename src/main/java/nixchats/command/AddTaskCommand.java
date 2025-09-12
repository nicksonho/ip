package nixchats.command;

import nixchats.Task;
import nixchats.data.TaskList;

/**
 * Command to add a task that can be undone.
 */
public class AddTaskCommand implements UndoableCommand {
    private final TaskList taskList;
    private final Task task;

    public AddTaskCommand(TaskList taskList, Task task) {
        this.taskList = taskList;
        this.task = task;
    }

    @Override
    public void execute() {
        taskList.addTask(task);
    }

    @Override
    public void undo() {
        // Remove the last added task (which should be this task)
        int lastIndex = taskList.size() - 1;
        if (lastIndex >= 0 && taskList.getTask(lastIndex).equals(task)) {
            taskList.deleteTask(lastIndex, false); // false = don't print message
        }
    }

    @Override
    public String getDescription() {
        return "add task: " + task.getDescription();
    }
}
