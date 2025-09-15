package nixchats;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private NixChats nixchats;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/DaUser.png"));
    private Image dukeImage = new Image(this.getClass().getResourceAsStream("/images/DaDuke.png"));

    @FXML
    public void initialize() {
        // AI-Enhanced Fix: Claude identified the ScrollPane binding conflict
        // Cannot bind vvalue and also manually set it - causes JavaFX runtime error
        
        // AI suggestion: Use layout listener for auto-scrolling instead of binding
        dialogContainer.heightProperty().addListener((observable, oldValue, newValue) -> {
            // Auto-scroll to bottom when new content is added
            javafx.application.Platform.runLater(() -> scrollPane.setVvalue(1.0));
        });
        
        // AI enhancement: Ensure proper sizing behavior for dynamic content
        scrollPane.setFitToWidth(true);
        dialogContainer.setFillWidth(true);
    }

    /** Injects the NixChats instance */
    public void setNixChats(NixChats n) {
        nixchats = n;
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Duke's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     * AI-Enhanced: Added proper layout updates for dynamic content sizing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = nixchats.getResponse(input);
        String commandType = nixchats.getCommandType();
        
        // AI suggestion: Create dialog boxes and add them individually to ensure proper layout
        DialogBox userDialog = DialogBox.getUserDialog(input, userImage);
        DialogBox dukeDialog = DialogBox.getDukeDialog(response, dukeImage, commandType);
        
        dialogContainer.getChildren().addAll(userDialog, dukeDialog);
        
        // AI enhancement: Force layout update to ensure proper sizing
        dialogContainer.applyCss();
        dialogContainer.layout();
        
        userInput.clear();
    }
}
