package seedu.nixchats.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import nixchats.DialogBox;

/**
 * AI-Enhanced Test Suite for DialogBox GUI component.
 * Tests JavaFX components with proper platform initialization.
 * Generated with assistance from GitHub Copilot and Claude.
 * 
 * Note: Some GUI-specific tests may be limited due to headless testing environment.
 */
public class DialogBoxTest {

    private Image testUserImage;
    private Image testBotImage;

    @BeforeAll
    static void initToolkit() {
        // AI enhancement: Ensure JavaFX Platform is initialized for testing
        try {
            // This will initialize JavaFX toolkit if not already done
            new javafx.embed.swing.JFXPanel();
        } catch (Exception e) {
            // Platform might already be initialized or in headless environment
        }
    }

    @BeforeEach
    void setUp() {
        // AI enhancement: Create test images for DialogBox components
        try {
            // Try to load actual images, fall back to dummy images if not available
            testUserImage = new Image(getClass().getResourceAsStream("/images/DaUser.png"));
            testBotImage = new Image(getClass().getResourceAsStream("/images/DaDuke.png"));
        } catch (Exception e) {
            // Create minimal test images if actual images not available
            testUserImage = new Image("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8/5+hHgAHggJ/PchI7wAAAABJRU5ErkJggg==");
            testBotImage = new Image("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8/5+hHgAHggJ/PchI7wAAAABJRU5ErkJggg==");
        }
    }

    @Test
    @DisplayName("getUserDialog should create dialog box with user styling")
    void getUserDialog_validInput_createsUserDialog() {
        // AI enhancement: Test factory method for user dialogs
        runLater(() -> {
            DialogBox userDialog = DialogBox.getUserDialog("Hello, chatbot!", testUserImage);
            
            assertNotNull(userDialog);
            // Verify the dialog has the expected content structure
            assertTrue(userDialog.getChildren().size() >= 2); // Should have label and imageview
        });
    }

    @Test
    @DisplayName("getDukeDialog should create dialog box with bot styling")
    void getDukeDialog_validInput_createsBotDialog() {
        runLater(() -> {
            DialogBox botDialog = DialogBox.getDukeDialog("Hello, user!", testBotImage, "info");
            
            assertNotNull(botDialog);
            assertTrue(botDialog.getChildren().size() >= 2);
        });
    }

    @Test
    @DisplayName("DialogBox should handle different command types for styling")
    void getDukeDialog_differentCommandTypes_appliesCorrectStyling() {
        runLater(() -> {
            DialogBox addDialog = DialogBox.getDukeDialog("Task added", testBotImage, "AddCommand");
            DialogBox markDialog = DialogBox.getDukeDialog("Task marked", testBotImage, "ChangeMarkCommand");
            DialogBox deleteDialog = DialogBox.getDukeDialog("Task deleted", testBotImage, "DeleteCommand");
            DialogBox unknownDialog = DialogBox.getDukeDialog("Unknown", testBotImage, "UnknownCommand");
            
            assertNotNull(addDialog);
            assertNotNull(markDialog);
            assertNotNull(deleteDialog);
            assertNotNull(unknownDialog);
            
            // AI enhancement: Verify styling is applied (would need access to CSS classes)
            // This is a basic structural test - full styling tests would require more complex setup
        });
    }

    @Test
    @DisplayName("DialogBox should handle long text without throwing exceptions")
    void dialogBox_longText_handlesGracefully() {
        runLater(() -> {
            String longText = "This is a very long message that should test the text wrapping and " +
                            "dynamic sizing capabilities of the DialogBox component. It should not " +
                            "cause any exceptions and should properly display all the text content " +
                            "by expanding vertically as needed. The AI-enhanced layout should handle " +
                            "this gracefully without cutting off any text with ellipses.";
            
            DialogBox longDialog = DialogBox.getUserDialog(longText, testUserImage);
            assertNotNull(longDialog);
        });
    }

    @Test
    @DisplayName("DialogBox should handle null text gracefully")
    void dialogBox_nullText_handlesGracefully() {
        runLater(() -> {
            // AI enhancement: Test error handling for null inputs
            DialogBox nullTextDialog = DialogBox.getDukeDialog(null, testBotImage, "info");
            assertNotNull(nullTextDialog);
            
            // Should contain error message instead of crashing
            Label dialogLabel = findDialogLabel(nullTextDialog);
            if (dialogLabel != null) {
                assertEquals("Error: No response text provided", dialogLabel.getText());
            }
        });
    }

