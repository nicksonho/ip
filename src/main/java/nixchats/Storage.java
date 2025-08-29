package nixchats;

import nixchats.exception.NixChatsException;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Storage class for saving and loading tasks.
 */
public class Storage {
    private final Path filePath;

    public Storage(Path filePath) throws IOException {
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
    public void save(List<Task> list) throws NixChatsException {
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
    public List<Task> load() throws NixChatsException {
        List<Task> result = new ArrayList<>();
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
                result.add(t);
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
        String done = t.isDone() ? "1" : "0";
        if (t instanceof ToDoTask) {
            return String.join(" | ", "T", done, t.getDescription());
        } else if (t instanceof DeadlineTask) {
            DeadlineTask d = (DeadlineTask) t;
            return String.join(" | ", "D", done, d.getDescription(), d.getBy());
        } else if (t instanceof EventTask) {
            EventTask e = (EventTask) t;
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
        String[] parts = line.split("\\s*\\|\\s*");
        String type = parts[0];
        boolean done = "1".equals(parts[1]);

        switch (type) {
            case "T": {
                String desc = parts[2];
                ToDoTask t = new ToDoTask(desc);
                t.setDoneSilent(done);
                return t;
            }
            case "D": {
                String desc = parts[2];
                String by = parts[3];
                DeadlineTask d = new DeadlineTask(desc, by);
                d.setDoneSilent(done);
                return d;
            }
            case "E": {
                String desc = parts[2];
                String from = parts[3];
                String to = parts[4];
                EventTask e = new EventTask(desc, from, to);
                e.setDoneSilent(done);
                return e;
            }
            default:
                throw new IllegalArgumentException("Unknown task type: " + type);
        }
    }
}
