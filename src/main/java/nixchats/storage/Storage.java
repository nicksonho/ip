package nixchats.storage;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import nixchats.DeadlineTask;
import nixchats.EventTask;
import nixchats.Task;
import nixchats.ToDoTask;
import nixchats.data.TaskList;
import nixchats.exception.NixChatsException;


/**
 * Storage class for saving and loading tasks.
 */
public class Storage {
    private final Path filePath;

    /**
     * Constructs a Storage object.
     * @param filePath Path to the file to be used for storage.
     * @throws IOException if the file cannot be created.
     */
    public Storage(Path filePath) throws IOException {
        assert filePath != null : "File path cannot be null";
        this.filePath = filePath;
        ensureFileExists();
    }

    private void ensureFileExists() throws IOException {
        Path dir = filePath.getParent();
        if (dir != null) {
            Files.createDirectories(dir); // safe if exists
        }
        if (Files.notExists(filePath)) {
            Files.createFile(filePath);
        }
    }

    /**
     * Saves the tasks to the file in a stable, decodable format.
     * One task per line, fields separated by "|".
     * @throws NixChatsException if the file cannot be written.
     */
    public void save(TaskList list) throws NixChatsException {
        assert list != null : "TaskList cannot be null";
        assert filePath != null : "File path should be initialized";

        try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)) {
            for (Task t : list) {
                writer.write(encode(t));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new NixChatsException("Failed to save tasks.", e);
        }
    }

    /**
     * Loads the tasks from the file.
     * @return List of tasks read from the file.
     * @throws NixChatsException if the file cannot be read.
     */
    public TaskList load() throws NixChatsException {
        TaskList result = new TaskList();
        try {
            if (Files.notExists(filePath)) {
                ensureFileExists();
                return result;
            }
            // reads the entire file into a list of Strings, one entry per line, decoded as UTF-8
            List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
            for (String raw : lines) {
                String line = raw == null ? "" : raw.trim();
                if (line.isEmpty()) {
                    continue;
                }
                Task t = decode(line);
                result.addTask(t);
            }
            return result;
        } catch (IOException e) {
            throw new NixChatsException("Failed to load tasks.", e);
        }
    }


    /**
     * Encodes a task to a string.
     * @param t Task to encode
     * @return String representation of the task, in the format "T|1|description"
     */
    private String encode(Task t) {
        assert t != null : "Task cannot be null";
        assert t.getDescription() != null : "Task description cannot be null";

        String done = t.isDone() ? "1" : "0";
        if (t instanceof ToDoTask) {
            return String.join(" | ", "T", done, t.getDescription());
        } else if (t instanceof DeadlineTask d) {
            assert d.getBy() != null : "Deadline task must have a 'by' date";
            return String.join(" | ", "D", done, d.getDescription(), d.getBy());
        } else if (t instanceof EventTask e) {
            assert e.getFrom() != null : "Event task must have a 'from' date";
            assert e.getTo() != null : "Event task must have a 'to' date";
            return String.join(" | ", "E", done, e.getDescription(), e.getFrom(), e.getTo());
        } else {
            // Fallback: store as a plain todo
            return String.join(" | ", "T", done, t.getDescription());
        }
    }

    /**
     * Decodes a string to a task.
     * @param line String representation of the task, in the format "T|1|description"
     * @return Task represented by the string
     */
    private Task decode(String line) {
        assert line != null : "Input line cannot be null";
        assert !line.trim().isEmpty() : "Input line cannot be empty";

        String[] parts = line.split("\\s*\\|\\s*");
        assert parts.length >= 3 : "Line must have at least 3 parts: type, done status, description";

        String type = parts[0];
        assert type != null && !type.isEmpty() : "Task type cannot be null or empty";

        boolean done = "1".equals(parts[1]);
        assert parts[2] != null : "Task description cannot be null";

        switch (type) {
        case "T": {
            String desc = parts[2];
            return new ToDoTask(desc, done);
        }
        case "D": {
            assert parts.length >= 4 : "Deadline task must have 'by' date";
            String desc = parts[2];
            String by = parts[3];
            assert by != null : "Deadline 'by' date cannot be null";
            return new DeadlineTask(desc, done, by);
        }
        case "E": {
            assert parts.length >= 5 : "Event task must have 'from' and 'to' dates";
            String desc = parts[2];
            String from = parts[3];
            String to = parts[4];
            assert from != null : "Event 'from' date cannot be null";
            assert to != null : "Event 'to' date cannot be null";
            return new EventTask(desc, done, from, to);
        }
        default:
            throw new IllegalArgumentException("Unknown task type: " + type);
        }
    }
}
