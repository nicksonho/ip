package seedu.nixchats.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nixchats.DeadlineTask;
import nixchats.Task;
import nixchats.ToDoTask;
import nixchats.command.AddTaskCommand;
import nixchats.command.DeleteTaskCommand;
import nixchats.command.MarkTaskCommand;
import nixchats.command.UnmarkTaskCommand;
import nixchats.data.TaskList;

/**
 * AI-Enhanced Test Suite for Command classes (Undo functionality).
 * Tests the Command Pattern implementation for undoable operations.
 * Generated with assistance from GitHub Copilot and Claude.
 */
public class CommandTest {

    private TaskList taskList;
    private Task todoTask;
    private Task deadlineTask;

    @BeforeEach
    void setUp() {
        taskList = new TaskList();
        todoTask = new ToDoTask("test todo", false);
        deadlineTask = new DeadlineTask("test deadline", false, "Jan 31 2025");
    }

    // AI-Enhanced: AddTaskCommand tests
    @Test
    @DisplayName("AddTaskCommand should add task to list")
    void addTaskCommand_execute_addsTaskToList() {
        AddTaskCommand command = new AddTaskCommand(taskList, todoTask);
        
        assertEquals(0, taskList.size());
        command.execute();
        assertEquals(1, taskList.size());
        assertEquals(todoTask, taskList.getTask(0));
    }

    @Test
    @DisplayName("AddTaskCommand undo should remove the added task")
    void addTaskCommand_undo_removesAddedTask() {
        AddTaskCommand command = new AddTaskCommand(taskList, todoTask);
        
        command.execute();
        assertEquals(1, taskList.size());
        
        command.undo();
        assertEquals(0, taskList.size());
    }

    @Test
    @DisplayName("AddTaskCommand should provide correct description")
    void addTaskCommand_getDescription_returnsCorrectDescription() {
        AddTaskCommand command = new AddTaskCommand(taskList, todoTask);
        assertEquals("add task: test todo", command.getDescription());
    }

    @Test
    @DisplayName("AddTaskCommand undo should handle multiple tasks correctly")
    void addTaskCommand_undoWithMultipleTasks_removesCorrectTask() {
        taskList.addTask(deadlineTask); // Add a task first
        AddTaskCommand command = new AddTaskCommand(taskList, todoTask);
        
        command.execute();
        assertEquals(2, taskList.size());
        assertEquals(todoTask, taskList.getTask(1)); // Should be at index 1
        
        command.undo();
        assertEquals(1, taskList.size());
        assertEquals(deadlineTask, taskList.getTask(0)); // Original task should remain
    }

    // AI-Enhanced: DeleteTaskCommand tests
    @Test
    @DisplayName("DeleteTaskCommand should remove task from list")
    void deleteTaskCommand_execute_removesTaskFromList() {
        taskList.addTask(todoTask);
        taskList.addTask(deadlineTask);
        
        DeleteTaskCommand command = new DeleteTaskCommand(taskList, 0);
        command.execute();
        
        assertEquals(1, taskList.size());
        assertEquals(deadlineTask, taskList.getTask(0));
    }

    @Test
    @DisplayName("DeleteTaskCommand undo should restore deleted task")
    void deleteTaskCommand_undo_restoresDeletedTask() {
        taskList.addTask(todoTask);
        taskList.addTask(deadlineTask);
        
        DeleteTaskCommand command = new DeleteTaskCommand(taskList, 0);
        command.execute();
        assertEquals(1, taskList.size());
        
        command.undo();
        assertEquals(2, taskList.size());
        assertEquals(todoTask, taskList.getTask(0));
        assertEquals(deadlineTask, taskList.getTask(1));
    }

    @Test
    @DisplayName("DeleteTaskCommand should provide correct description")
    void deleteTaskCommand_getDescription_returnsCorrectDescription() {
        taskList.addTask(todoTask);
        DeleteTaskCommand command = new DeleteTaskCommand(taskList, 0);
        command.execute(); // Need to execute to save the deleted task
        
        assertTrue(command.getDescription().contains("delete task: test todo"));
    }

    // AI-Enhanced: MarkTaskCommand tests
    @Test
    @DisplayName("MarkTaskCommand should mark task as done")
    void markTaskCommand_execute_marksTaskAsDone() {
        taskList.addTask(todoTask);
        assertFalse(todoTask.isDone());
        
        MarkTaskCommand command = new MarkTaskCommand(taskList, 0);
        command.execute();
        
        assertTrue(todoTask.isDone());
    }

    @Test
    @DisplayName("MarkTaskCommand undo should restore previous state")
    void markTaskCommand_undo_restoresPreviousState() {
        taskList.addTask(todoTask);
        
        MarkTaskCommand command = new MarkTaskCommand(taskList, 0);
        command.execute();
        assertTrue(todoTask.isDone());
        
        command.undo();
        assertFalse(todoTask.isDone());
    }

