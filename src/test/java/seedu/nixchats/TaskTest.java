package seedu.nixchats;

import nixchats.DeadlineTask;
import nixchats.EventTask;
import nixchats.ToDoTask;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class TaskTest {

    private ToDoTask todoTask;
    private DeadlineTask deadlineTask;
    private EventTask eventTask;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        todoTask = new ToDoTask("read book", false);
        deadlineTask = new DeadlineTask("submit assignment", false, "Jan 31 2025");
        eventTask = new EventTask("team meeting", false, "Jan 15 2025", "Jan 16 2025");

        // Capture System.out for testing print statements
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    void tearDown() {
        // Restore original System.out
        System.setOut(originalOut);
    }

    @Test
    @DisplayName("markAsDone should change task status and print confirmation")
    void markAsDone_taskNotDone_changesStatusAndPrintsMessage() {
        // Initial state
        assertFalse(todoTask.isDone());
        assertEquals(" ", todoTask.getStatusIcon());

        // Mark as done
        todoTask.markAsDone();

        // Verify state change
        assertTrue(todoTask.isDone());
        assertEquals("X", todoTask.getStatusIcon());

        // Verify printed message
        String output = outputStream.toString();
        assertTrue(output.contains("Nice! I've marked this task as done:"));
        assertTrue(output.contains("[T][X] read book"));

        tearDown();
    }

    @Test
    @DisplayName("markAsDone should work correctly when task is already done")
    void markAsDone_taskAlreadyDone_maintainsStatusAndPrintsMessage() {
        // Create already done task
        ToDoTask doneTask = new ToDoTask("completed task", true);
        assertTrue(doneTask.isDone());
        assertEquals("X", doneTask.getStatusIcon());

        // Mark as done again
        doneTask.markAsDone();

        // Should still be done
        assertTrue(doneTask.isDone());
        assertEquals("X", doneTask.getStatusIcon());

        // Should still print confirmation
        String output = outputStream.toString();
        assertTrue(output.contains("Nice! I've marked this task as done:"));

        tearDown();
    }

    @Test
    @DisplayName("unmarkAsNotDone should change task status and print confirmation")
    void unmarkAsNotDone_taskDone_changesStatusAndPrintsMessage() {
        // Start with done task
        ToDoTask doneTask = new ToDoTask("completed task", true);
        assertTrue(doneTask.isDone());
        assertEquals("X", doneTask.getStatusIcon());

        // Unmark as done
        doneTask.unmarkAsNotDone();

        // Verify state change
        assertFalse(doneTask.isDone());
        assertEquals(" ", doneTask.getStatusIcon());

        // Verify printed message
        String output = outputStream.toString();
        assertTrue(output.contains("OK, I've marked this task as not done yet:"));
        assertTrue(output.contains("[T][ ] completed task"));

        tearDown();
    }

    @Test
    @DisplayName("unmarkAsNotDone should work correctly when task is already not done")
    void unmarkAsNotDone_taskAlreadyNotDone_maintainsStatusAndPrintsMessage() {
        // Initial state - not done
        assertFalse(todoTask.isDone());
        assertEquals(" ", todoTask.getStatusIcon());

        // Unmark as done
        todoTask.unmarkAsNotDone();

        // Should still be not done
        assertFalse(todoTask.isDone());
        assertEquals(" ", todoTask.getStatusIcon());

        // Should still print confirmation
        String output = outputStream.toString();
        assertTrue(output.contains("OK, I've marked this task as not done yet:"));

        tearDown();
    }

    @Test
    @DisplayName("getStatusIcon should return correct icon for both states")
    void getStatusIcon_bothStates_returnsCorrectIcon() {
        // Not done task
        ToDoTask notDoneTask = new ToDoTask("pending task", false);
        assertEquals(" ", notDoneTask.getStatusIcon());

        // Done task
        ToDoTask doneTask = new ToDoTask("finished task", true);
        assertEquals("X", doneTask.getStatusIcon());
    }

    @Test
    @DisplayName("isDone should return correct boolean value")
    void isDone_bothStates_returnsCorrectValue() {
        // Not done task
        ToDoTask notDoneTask = new ToDoTask("pending task", false);
        assertFalse(notDoneTask.isDone());

        // Done task
        ToDoTask doneTask = new ToDoTask("finished task", true);
        assertTrue(doneTask.isDone());
    }

    @Test
    @DisplayName("getDescription should return correct description")
    void getDescription_variousInputs_returnsCorrectDescription() {
        assertEquals("read book", todoTask.getDescription());
        assertEquals("submit assignment", deadlineTask.getDescription());
        assertEquals("team meeting", eventTask.getDescription());

        // Test with empty description
        ToDoTask emptyTask = new ToDoTask("", false);
        assertEquals("", emptyTask.getDescription());

        // Test with description containing special characters
        ToDoTask specialTask = new ToDoTask("buy coffee @ 3pm & donuts!", false);
        assertEquals("buy coffee @ 3pm & donuts!", specialTask.getDescription());
    }

    @Test
    @DisplayName("toString should format task correctly for different types and states")
    void toString_variousTaskTypesAndStates_formatsCorrectly() {
        // Todo task - not done
        assertEquals("[T][ ] read book", todoTask.toString());

        // Todo task - done
        todoTask.markAsDone();
        assertEquals("[T][X] read book", todoTask.toString());

        // Reset output stream for next test
        outputStream.reset();

        // Deadline task - not done
        assertEquals("[D][ ] submit assignment (by: Jan 31 2025)", deadlineTask.toString());

        // Deadline task - done
        deadlineTask.markAsDone();
        assertEquals("[D][X] submit assignment (by: Jan 31 2025)", deadlineTask.toString());

        tearDown();
    }

    @Test
    @DisplayName("Task state transitions should be consistent")
    void taskStateTransitions_multipleChanges_remainsConsistent() {
        // Start not done
        assertFalse(todoTask.isDone());
        assertEquals(" ", todoTask.getStatusIcon());

        // Mark as done
        todoTask.markAsDone();
        assertTrue(todoTask.isDone());
        assertEquals("X", todoTask.getStatusIcon());

        outputStream.reset(); // Clear output for next test

        // Unmark as done
        todoTask.unmarkAsNotDone();
        assertFalse(todoTask.isDone());
        assertEquals(" ", todoTask.getStatusIcon());

        outputStream.reset(); // Clear output for next test

        // Mark as done again
        todoTask.markAsDone();
        assertTrue(todoTask.isDone());
        assertEquals("X", todoTask.getStatusIcon());

        // Verify final state is consistent
        assertTrue(todoTask.toString().contains("[X]"));

        tearDown();
    }

    @Test
    @DisplayName("Task construction with different initial states")
    void taskConstruction_differentInitialStates_createsCorrectly() {
        // Create task as not done
        ToDoTask notDoneTask = new ToDoTask("not done task", false);
        assertFalse(notDoneTask.isDone());
        assertEquals(" ", notDoneTask.getStatusIcon());
        assertEquals("not done task", notDoneTask.getDescription());

        // Create task as done
        ToDoTask doneTask = new ToDoTask("done task", true);
        assertTrue(doneTask.isDone());
        assertEquals("X", doneTask.getStatusIcon());
        assertEquals("done task", doneTask.getDescription());

        tearDown();
    }
}
