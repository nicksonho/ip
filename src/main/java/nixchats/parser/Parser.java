package nixchats.parser;

import nixchats.DeadlineTask;
import nixchats.EventTask;
import nixchats.Task;
import nixchats.ToDoTask;
import nixchats.exception.InputException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Parser utility class that handles parsing of user input commands
 * and converts them into appropriate Task objects or indices.
 */
public class Parser {

    /**
     * Parses a command like "mark 2" or "unmark 3" and returns a zero-based index.
     * Throws IllegalArgumentException with friendly messages for user errors.
     *
     * @param line the input command string containing task number
     * @param size the total number of tasks in the list
     * @return zero-based index of the task
     * @throws IllegalArgumentException if the input is invalid, task number is missing,
     *                                  non-numeric, zero/negative, or out of range
     */
    public static int parseTaskIndex(String line, int size) {
        String[] parts = line.split("\\s+");
        if (parts.length < 2 || parts[1].isBlank()) {
            throw new IllegalArgumentException("Please provide the task number, e.g., \"mark 2\".");
        }
        int oneBased;
        try {
            oneBased = Integer.parseInt(parts[1]);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Task number must be a positive integer.");
        }
        if (oneBased <= 0) {
            throw new IllegalArgumentException("Task number must be greater than zero.");
        }
        if (oneBased > size) {
            throw new IllegalArgumentException("Task number out of range. You have " + size + " task(s).");
        }
        return oneBased - 1; // zero-based
    }

    /**
     * Parses the input line and adds a Task to the given list.
     * Supported:
     *   - todo <description>
     *   - deadline <description> /by <when>
     *   - event <description> /from <start> /to <end>
     * Returns the created Task. Throws InputException with a user-friendly message
     * if the input is invalid.
     */
    public static Task parseTask(String input) throws InputException {
        if (input == null || input.trim().isEmpty()) {
            throw new InputException(InputException.Reason.EMPTY_INPUT, "Please enter a command.");
        }

        String trimmed = input.trim();
        String lower = trimmed.toLowerCase();

        // todo
        if (lower.startsWith("todo")) {
            String desc = trimmed.length() <= 4 ? "" : trimmed.substring(4).trim();
            if (desc.isEmpty()) {
                throw new InputException(InputException.Reason.MISSING_ARGUMENT,
                        "The description of a todo cannot be empty.");
            }
            return new ToDoTask(desc, false);
        }

        // deadline
        if (lower.startsWith("deadline")) {
            return getDeadlineTask(trimmed);
        }

        // event
        if (lower.startsWith("event")) {
            return getEventTask(trimmed);
        }

        // Unknown command
        throw new InputException(InputException.Reason.UNKNOWN_COMMAND,
                "I'm sorry, but I don't know what that means.");
    }

    /**
     * Creates a DeadlineTask from the parsed deadline command.
     *
     * @param trimmed the trimmed deadline command string
     * @return a new DeadlineTask object with parsed description and due date
     * @throws InputException if the deadline format is invalid, missing arguments,
     *                        or contains invalid date format
     */
    private static DeadlineTask getDeadlineTask(String trimmed) throws InputException {
        String[] parts = getStrings(trimmed);
        String desc = parts[0].trim();
        String by = parts[1].trim();

        try {
            LocalDate byDate = LocalDate.parse(by);

            DateTimeFormatter outFmt = DateTimeFormatter.ofPattern("MMM d yyyy");
            String byDisplay =byDate.format(outFmt);

            return new DeadlineTask(desc, false, byDisplay);
        } catch (java.time.format.DateTimeParseException ex) {
            throw new InputException(InputException.Reason.INVALID_ARGUMENT,
                    "Invalid date format. Please use yyyy-MM-dd (e.g., 2025-01-31).");
        }
    }

    /**
     * Parses and validates the deadline command format to extract description and due date.
     *
     * @param trimmed the trimmed deadline command string
     * @return array containing description and due date string
     * @throws InputException if the deadline command is missing description,
     *                        missing /by delimiter, or has invalid format
     */
    private static String[] getStrings(String trimmed) throws InputException {
        String rest = trimmed.length() <= 8 ? "" : trimmed.substring(8).trim(); // after "deadline"
        if (rest.isEmpty()) {
            throw new InputException(InputException.Reason.MISSING_ARGUMENT,
                    "The description of a deadline cannot be empty. Usage: deadline <desc> /by <when>");
        }
        String[] parts = rest.split("\\s+/by\\s+", 2);
        if (parts.length != 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
            throw new InputException(InputException.Reason.INVALID_ARGUMENT,
                    "Missing '/by'. Usage: deadline <desc> /by <when>");
        }
        return parts;
    }

    /**
     * Creates an EventTask from the parsed event command.
     * Overloaded method that handles the initial parsing of the event command.
     *
     * @param trimmed the trimmed event command string
     * @return a new EventTask object with parsed description, start and end dates
     * @throws InputException if the event format is invalid, missing arguments,
     *                        or contains invalid date format
     */
    private static EventTask getEventTask(String trimmed) throws InputException {
        String rest = trimmed.length() <= 5 ? "" : trimmed.substring(5).trim(); // after "event"
        if (rest.isEmpty()) {
            throw new InputException(InputException.Reason.MISSING_ARGUMENT,
                    "The description of an event cannot be empty. Usage: event <desc> /from <start> /to <end>");
        }
        String[] fromParts = rest.split("\\s+/from\\s+", 2);
        if (fromParts.length != 2 || fromParts[0].trim().isEmpty()) {
            throw new InputException(InputException.Reason.INVALID_ARGUMENT,
                    "Missing '/from'. Usage: event <desc> /from <start> /to <end>");
        }
        return getEventTask(fromParts);
    }

    /**
     * Creates an EventTask from the parsed event command parts.
     * Overloaded method that handles the final creation of the EventTask after parsing.
     *
     * @param fromParts array containing description and the part after /from
     * @return a new EventTask object with parsed description, start and end dates
     * @throws InputException if the event dates are invalid, missing /to delimiter,
     *                        invalid date format, or end date is before start date
     */
    private static EventTask getEventTask(String[] fromParts) throws InputException {
        String desc = fromParts[0].trim();

        String[] toParts = fromParts[1].split("\\s+/to\\s+", 2);
        if (toParts.length != 2 || toParts[0].trim().isEmpty() || toParts[1].trim().isEmpty()) {
            throw new InputException(InputException.Reason.INVALID_ARGUMENT,
                    "Missing '/to'. Usage: event <desc> /from <start> /to <end>");
        }
        String from = toParts[0].trim();
        String to = toParts[1].trim();

        try {
            LocalDate fromDate = LocalDate.parse(from); // expects yyyy-MM-dd
            LocalDate toDate = LocalDate.parse(to);     // expects yyyy-MM-dd

            if (toDate.isBefore(fromDate)) {
                throw new InputException(InputException.Reason.INVALID_ARGUMENT,
                        "End date must be on or after the start date.");
            }

            DateTimeFormatter outFmt = DateTimeFormatter.ofPattern("MMM d yyyy");
            String fromDisplay = fromDate.format(outFmt);
            String toDisplay = toDate.format(outFmt);

            return new EventTask(desc, false, fromDisplay, toDisplay);
        } catch (java.time.format.DateTimeParseException ex) {
            throw new InputException(InputException.Reason.INVALID_ARGUMENT,
                    "Invalid date format. Please use yyyy-MM-dd (e.g., 2025-01-31).");
        }
    }



}