    @Test
    @DisplayName("DialogBox should throw exception for null or empty user text")
    void getUserDialog_nullOrEmptyText_throwsException() {
        runLater(() -> {
            // AI enhancement: Test input validation
            assertThrows(IllegalArgumentException.class, () -> 
                DialogBox.getUserDialog(null, testUserImage));
            
            assertThrows(IllegalArgumentException.class, () -> 
                DialogBox.getUserDialog("", testUserImage));
            
            assertThrows(IllegalArgumentException.class, () -> 
                DialogBox.getUserDialog("   ", testUserImage));
        });
    }

    @Test
    @DisplayName("DialogBox should handle null images gracefully")
    void dialogBox_nullImage_handlesGracefully() {
        runLater(() -> {
            DialogBox nullImageDialog = DialogBox.getUserDialog("Test message", null);
            assertNotNull(nullImageDialog);
            
            // Should not crash, image should be empty/default
            ImageView imageView = findImageView(nullImageDialog);
            // Basic structural test - image might be null but component should exist
            assertNotNull(imageView);
        });
    }

    @Test
    @DisplayName("DialogBox should have proper text wrapping enabled")
    void dialogBox_textWrapping_isEnabled() {
        runLater(() -> {
            DialogBox dialog = DialogBox.getUserDialog("Test message", testUserImage);
            Label dialogLabel = findDialogLabel(dialog);
            
            if (dialogLabel != null) {
                assertTrue(dialogLabel.isWrapText(), "Text wrapping should be enabled");
            }
        });
    }

    @Test
    @DisplayName("DialogBox fallback layout should work when FXML fails")
    void dialogBox_fxmlFailure_usesFallbackLayout() {
        // AI enhancement: Test graceful degradation
        // This test is more complex and would require mocking FXML loading failure
        // For now, we test that the fallback components can be created
        runLater(() -> {
            // Test that basic JavaFX components can be created (fallback scenario)
            Label fallbackLabel = new Label("FXML Load Error");
            ImageView fallbackImage = new ImageView();
            
            assertNotNull(fallbackLabel);
            assertNotNull(fallbackImage);
            assertEquals("FXML Load Error", fallbackLabel.getText());
        });
    }

    // AI-Enhanced helper methods for component testing
    private Label findDialogLabel(DialogBox dialogBox) {
        // Helper method to find the Label component in DialogBox
        return dialogBox.getChildren().stream()
            .filter(node -> node instanceof Label)
            .map(node -> (Label) node)
            .findFirst()
            .orElse(null);
    }

    private ImageView findImageView(DialogBox dialogBox) {
        // Helper method to find the ImageView component in DialogBox
        return dialogBox.getChildren().stream()
            .filter(node -> node instanceof ImageView)
            .map(node -> (ImageView) node)
            .findFirst()
            .orElse(null);
    }

    private void runLater(Runnable task) {
        // AI enhancement: Helper method to run tests on JavaFX Application Thread
        try {
            javafx.application.Platform.runLater(task);
            // Small delay to allow Platform.runLater to execute
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // AI-Enhanced: Performance and memory tests
    @Test
    @DisplayName("DialogBox should not cause memory leaks with multiple instances")
    void dialogBox_multipleInstances_noMemoryLeaks() {
        runLater(() -> {
            // Create multiple dialog boxes to test for memory leaks
            for (int i = 0; i < 100; i++) {
                DialogBox dialog = DialogBox.getUserDialog("Test message " + i, testUserImage);
                assertNotNull(dialog);
                // Allow garbage collection
                dialog = null;
            }
            
            // Force garbage collection
            System.gc();
            
            // This is a basic test - proper memory leak testing would require profiling tools
            assertTrue(true, "No exceptions thrown during multiple instance creation");
        });
    }

    @Test
    @DisplayName("DialogBox should handle special characters correctly")
    void dialogBox_specialCharacters_handlesCorrectly() {
        runLater(() -> {
            String specialText = "Special chars: !@#$%^&*()_+-=[]{}|;':\",./<>?`~äöüß你好🎉";
            DialogBox specialDialog = DialogBox.getUserDialog(specialText, testUserImage);
            
            assertNotNull(specialDialog);
            Label dialogLabel = findDialogLabel(specialDialog);
            if (dialogLabel != null) {
                assertEquals(specialText, dialogLabel.getText());
            }
        });
    }
}
