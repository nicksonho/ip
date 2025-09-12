package nixchats;

import java.nio.file.Path;
import java.nio.file.Paths;

import nixchats.data.TaskList;
import nixchats.exception.InputException;
import nixchats.exception.NixChatsException;
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
        assert taskList != null : "TaskList should be initialized";
        assert storage != null : "Storage should be initialized";

        String line = input == null ? "" : input.trim();
        StringBuilder response = new StringBuilder();

        try {
            String command = Parser.getCommand(line);
            assert command != null : "Parser should never return null command";

            processCommand(command, line, response);
            saveIfModified(command);

        } catch (NixChatsException e) {
            response.append("Error saving data: ").append(e.getMessage());
            lastCommandType = "error";
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
     * Processes the given command and updates response.
     */
    private void processCommand(String command, String line, StringBuilder response) {
        switch (command) {
        case "bye":
            handleByeCommand(response);
            break;
        case "list":
            handleListCommand(response);
            break;
        case "find":
            handleFindCommand(line, response);
            break;
        case "mark":
            handleMarkCommand(line, response);
            break;
        case "unmark":
            handleUnmarkCommand(line, response);
            break;
        case "delete":
            handleDeleteCommand(line, response);
            break;
        default:
            handleAddCommand(line, response);
            break;
        }
    }

    /**
     * Handles the bye command.
     */
    private void handleByeCommand(StringBuilder response) {
        lastCommandType = "bye";
        response.append("Bye! Hope to see you again soon!");
    }

    /**
     * Handles the list command.
     */
    private void handleListCommand(StringBuilder response) {
        lastCommandType = "list";
        response.append("Here are the tasks in your list:\n");
        if (taskList.isEmpty()) {
            response.append("No tasks found.");
        } else {
            response.append(getTaskListString());
        }
    }

    /**
     * Handles the find command.
     */
    private void handleFindCommand(String line, StringBuilder response) {
        lastCommandType = "find";
        String keyword = Parser.getKeyword(line);
        if (keyword.isEmpty()) {
            response.append("Please provide a keyword to search for, e.g., \"find book\".");
        } else {
            response.append(getFindResultsString(keyword));
        }
    }

    /**
     * Handles the mark command.
     */
    private void handleMarkCommand(String line, StringBuilder response) {
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
    }

    /**
     * Handles the unmark command.
     */
    private void handleUnmarkCommand(String line, StringBuilder response) {
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
    }

    /**
     * Handles the delete command.
     */
    private void handleDeleteCommand(String line, StringBuilder response) {
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
    }

    /**
     * Handles adding a new task.
     */
    private void handleAddCommand(String line, StringBuilder response) {
        lastCommandType = "add";
        try {
            taskList.addTask(line);
            response.append("Got it, I have added: ").append(line);
        } catch (InputException e) {
            response.append(e.getMessage());
            lastCommandType = "error";
        }
    }

    /**
     * Saves the task list if the command modified data.
     */
    private void saveIfModified(String command) throws NixChatsException {
        boolean isReadOnlyCommand = command.equals("list") || command.equals("find") || command.equals("bye");
        if (!isReadOnlyCommand) {
            assert storage != null : "Storage should be available for saving";
            assert taskList != null : "TaskList should be available for saving";
            storage.save(taskList);
        }
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
        
        return java.util.stream.IntStream.range(0, taskList.size())
                .mapToObj(i -> taskList.getTask(i).toString())
                .collect(java.util.stream.Collectors.joining("\n"));
    }

    /**
     * Helper method to get find results as string.
     */
    private String getFindResultsString(String keyword) {
        java.util.List<Task> matchingTasks = taskList.findTasks(keyword);

        if (matchingTasks.isEmpty()) {
            return "No matching tasks found.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Here are the matching tasks in your list:\n");
        
        for (int i = 0; i < matchingTasks.size(); i++) {
            if (i > 0) {
                sb.append("\n");
            }
            sb.append((i + 1)).append(".").append(matchingTasks.get(i).toString());
        }

        return sb.toString();
    }

    /**
     * Main method for command-line execution.
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        try {
            NixChatsCli.chat();
        } catch (Exception e) {
            System.err.println("Error starting NixChats: " + e.getMessage());
        }
    }
}
