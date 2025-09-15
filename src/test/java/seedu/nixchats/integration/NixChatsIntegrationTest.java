package seedu.nixchats.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import nixchats.NixChats;

/**
 * AI-Enhanced Integration Test Suite for NixChats main class.
 * Tests end-to-end functionality including command processing and data persistence.
 * Generated with assistance from GitHub Copilot and Claude.
 */
public class NixChatsIntegrationTest {

    @TempDir
    Path tempDir;

    private NixChats nixchats;

    @BeforeEach
    void setUp() throws IOException {
        // AI enhancement: Clean the data file before each test for isolation
        Path dataFile = Path.of("data", "NixChatHistory.txt");
        if (Files.exists(dataFile)) {
            Files.delete(dataFile);
        }
        // Use clean NixChats instance for each test
        nixchats = new NixChats();
    }

    @Test
    @DisplayName("Adding tasks should work correctly")
    void addTasks_variousTypes_worksCorrectly() {
        String response1 = nixchats.getResponse("todo read book");
        assertTrue(response1.contains("Got it, I have added: todo read book"));
        assertEquals("add", nixchats.getCommandType());

        String response2 = nixchats.getResponse("deadline submit assignment /by 2025-01-31");
        assertTrue(response2.contains("Got it, I have added: deadline submit assignment /by 2025-01-31"));
        assertEquals("add", nixchats.getCommandType());

        String response3 = nixchats.getResponse("event team meeting /from 2025-01-15 /to 2025-01-16");
        assertTrue(response3.contains("Got it, I have added: event team meeting /from 2025-01-15 /to 2025-01-16"));
        assertEquals("add", nixchats.getCommandType());
    }

    @Test
    @DisplayName("List command should show added tasks")
    void listCommand_withTasks_showsAllTasks() {
        nixchats.getResponse("todo task 1");
        nixchats.getResponse("todo task 2");
        
        String response = nixchats.getResponse("list");
        assertTrue(response.contains("Here are the tasks in your list:"));
        assertTrue(response.contains("[T][ ] task 1"));
        assertTrue(response.contains("[T][ ] task 2"));
        assertEquals("list", nixchats.getCommandType());
    }

    @Test
    @DisplayName("List command should handle empty task list")
    void listCommand_emptyList_showsNoTasks() {
        String response = nixchats.getResponse("list");
        assertTrue(response.contains("Here are the tasks in your list:"));
        assertTrue(response.contains("No tasks found."));
        assertEquals("list", nixchats.getCommandType());
    }

    @Test
    @DisplayName("Mark and unmark commands should work correctly")
    void markUnmarkCommands_validTasks_worksCorrectly() {
        nixchats.getResponse("todo test task");
        
        String markResponse = nixchats.getResponse("mark 1");
        assertTrue(markResponse.contains("Nice! I've marked this task as done:"));
        assertEquals("mark", nixchats.getCommandType());
        
        String unmarkResponse = nixchats.getResponse("unmark 1");
        assertTrue(unmarkResponse.contains("OK, I've marked this task as not done yet:"));
        assertEquals("unmark", nixchats.getCommandType());
    }

    @Test
    @DisplayName("Delete command should remove tasks")
    void deleteCommand_validTask_removesTask() {
        nixchats.getResponse("todo test task");
        
        String deleteResponse = nixchats.getResponse("delete 1");
        assertTrue(deleteResponse.contains("Got it, deleted task [T][ ] test task"));
        assertEquals("delete", nixchats.getCommandType());
        
        String listResponse = nixchats.getResponse("list");
        assertTrue(listResponse.contains("No tasks found."));
    }

    @Test
    @DisplayName("Find command should locate matching tasks")
    void findCommand_matchingTasks_returnsResults() {
        nixchats.getResponse("todo read book");
        nixchats.getResponse("todo buy book");
        nixchats.getResponse("todo write report");
        
        String response = nixchats.getResponse("find book");
        assertTrue(response.contains("Here are the matching tasks in your list:"));
        assertTrue(response.contains("read book"));
        assertTrue(response.contains("buy book"));
        assertTrue(!response.contains("write report"));
        assertEquals("find", nixchats.getCommandType());
    }

    @Test
    @DisplayName("Find command should handle no matches")
    void findCommand_noMatches_returnsNoResults() {
        nixchats.getResponse("todo read book");
        
        String response = nixchats.getResponse("find nonexistent");
        assertTrue(response.contains("No matching tasks found."));
        assertEquals("find", nixchats.getCommandType());
    }

    @Test
    @DisplayName("Undo command should reverse operations")
    void undoCommand_afterOperations_reversesChanges() {
        // Add a task
        nixchats.getResponse("todo test task");
        
        // Verify task is added
        String listResponse1 = nixchats.getResponse("list");
        assertTrue(listResponse1.contains("[T][ ] test task"));
        
        // Undo the add operation
        String undoResponse = nixchats.getResponse("undo");
        assertTrue(undoResponse.contains("Undone: add task: test task"));
        assertEquals("undo", nixchats.getCommandType());
        
        // Verify task is removed
        String listResponse2 = nixchats.getResponse("list");
        assertTrue(listResponse2.contains("No tasks found."));
    }

