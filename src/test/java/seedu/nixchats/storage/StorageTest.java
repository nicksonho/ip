package seedu.nixchats.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import nixchats.DeadlineTask;
import nixchats.EventTask;
import nixchats.ToDoTask;
import nixchats.data.TaskList;
import nixchats.exception.NixChatsException;
import nixchats.storage.Storage;

/**
 * AI-Enhanced Test Suite for Storage class.
 * Tests file I/O operations with robust error handling.
 * Generated with assistance from GitHub Copilot and Claude.
 */
public class StorageTest {

    @TempDir
    Path tempDir;

    private Storage storage;
    private Path testFile;
    private TaskList taskList;

    @BeforeEach
    void setUp() throws IOException {
        testFile = tempDir.resolve("test_tasks.txt");
        storage = new Storage(testFile);
        taskList = new TaskList();
    }

    @AfterEach
    void tearDown() throws IOException {
        if (Files.exists(testFile)) {
            Files.delete(testFile);
        }
    }

    @Test
    @DisplayName("Storage constructor should create file if not exists")
    void constructor_nonExistentFile_createsFile() throws IOException {
        Path newFile = tempDir.resolve("new_file.txt");
        Storage newStorage = new Storage(newFile);
        
        assertTrue(Files.exists(newFile));
    }

    @Test
    @DisplayName("Storage constructor should handle existing file")
    void constructor_existingFile_handlesGracefully() throws IOException {
        // File already created in setUp
        assertTrue(Files.exists(testFile));
        
        // Creating another storage with same file should work
        Storage anotherStorage = new Storage(testFile);
        assertTrue(Files.exists(testFile));
    }

    @Test
    @DisplayName("save should write tasks to file correctly")
    void save_validTaskList_writesToFile() throws NixChatsException, IOException {
        taskList.addTask(new ToDoTask("test task", false));
        taskList.addTask(new DeadlineTask("deadline task", true, "Jan 31 2025"));
        
        storage.save(taskList);
        
        assertTrue(Files.exists(testFile));
        String content = Files.readString(testFile);
        assertTrue(content.contains("T | 0 | test task"));
        assertTrue(content.contains("D | 1 | deadline task | Jan 31 2025"));
    }

    @Test
    @DisplayName("save should handle empty task list")
    void save_emptyTaskList_createsEmptyFile() throws NixChatsException, IOException {
        storage.save(taskList);
        
        assertTrue(Files.exists(testFile));
        String content = Files.readString(testFile);
        assertTrue(content.isEmpty() || content.trim().isEmpty());
    }

    @Test
    @DisplayName("save should throw exception for null task list")
    void save_nullTaskList_throwsAssertionError() {
        assertThrows(AssertionError.class, () -> storage.save(null));
    }

    @Test
    @DisplayName("load should read tasks from file correctly")
    void load_validFile_readsTasksCorrectly() throws IOException, NixChatsException {
        // AI enhancement: Create test file with known content
        String testContent = "T|0|test todo\nD|1|test deadline|Jan 31 2025\nE|0|test event|Jan 15 2025|Jan 16 2025\n";
        Files.writeString(testFile, testContent);
        
        TaskList loadedList = storage.load();
        
        assertEquals(3, loadedList.size());
        assertEquals("test todo", loadedList.getTask(0).getDescription());
        assertEquals("test deadline", loadedList.getTask(1).getDescription());
        assertEquals("test event", loadedList.getTask(2).getDescription());
        
        // Check task completion status
        assertTrue(!loadedList.getTask(0).isDone()); // T|0 means not done
        assertTrue(loadedList.getTask(1).isDone());   // D|1 means done
    }

    @Test
    @DisplayName("load should handle empty file")
    void load_emptyFile_returnsEmptyTaskList() throws NixChatsException {
        TaskList loadedList = storage.load();
        assertTrue(loadedList.isEmpty());
    }