    @Test
    @DisplayName("MarkTaskCommand should handle already done task")
    void markTaskCommand_alreadyDoneTask_maintainsState() {
        Task doneTask = new ToDoTask("done task", true);
        taskList.addTask(doneTask);
        
        MarkTaskCommand command = new MarkTaskCommand(taskList, 0);
        command.execute();
        assertTrue(doneTask.isDone());
        
        command.undo();
        assertTrue(doneTask.isDone()); // Should remain done
    }

    @Test
    @DisplayName("MarkTaskCommand should provide correct description")
    void markTaskCommand_getDescription_returnsCorrectDescription() {
        taskList.addTask(todoTask);
        MarkTaskCommand command = new MarkTaskCommand(taskList, 0);
        
        assertEquals("mark task: test todo", command.getDescription());
    }

    // AI-Enhanced: UnmarkTaskCommand tests
    @Test
    @DisplayName("UnmarkTaskCommand should unmark task as not done")
    void unmarkTaskCommand_execute_unmarksTask() {
        Task doneTask = new ToDoTask("done task", true);
        taskList.addTask(doneTask);
        assertTrue(doneTask.isDone());
        
        UnmarkTaskCommand command = new UnmarkTaskCommand(taskList, 0);
        command.execute();
        
        assertFalse(doneTask.isDone());
    }

    @Test
    @DisplayName("UnmarkTaskCommand undo should restore previous state")
    void unmarkTaskCommand_undo_restoresPreviousState() {
        Task doneTask = new ToDoTask("done task", true);
        taskList.addTask(doneTask);
        
        UnmarkTaskCommand command = new UnmarkTaskCommand(taskList, 0);
        command.execute();
        assertFalse(doneTask.isDone());
        
        command.undo();
        assertTrue(doneTask.isDone());
    }

    @Test
    @DisplayName("UnmarkTaskCommand should handle already undone task")
    void unmarkTaskCommand_alreadyUndoneTask_maintainsState() {
        taskList.addTask(todoTask); // Already not done
        
        UnmarkTaskCommand command = new UnmarkTaskCommand(taskList, 0);
        command.execute();
        assertFalse(todoTask.isDone());
        
        command.undo();
        assertFalse(todoTask.isDone()); // Should remain not done
    }

    @Test
    @DisplayName("UnmarkTaskCommand should provide correct description")
    void unmarkTaskCommand_getDescription_returnsCorrectDescription() {
        taskList.addTask(todoTask);
        UnmarkTaskCommand command = new UnmarkTaskCommand(taskList, 0);
        
        assertEquals("unmark task: test todo", command.getDescription());
    }

    // AI-Enhanced: Integration tests for command sequences
    @Test
    @DisplayName("Multiple commands should work together correctly")
    void multipleCommands_executeAndUndo_worksTogether() {
        // Add two tasks
        AddTaskCommand addCmd1 = new AddTaskCommand(taskList, todoTask);
        AddTaskCommand addCmd2 = new AddTaskCommand(taskList, deadlineTask);
        
        addCmd1.execute();
        addCmd2.execute();
        assertEquals(2, taskList.size());
        
        // Mark first task as done
        MarkTaskCommand markCmd = new MarkTaskCommand(taskList, 0);
        markCmd.execute();
        assertTrue(todoTask.isDone());
        
        // Delete second task
        DeleteTaskCommand deleteCmd = new DeleteTaskCommand(taskList, 1);
        deleteCmd.execute();
        assertEquals(1, taskList.size());
        
        // Undo operations in reverse order
        deleteCmd.undo();
        assertEquals(2, taskList.size());
        assertEquals(deadlineTask, taskList.getTask(1));
        
        markCmd.undo();
        assertFalse(todoTask.isDone());
        
        addCmd2.undo();
        assertEquals(1, taskList.size());
        
        addCmd1.undo();
        assertEquals(0, taskList.size());
    }

    @Test
    @DisplayName("Commands should handle edge cases gracefully")
    void commands_edgeCases_handleGracefully() {
        // AI enhancement: Test boundary conditions
        taskList.addTask(todoTask);
        
        // Test marking already marked task
        MarkTaskCommand markCmd = new MarkTaskCommand(taskList, 0);
        markCmd.execute();
        markCmd.execute(); // Second mark should not crash
        assertTrue(todoTask.isDone());
        
        // Test unmarking already unmarked task
        UnmarkTaskCommand unmarkCmd = new UnmarkTaskCommand(taskList, 0);
        unmarkCmd.execute();
        unmarkCmd.execute(); // Second unmark should not crash
        assertFalse(todoTask.isDone());
    }
}