    @Test
    @DisplayName("Undo command should handle empty history")
    void undoCommand_emptyHistory_returnsNothingToUndo() {
        String response = nixchats.getResponse("undo");
        assertTrue(response.contains("Nothing to undo."));
        assertEquals("undo", nixchats.getCommandType());
    }

    @Test
    @DisplayName("Multiple undo operations should work correctly")
    void multipleUndo_sequentialOperations_reversesInOrder() {
        // Perform multiple operations
        nixchats.getResponse("todo task 1");
        nixchats.getResponse("todo task 2");
        nixchats.getResponse("mark 1");
        
        // Undo mark operation
        String undo1 = nixchats.getResponse("undo");
        assertTrue(undo1.contains("Undone: mark task: task 1"));
        
        // Undo add task 2
        String undo2 = nixchats.getResponse("undo");
        assertTrue(undo2.contains("Undone: add task: task 2"));
        
        // Verify only task 1 remains
        String listResponse = nixchats.getResponse("list");
        assertTrue(listResponse.contains("[T][ ] task 1"));
        assertTrue(!listResponse.contains("task 2"));
    }

    @Test
    @DisplayName("Error handling should work correctly")
    void errorHandling_invalidCommands_returnsErrorMessages() {
        // Test invalid task number
        String response1 = nixchats.getResponse("mark 999");
        assertTrue(response1.contains("Task number out of range"));
        assertEquals("error", nixchats.getCommandType());
        
        // Test invalid command
        String response2 = nixchats.getResponse("invalidcommand");
        assertTrue(response2.contains("I'm sorry, but I don't know what that means"));
        assertEquals("error", nixchats.getCommandType());
        
        // Test empty todo
        String response3 = nixchats.getResponse("todo");
        assertTrue(response3.contains("The description of a todo cannot be empty"));
        assertEquals("error", nixchats.getCommandType());
    }

    @Test
    @DisplayName("Bye command should work correctly")
    void byeCommand_always_returnsGoodbye() {
        String response = nixchats.getResponse("bye");
        assertTrue(response.contains("Bye! Hope to see you again soon!"));
        assertEquals("bye", nixchats.getCommandType());
    }

    @Test
    @DisplayName("Complex workflow should work end-to-end")
    void complexWorkflow_multipleOperations_worksCorrectly() {
        // AI enhancement: Test realistic user workflow
        
        // Add multiple tasks
        nixchats.getResponse("todo buy groceries");
        nixchats.getResponse("deadline submit report /by 2025-12-31");
        nixchats.getResponse("event team meeting /from 2025-01-15 /to 2025-01-16");
        
        // Mark one as done
        nixchats.getResponse("mark 1");
        
        // Find tasks
        String findResponse = nixchats.getResponse("find team");
        assertTrue(findResponse.contains("team meeting"));
        
        // Delete a task
        nixchats.getResponse("delete 2");
        
        // Check final state
        String finalList = nixchats.getResponse("list");
        assertTrue(finalList.contains("buy groceries"));
        assertTrue(finalList.contains("[T][X]")); // Should be marked as done
        assertTrue(finalList.contains("team meeting"));
        assertTrue(!finalList.contains("submit report")); // Should be deleted
        
        // Test undo sequence
        String undo1 = nixchats.getResponse("undo"); // Undo delete
        assertTrue(undo1.contains("Undone: delete task"));
        
        String undo2 = nixchats.getResponse("undo"); // Undo mark
        assertTrue(undo2.contains("Undone: mark task"));
        
        // Verify state after undos
        String afterUndoList = nixchats.getResponse("list");
        assertTrue(afterUndoList.contains("submit report")); // Should be restored
        assertTrue(afterUndoList.contains("[ ]")); // Should be unmarked
    }

    @Test
    @DisplayName("Command type tracking should work correctly")
    void commandTypeTracking_variousCommands_tracksCorrectly() {
        // AI enhancement: Test command type tracking for GUI styling
        
        nixchats.getResponse("todo test");
        assertEquals("add", nixchats.getCommandType());
        
        nixchats.getResponse("list");
        assertEquals("list", nixchats.getCommandType());
        
        nixchats.getResponse("mark 1");
        assertEquals("mark", nixchats.getCommandType());
        
        nixchats.getResponse("find test");
        assertEquals("find", nixchats.getCommandType());
        
        nixchats.getResponse("invalid command");
        assertEquals("error", nixchats.getCommandType());
        
        nixchats.getResponse("bye");
        assertEquals("bye", nixchats.getCommandType());
    }
}
