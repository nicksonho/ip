package seedu.nixchats.parser;

import nixchats.DeadlineTask;
import nixchats.EventTask;
import nixchats.Task;
import nixchats.ToDoTask;
import nixchats.exception.InputException;
import nixchats.parser.Parser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

public class ParserTest {

    @Test
    @DisplayName("parseTaskIndex should return correct zero-based index for valid input")
    void parseTaskIndex_validInput_returnsCorrectIndex() {
        // Test valid inputs with different task list sizes
        assertEquals(0, Parser.parseTaskIndex("mark 1", 5));
        assertEquals(1, Parser.parseTaskIndex("unmark 2", 5));
        assertEquals(4, Parser.parseTaskIndex("mark 5", 5));

        // Test with single item list
        assertEquals(0, Parser.parseTaskIndex("mark 1", 1));

        // Test with larger numbers
        assertEquals(99, Parser.parseTaskIndex("delete 100", 100));
    }

    @Test
    @DisplayName("parseTaskIndex should throw exception for missing task number")
    void parseTaskIndex_missingTaskNumber_throwsException() {
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, 
            () -> Parser.parseTaskIndex("mark", 5));
        assertEquals("Please provide the task number, e.g., \"mark 2\".", ex1.getMessage());

        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, 
            () -> Parser.parseTaskIndex("mark ", 5));
        assertEquals("Please provide the task number, e.g., \"mark 2\".", ex2.getMessage());

        IllegalArgumentException ex3 = assertThrows(IllegalArgumentException.class, 
            () -> Parser.parseTaskIndex("unmark   ", 5));
        assertEquals("Please provide the task number, e.g., \"mark 2\".", ex3.getMessage());
    }

    @Test
    @DisplayName("parseTaskIndex should throw exception for non-numeric task number")
    void parseTaskIndex_nonNumericInput_throwsException() {
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, 
            () -> Parser.parseTaskIndex("mark abc", 5));
        assertEquals("Task number must be a positive integer.", ex1.getMessage());

        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, 
            () -> Parser.parseTaskIndex("mark 1.5", 5));
        assertEquals("Task number must be a positive integer.", ex2.getMessage());

        IllegalArgumentException ex3 = assertThrows(IllegalArgumentException.class, 
            () -> Parser.parseTaskIndex("mark -", 5));
        assertEquals("Task number must be a positive integer.", ex3.getMessage());
    }

    @Test
    @DisplayName("parseTaskIndex should throw exception for zero or negative numbers")
    void parseTaskIndex_zeroOrNegativeNumbers_throwsException() {
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, 
            () -> Parser.parseTaskIndex("mark 0", 5));
        assertEquals("Task number must be greater than zero.", ex1.getMessage());

        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, 
            () -> Parser.parseTaskIndex("mark -1", 5));
        assertEquals("Task number must be greater than zero.", ex2.getMessage());

        IllegalArgumentException ex3 = assertThrows(IllegalArgumentException.class, 
            () -> Parser.parseTaskIndex("mark -100", 5));
        assertEquals("Task number must be greater than zero.", ex3.getMessage());
    }

    @Test
    @DisplayName("parseTaskIndex should throw exception for out of range task numbers")
    void parseTaskIndex_outOfRange_throwsException() {
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, 
            () -> Parser.parseTaskIndex("mark 6", 5));
        assertEquals("Task number out of range. You have 5 task(s).", ex1.getMessage());

        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, 
            () -> Parser.parseTaskIndex("mark 2", 1));
        assertEquals("Task number out of range. You have 1 task(s).", ex2.getMessage());

        IllegalArgumentException ex3 = assertThrows(IllegalArgumentException.class, 
            () -> Parser.parseTaskIndex("mark 1000", 10));
        assertEquals("Task number out of range. You have 10 task(s).", ex3.getMessage());
    }

    @Test
    @DisplayName("parseTaskIndex should handle edge case with empty task list")
    void parseTaskIndex_emptyTaskList_throwsException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, 
            () -> Parser.parseTaskIndex("mark 1", 0));
        assertEquals("Task number out of range. You have 0 task(s).", ex.getMessage());
    }


    @Test
    @DisplayName("parseTask should create correct ToDo task")
    void parseTask_validTodoInput_createsCorrectTask() throws InputException {
        Task task = Parser.parseTask("todo read book");

        assertTrue(task instanceof ToDoTask);
        assertEquals("read book", task.getDescription());
        assertFalse(task.isDone());
        assertTrue(task.toString().contains("[T]"));
    }

    @Test
    @DisplayName("parseTask should throw exception for empty todo description")
    void parseTask_emptyTodoDescription_throwsException() {
        InputException ex1 = assertThrows(InputException.class, 
            () -> Parser.parseTask("todo"));
        assertEquals("The description of a todo cannot be empty.", ex1.getMessage());
        assertEquals(InputException.Reason.MISSING_ARGUMENT, ex1.getReason());

        InputException ex2 = assertThrows(InputException.class, 
            () -> Parser.parseTask("todo   "));
        assertEquals("The description of a todo cannot be empty.", ex2.getMessage());
    }

    @Test
    @DisplayName("parseTask should create correct Deadline task with valid date")
    void parseTask_validDeadlineInput_createsCorrectTask() throws InputException {
        Task task = Parser.parseTask("deadline submit assignment /by 2025-01-31");

        assertTrue(task instanceof DeadlineTask);
        assertEquals("submit assignment", task.getDescription());
        assertFalse(task.isDone());
        assertTrue(task.toString().contains("[D]"));
        assertTrue(task.toString().contains("Jan 31 2025"));
    }

    @Test
    @DisplayName("parseTask should throw exception for invalid deadline format")
    void parseTask_invalidDeadlineFormat_throwsException() {
        // Missing /by
        InputException ex1 = assertThrows(InputException.class, 
            () -> Parser.parseTask("deadline submit assignment tomorrow"));
        assertEquals("Missing '/by'. Usage: deadline <desc> /by <when>", ex1.getMessage());

        // Invalid date format
        InputException ex2 = assertThrows(InputException.class, 
            () -> Parser.parseTask("deadline submit assignment /by tomorrow"));
        assertEquals("Invalid date format. Please use yyyy-MM-dd (e.g., 2025-01-31).", ex2.getMessage());

        // Empty description
        InputException ex3 = assertThrows(InputException.class, 
            () -> Parser.parseTask("deadline"));
        assertEquals("The description of a deadline cannot be empty. Usage: deadline <desc> /by <when>", ex3.getMessage());
    }

    @Test
    @DisplayName("parseTask should create correct Event task")
    void parseTask_validEventInput_createsCorrectTask() throws InputException {
        Task task = Parser.parseTask("event team meeting /from 2025-01-15 /to 2025-01-16");

        assertInstanceOf(EventTask.class, task);
        assertEquals("team meeting", task.getDescription());
        assertFalse(task.isDone());
        assertTrue(task.toString().contains("[E]"));
        assertTrue(task.toString().contains("Jan 15 2025"));
        assertTrue(task.toString().contains("Jan 16 2025"));
    }

    @Test
    @DisplayName("parseTask should throw exception for invalid event dates")
    void parseTask_invalidEventDates_throwsException() {
        // End date before start date
        InputException ex = assertThrows(InputException.class, 
            () -> Parser.parseTask("event conference /from 2025-01-20 /to 2025-01-15"));
        assertEquals("End date must be on or after the start date.", ex.getMessage());
    }

    @Test
    @DisplayName("parseTask should throw exception for unknown commands")
    void parseTask_unknownCommand_throwsException() {
        InputException ex = assertThrows(InputException.class, 
            () -> Parser.parseTask("unknown command"));
        assertEquals("I'm sorry, but I don't know what that means.", ex.getMessage());
        assertEquals(InputException.Reason.UNKNOWN_COMMAND, ex.getReason());
    }

    @Test
    @DisplayName("parseTask should throw exception for null or empty input")
    void parseTask_nullOrEmptyInput_throwsException() {
        InputException ex1 = assertThrows(InputException.class, 
            () -> Parser.parseTask(null));
        assertEquals("Please enter a command.", ex1.getMessage());
        assertEquals(InputException.Reason.EMPTY_INPUT, ex1.getReason());

        InputException ex2 = assertThrows(InputException.class, 
            () -> Parser.parseTask(""));
        assertEquals("Please enter a command.", ex2.getMessage());

        InputException ex3 = assertThrows(InputException.class, 
            () -> Parser.parseTask("   "));
        assertEquals("Please enter a command.", ex3.getMessage());
    }
}
