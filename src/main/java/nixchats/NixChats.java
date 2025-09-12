package nixchats;

import java.nio.file.Path;
import java.nio.file.Paths;

import nixchats.data.TaskList;
import nixchats.exception.InputException;
import nixchats.parser.Parser;
import nixchats.storage.Storage;


/**
 * Main class for the NixChats chatbot.
 */
public class NixChats {
    private TaskList taskList;
    private Storage storage;
    private String lastCommandType = "info";

    /**
     * Constructor for GUI usage.
     */
    public NixChats() {
        try {
            Path filePath = Paths.get("data", "NixChatHistory.txt");
            storage = new Storage(filePath);
            taskList = storage.load();
        } catch (Exception e) {
            taskList = new TaskList();
        }
        // Post-condition: taskList and storage should never be null
        assert taskList != null : "TaskList should never be null after construction";
        assert storage != null : "Storage should never be null after construction";
    }

    /**
     * Processes user input and returns the chatbot's response.
     * Also sets the lastCommandType for GUI styling.
     * @param input User input string.
     * @return Chatbot response string.
     */
    public String getResponse(String input) {
        // Pre-conditions: ensure object state is valid
        assert taskList != null : "TaskList should be initialized";
        assert storage != null : "Storage should be initialized";
        
        String line = input == null ? "" : input.trim();
        StringBuilder response = new StringBuilder();
        
        try {
            String command = Parser.getCommand(line);
            assert command != null : "Parser should never return null command";
            
            switch (command) {
            case "bye":
                lastCommandType = "bye";
                response.append("Bye! Hope to see you again soon!");
                break;
            case "list":
                lastCommandType = "list";
                response.append("Here are the tasks in your list:\n");
                if (taskList.isEmpty()) {
                    response.append("No tasks found.");
                } else {
                    response.append(getTaskListString());
                }
                break;
            case "find":
                lastCommandType = "find";
                String keyword = Parser.getKeyword(line);
                if (keyword.isEmpty()) {
                    response.append("Please provide a keyword to search for, e.g., \"find book\".");
                } else {
                    response.append(getFindResultsString(keyword));
                }
                break;
            case "mark":
                lastCommandType = "mark";
                try {
                    int idx = Parser.parseTaskIndex(line, taskList.size());
                    taskList.getTask(idx).markAsDone();
                    response.append("Nice! I've marked this task as done:\n  ");
                    response.append(taskList.getTask(idx).toString());
                } catch (IllegalArgumentException e) {
                    response.append(e.getMessage());
                    lastCommandType = "error";
                }
                break;
            case "unmark":
                lastCommandType = "unmark";
                try {
                    int idx = Parser.parseTaskIndex(line, taskList.size());
                    taskList.getTask(idx).unmarkAsNotDone();
                    response.append("OK, I've marked this task as not done yet:\n  ");
                    response.append(taskList.getTask(idx).toString());
                } catch (IllegalArgumentException e) {
                    response.append(e.getMessage());
                    lastCommandType = "error";
                }
                break;
            case "delete":
                lastCommandType = "delete";
                try {
                    int idx = Parser.parseTaskIndex(line, taskList.size());
                    Task deletedTask = taskList.getTask(idx);
                    taskList.deleteTask(idx);
                    response.append("Got it, deleted task ").append(deletedTask);
                } catch (IllegalArgumentException e) {
                    response.append(e.getMessage());
                    lastCommandType = "error";
                }
                break;
            default:
                lastCommandType = "add";
                try {
                    taskList.addTask(line);
                    response.append("Got it, I have added: ").append(line);
                } catch (InputException e) {
                    response.append(e.getMessage());
                    lastCommandType = "error";
                }
                break;
            }
            
            // Save after any modification
            if (!command.equals("list") && !command.equals("find") && !command.equals("bye")) {
                assert storage != null : "Storage should be available for saving";
                assert taskList != null : "TaskList should be available for saving";
                storage.save(taskList);
            }
            
        } catch (Exception e) {
            response.append("An unexpected error occurred. Please try again.");
            lastCommandType = "error";
        }
        
        String result = response.toString();
        assert result != null : "Response should never be null";
        assert lastCommandType != null : "Command type should always be set";
        return result;
    }

    /**
     * Returns the type of the last executed command for GUI styling.
     */
    public String getCommandType() {
        assert lastCommandType != null : "Command type should never be null";
        return lastCommandType;
    }

    /**
     * Helper method to get task list as string.
     */
    private String getTaskListString() {
        assert taskList != null : "TaskList should not be null";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < taskList.size(); i++) {
            if (i > 0) sb.append("\n");
            sb.append(taskList.getTask(i).toString());
        }
        return sb.toString();
    }

    /**
     * Helper method to get find results as string.
     */
    private String getFindResultsString(String keyword) {
        StringBuilder sb = new StringBuilder();
        java.util.List<Task> matchingTasks = new java.util.ArrayList<>();
        
        for (int i = 0; i < taskList.size(); i++) {
            Task task = taskList.getTask(i);
            if (task.getDescription().toLowerCase().contains(keyword.toLowerCase())) {
                matchingTasks.add(task);
            }
        }
        
        if (matchingTasks.isEmpty()) {
            sb.append("No matching tasks found.");
        } else {
            sb.append("Here are the matching tasks in your list:\n");
            for (int i = 0; i < matchingTasks.size(); i++) {
                if (i > 0) sb.append("\n");
                sb.append((i + 1)).append(".").append(matchingTasks.get(i).toString());
            }
        }
        
        return sb.toString();
    }
}
