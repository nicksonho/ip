package nixchats.data;

import nixchats.Task;
import nixchats.exception.InputException;
import nixchats.parser.Parser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Represents a list of tasks.
 */
public class TaskList implements Iterable<Task>{

    private final List<Task> taskList;

    public TaskList() {
        this.taskList = new ArrayList<>();
    }

    public void addTask(String task) throws InputException {
        taskList.add(Parser.parseTask(task));
    }

    public void addTask(Task task) {
        taskList.add(task);
    }

    public void deleteTask(int index) {
        System.out.println("Got it, deleted task " + taskList.get(index));
        taskList.remove(index);
    }

    public boolean isEmpty() {
        return taskList.isEmpty();
    }

    public void printTasks() {
        taskList.forEach(System.out::println);
    }

    public void findTasks(String keyword) {
        List<Task> matchingTasks = new ArrayList<>();
        for (Task task : taskList) {
            if (task.getDescription().toLowerCase().contains(keyword.toLowerCase())) {
                matchingTasks.add(task);
            }
        }
        
        if (matchingTasks.isEmpty()) {
            System.out.println("No matching tasks found.");
        } else {
            System.out.println("Here are the matching tasks in your list:");
            for (int i = 0; i < matchingTasks.size(); i++) {
                System.out.println((i + 1) + "." + matchingTasks.get(i).toString());
            }
        }
    }

    public int size() {
        return taskList.size();
    }

    public Task getTask(int index) {
        return taskList.get(index);
    }

    public Iterator<Task> iterator() {
        return taskList.iterator();
    }
}
