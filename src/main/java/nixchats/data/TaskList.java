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

    public void addTask(String task) throws InputException {
        taskList.add(Parser.parseTask(task));
    }

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

    public int size() {
        return taskList.size();
    }

    public Task getTask(int index) {
        assert index >= 0 && index < taskList.size() : "Index must be within bounds: " + index;
        return taskList.get(index);
    }

    public Iterator<Task> iterator() {
        assert taskList != null : "TaskList should never be null";
        return taskList.iterator();
    }

    /**
     * Finds tasks that contain the given keyword.
     * @param keyword Keyword to be searched for.
     */
    public void findTasks(String keyword) {
        assert keyword != null : "Keyword cannot be null";
        assert !keyword.trim().isEmpty() : "Keyword cannot be empty after trimming";
        taskList.stream()
                .filter(task -> task.getDescription().toLowerCase().contains(keyword.toLowerCase()))
                .forEach(System.out::println);
    }
}
