package nixchats;

import java.io.IOException;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * Represents a dialog box consisting of an ImageView to represent the speaker's face
 * and a label containing text from the speaker.
 *
 * AI-Enhanced Documentation: Claude suggested additional details for better clarity.
 * This component automatically handles text wrapping and dynamic sizing to prevent
 * text overflow issues. Supports different visual styles for different command types.
 *
 * @author NixChats Team (with AI assistance from Claude and GitHub Copilot)
 * @version 2.0 (AI-Enhanced)
 */
public class DialogBox extends HBox {
    @FXML
    private Label dialog;
    @FXML
    private ImageView displayPicture;

    private DialogBox(String text, Image img) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            // AI-Enhanced Error Handling: GitHub Copilot suggested more informative error logging
            System.err.println("Failed to load DialogBox FXML: " + e.getMessage());
            e.printStackTrace();
            // AI suggestion: Provide fallback behavior instead of silent failure
            createFallbackLayout();
        }

        // AI-Assisted Enhancement: Claude suggested these properties to fix text overflow
        // Previously, long text was being truncated with ellipses
        dialog.setText(text != null ? text : ""); // AI suggestion: Null safety
        dialog.setWrapText(true); // Enable text wrapping for long messages
        dialog.setTextOverrun(javafx.scene.control.OverrunStyle.CLIP); // Remove ellipsis truncation
        // AI suggestion: Use computed size to allow dynamic height based on content
        dialog.setPrefHeight(javafx.scene.control.Control.USE_COMPUTED_SIZE);
        dialog.setMaxHeight(javafx.scene.control.Control.USE_PREF_SIZE);

        if (img != null) { // AI suggestion: Null check for image
            displayPicture.setImage(img);
        }
        
        // AI-Enhanced Fix: Ensure HBox itself can grow vertically with content
        this.setPrefHeight(javafx.scene.control.Control.USE_COMPUTED_SIZE);
        this.setMaxHeight(javafx.scene.control.Control.USE_PREF_SIZE);
        this.setFillHeight(true);
    }

    /**
     * AI-Assisted Method: Creates a fallback layout when FXML loading fails.
     * GitHub Copilot suggested this graceful degradation approach.
     */
    private void createFallbackLayout() {
        dialog = new Label("FXML Load Error");
        displayPicture = new ImageView();
        this.getChildren().addAll(dialog, displayPicture);
        this.setSpacing(10);
        this.setAlignment(Pos.CENTER_LEFT);
    }

    /**
     * Flips the dialog box such that the ImageView is on the left and text on the right.
     */
    private void flip() {
        ObservableList<Node> tmp = FXCollections.observableArrayList(this.getChildren());
        Collections.reverse(tmp);
        getChildren().setAll(tmp);
        setAlignment(Pos.TOP_LEFT);
        dialog.getStyleClass().add("reply-label");
    }

    /**
     * Creates a dialog box for user messages.
     * AI-Enhanced: Added input validation suggested by GitHub Copilot.
     * 
     * @param text The message text to display
     * @param img The user's avatar image
     * @return A configured DialogBox for user messages
     * @throws IllegalArgumentException if text is null or empty
     */
    public static DialogBox getUserDialog(String text, Image img) {
        // AI suggestion: Input validation for better robustness
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Dialog text cannot be null or empty");
        }
        return new DialogBox(text, img);
    }

    /**
     * AI-Enhanced Method: Applies visual styling based on command type.
     * Claude suggested using enums for better type safety, but keeping string
     * compatibility for existing code.
     *
     * @param commandType The type of command that generated this dialog
     */
    private void changeDialogStyle(String commandType) {
        // AI suggestion: More robust switch with validation
        if (commandType == null) {
            return; // Graceful handling of null input
        }

        switch(commandType) {
        case "AddCommand":
            dialog.getStyleClass().add("add-label");
            break;
        case "ChangeMarkCommand":
            dialog.getStyleClass().add("marked-label");
            break;
        case "DeleteCommand":
            dialog.getStyleClass().add("delete-label");
            break;
        default:
            // AI suggestion: Log unknown command types for debugging
            System.out.println("Unknown command type for styling: " + commandType);
        }
    }

    /**
     * Creates a dialog box for chatbot responses.
     * AI-Enhanced: Improved documentation and error handling.
     *
     * @param text The response text to display
     * @param img The chatbot's avatar image
     * @param commandType The type of command for visual styling
     * @return A configured DialogBox for chatbot responses
     */
    public static DialogBox getDukeDialog(String text, Image img, String commandType) {
        // AI suggestion: Validate inputs before processing
        if (text == null) {
            text = "Error: No response text provided";
        }

        var db = new DialogBox(text, img);
        db.flip();
        db.changeDialogStyle(commandType);
        return db;
    }
}