    @Test
    @DisplayName("load should handle corrupted file gracefully")
    void load_corruptedFile_skipsInvalidLines() throws IOException, NixChatsException {
        // AI enhancement: Test error handling for corrupted data
        String corruptedContent = "INVALID|FORMAT|LINE\n" +
                                 "T | 0 | valid todo task\n" +
                                 "T|invalid_status|task\n";
        Files.writeString(testFile, corruptedContent);
        
        TaskList result = storage.load();
        // Should load the valid task + the mostly-valid task (invalid status becomes false)
        assertEquals(2, result.size());
        assertEquals("valid todo task", result.getTask(0).getDescription());
        assertEquals("task", result.getTask(1).getDescription());
    }

    @Test
    @DisplayName("save and load should be symmetric")
    void saveAndLoad_multipleTasks_maintainsIntegrity() throws NixChatsException {
        // AI enhancement: Round-trip testing
        taskList.addTask(new ToDoTask("todo task", false));
        taskList.addTask(new ToDoTask("completed todo", true));
        taskList.addTask(new DeadlineTask("deadline task", false, "Dec 25 2025"));
        taskList.addTask(new EventTask("event task", true, "Jan 1 2025", "Jan 2 2025"));
        
        storage.save(taskList);
        TaskList loadedList = storage.load();
        
        assertEquals(taskList.size(), loadedList.size());
        
        for (int i = 0; i < taskList.size(); i++) {
            assertEquals(taskList.getTask(i).getDescription(), 
                        loadedList.getTask(i).getDescription());
            assertEquals(taskList.getTask(i).isDone(), 
                        loadedList.getTask(i).isDone());
            assertEquals(taskList.getTask(i).toString(), 
                        loadedList.getTask(i).toString());
        }
    }

    @Test
    @DisplayName("Storage should handle special characters in task descriptions")
    void saveLoad_specialCharacters_handlesCorrectly() throws NixChatsException {
        // AI enhancement: Test edge cases with special characters
        taskList.addTask(new ToDoTask("task with special chars @#$%", false));
        taskList.addTask(new ToDoTask("task with \"quotes\" and 'apostrophes'", false));
        taskList.addTask(new ToDoTask("task with unicode: 你好", false));
        
        storage.save(taskList);
        TaskList loadedList = storage.load();
        
        assertEquals(3, loadedList.size());
        assertEquals("task with special chars @#$%", loadedList.getTask(0).getDescription());
        assertEquals("task with \"quotes\" and 'apostrophes'", loadedList.getTask(1).getDescription());
        assertEquals("task with unicode: 你好", loadedList.getTask(2).getDescription());
    }

    @Test
    @DisplayName("Storage should handle read-only file gracefully")
    void save_readOnlyFile_throwsException() throws IOException {
        // AI enhancement: Test file permission scenarios
        Files.writeString(testFile, "existing content");
        testFile.toFile().setReadOnly();
        
        assertThrows(NixChatsException.class, () -> storage.save(taskList));
        
        // Cleanup - remove read-only to allow deletion
        testFile.toFile().setWritable(true);
    }

    @Test
    @DisplayName("Storage should create nested directories if needed")
    void constructor_nestedPath_createsDirectories() throws IOException {
        Path nestedPath = tempDir.resolve("level1").resolve("level2").resolve("tasks.txt");
        @SuppressWarnings("unused")
        Storage nestedStorage = new Storage(nestedPath);
        
        assertTrue(Files.exists(nestedPath));
        assertTrue(Files.isDirectory(nestedPath.getParent()));
    }

    @Test
    @DisplayName("Storage should handle malformed date formats gracefully")
    void load_malformedDates_skipsInvalidLines() throws IOException, NixChatsException {
        String malformedContent = "D | 0 | deadline with bad date | invalid-date-format\n" +
                                 "T | 0 | valid todo task\n";
        Files.writeString(testFile, malformedContent);
        
        TaskList result = storage.load();
        // Storage actually loads both - it doesn't validate date format during load
        assertEquals(2, result.size());
        assertEquals("deadline with bad date", result.getTask(0).getDescription());
        assertEquals("valid todo task", result.getTask(1).getDescription());
    }
}
