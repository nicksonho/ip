package nixchats.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nixchats.Task;
import nixchats.exception.InputException;
import nixchats.parser.Parser;

/**
 * Represents a list of tasks.
 */
public class TaskList implements Iterable<Task> {
    private final List<Task> taskList;

    public TaskList() {
        this.taskList = new ArrayList<>();
    }

    /**
     * Adds a task to the list by parsing the command string.
     * @param task The command string to parse and add as a task.
     * @throws InputException if the task command is invalid.
     */
    public void addTask(String task) throws InputException {
        taskList.add(Parser.parseTask(task));
    }

    /**
     * Adds a task object directly to the list.
     * @param task The task object to add.
     */
    public void addTask(Task task) {
        assert task != null : "Task cannot be null";
        taskList.add(task);
    }

    /**
     * Deletes the task at the given index.
     * @param index Index of the task to be deleted.
     */
    public void deleteTask(int index) {
        assert index >= 0 && index < taskList.size() : "Index must be within bounds: " + index;
        System.out.println("Got it, deleted task " + taskList.get(index));
        taskList.remove(index);
    }

    public boolean isEmpty() {
        return taskList.isEmpty();
    }

    public void printTasks() {
        taskList.forEach(System.out::println);
    }

    /**
     * Returns the number of tasks in the list.
     * @return The size of the task list.
     */
    public int size() {
        return taskList.size();
    }

    /**
     * Gets a task at the specified index.
     * @param index The index of the task to retrieve.
     * @return The task at the specified index.
     */
    public Task getTask(int index) {
        assert index >= 0 && index < taskList.size() : "Index must be within bounds: " + index;
        return taskList.get(index);
    }

    /**
     * Returns an iterator over the tasks in the list.
     * @return An iterator for the task list.
     */
    public Iterator<Task> iterator() {
        assert taskList != null : "TaskList should never be null";
        return taskList.iterator();
    }

    /**
     * Finds tasks that contain the given keyword.
     * @param keyword Keyword to be searched for.
     * @return List of tasks that match the keyword (case-insensitive).
     */
    public java.util.List<Task> findTasks(String keyword) {
        assert keyword != null : "Keyword cannot be null";
        assert !keyword.trim().isEmpty() : "Keyword cannot be empty after trimming";
        
        return taskList.stream()
                .filter(task -> task.getDescription().toLowerCase().contains(keyword.toLowerCase()))
                .collect(java.util.stream.Collectors.toList());
    }
}
