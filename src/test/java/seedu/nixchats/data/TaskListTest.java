package seedu.nixchats.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nixchats.DeadlineTask;
import nixchats.EventTask;
import nixchats.Task;
import nixchats.ToDoTask;
import nixchats.data.TaskList;
import nixchats.exception.InputException;

/**
 * AI-Enhanced Test Suite for TaskList class.
 * Tests generated with assistance from GitHub Copilot and Claude.
 */
public class TaskListTest {

    private TaskList taskList;
    private Task todoTask;
    private Task deadlineTask;
    private Task eventTask;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        taskList = new TaskList();
        todoTask = new ToDoTask("read book", false);
        deadlineTask = new DeadlineTask("submit assignment", false, "Jan 31 2025");
        eventTask = new EventTask("team meeting", false, "Jan 15 2025", "Jan 16 2025");

        // AI suggestion: Capture System.out for testing print statements
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    @DisplayName("New TaskList should be empty")
    void constructor_newTaskList_isEmpty() {
        assertTrue(taskList.isEmpty());
        assertEquals(0, taskList.size());
    }

    @Test
    @DisplayName("addTask should add task and increase size")
    void addTask_validTask_increasesSize() {
        taskList.addTask(todoTask);
        assertFalse(taskList.isEmpty());
        assertEquals(1, taskList.size());
        assertEquals(todoTask, taskList.getTask(0));
    }

    @Test
    @DisplayName("addTask should handle multiple tasks")
    void addTask_multipleTasks_maintainsOrder() {
        taskList.addTask(todoTask);
        taskList.addTask(deadlineTask);
        taskList.addTask(eventTask);

        assertEquals(3, taskList.size());
        assertEquals(todoTask, taskList.getTask(0));
        assertEquals(deadlineTask, taskList.getTask(1));
        assertEquals(eventTask, taskList.getTask(2));
    }

    @Test
    @DisplayName("addTask with command string should parse and add task")
    void addTask_commandString_parsesAndAdds() throws InputException {
        taskList.addTask("todo test task");
        assertEquals(1, taskList.size());
        assertEquals("test task", taskList.getTask(0).getDescription());
    }

    @Test
    @DisplayName("addTask should throw exception for null task")
    void addTask_nullTask_throwsAssertionError() {
        // AI enhancement: Test defensive programming
        assertThrows(AssertionError.class, () -> taskList.addTask((Task) null));
    }

    @Test
    @DisplayName("insertTask should insert at specified index")
    void insertTask_validIndex_insertsCorrectly() {
        taskList.addTask(todoTask);
        taskList.addTask(eventTask);
        taskList.insertTask(1, deadlineTask);

        assertEquals(3, taskList.size());
        assertEquals(todoTask, taskList.getTask(0));
        assertEquals(deadlineTask, taskList.getTask(1));
        assertEquals(eventTask, taskList.getTask(2));
    }

    @Test
    @DisplayName("deleteTask should remove task at index")
    void deleteTask_validIndex_removesTask() {
        taskList.addTask(todoTask);
        taskList.addTask(deadlineTask);
        taskList.deleteTask(0);

        assertEquals(1, taskList.size());
        assertEquals(deadlineTask, taskList.getTask(0));
        tearDown();
    }

    @Test
    @DisplayName("deleteTask with message should print confirmation")
    void deleteTask_withMessage_printsConfirmation() {
        taskList.addTask(todoTask);
        taskList.deleteTask(0, true);

        String output = outputStream.toString();
        assertTrue(output.contains("Got it, deleted task"));
        assertTrue(output.contains("read book"));
        tearDown();
    }

    @Test
    @DisplayName("deleteTask without message should not print")
    void deleteTask_withoutMessage_doesNotPrint() {
        taskList.addTask(todoTask);
        taskList.deleteTask(0, false);

        String output = outputStream.toString();
        assertTrue(output.isEmpty());
        tearDown();
    }

    @Test
    @DisplayName("getTask should throw assertion error for invalid index")
    void getTask_invalidIndex_throwsAssertionError() {
        taskList.addTask(todoTask);
        
        // AI enhancement: Test boundary conditions
        assertThrows(AssertionError.class, () -> taskList.getTask(-1));
        assertThrows(AssertionError.class, () -> taskList.getTask(1));
        assertThrows(AssertionError.class, () -> taskList.getTask(100));
    }

    @Test
    @DisplayName("findTasks should return matching tasks")
    void findTasks_matchingKeyword_returnsCorrectTasks() {
        taskList.addTask(new ToDoTask("read book", false));
        taskList.addTask(new ToDoTask("buy book", false));
        taskList.addTask(new ToDoTask("write report", false));

        var results = taskList.findTasks("book");
        assertEquals(2, results.size());
        assertTrue(results.contains(taskList.getTask(0)));
        assertTrue(results.contains(taskList.getTask(1)));
    }

    @Test
    @DisplayName("findTasks should be case insensitive")
    void findTasks_caseInsensitive_returnsMatches() {
        taskList.addTask(new ToDoTask("Read Book", false));
        
        var results = taskList.findTasks("book");
        assertEquals(1, results.size());
        
        results = taskList.findTasks("READ");
        assertEquals(1, results.size());
    }

    @Test
    @DisplayName("findTasks should return empty list for no matches")
    void findTasks_noMatches_returnsEmptyList() {
        taskList.addTask(todoTask);
        
        var results = taskList.findTasks("nonexistent");
        assertTrue(results.isEmpty());
    }

    @Test
    @DisplayName("iterator should iterate through all tasks")
    void iterator_allTasks_iteratesCorrectly() {
        taskList.addTask(todoTask);
        taskList.addTask(deadlineTask);

        int count = 0;
        for (Task task : taskList) {
            count++;
            assertTrue(task == todoTask || task == deadlineTask);
        }
        assertEquals(2, count);
    }

    @Test
    @DisplayName("printTasks should output all tasks")
    void printTasks_multipleTasks_printsAll() {
        taskList.addTask(todoTask);
        taskList.addTask(deadlineTask);
        taskList.printTasks();

        String output = outputStream.toString();
        assertTrue(output.contains("read book"));
        assertTrue(output.contains("submit assignment"));
        tearDown();
    }

    @Test
    @DisplayName("TaskList should handle edge cases gracefully")
    void edgeCases_emptyOperations_handleGracefully() {
        // AI enhancement: Test edge cases
        assertTrue(taskList.isEmpty());
        assertEquals(0, taskList.size());
        
        // Should not crash when iterating empty list
        for (Task task : taskList) {
            // Should not execute
            assertEquals("", task.getDescription());
        }
        
        // Find on empty list should return empty
        assertTrue(taskList.findTasks("anything").isEmpty());
    }
}
