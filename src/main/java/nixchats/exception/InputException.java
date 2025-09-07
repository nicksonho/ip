package nixchats.exception;

/**
 * Represents an error that occurs during user input.
 */
public class InputException extends NixChatsException {

    /**
     * Represents the reason for the exception.
     */
    public enum Reason {
        UNKNOWN_COMMAND,
        MISSING_ARGUMENT,
        INVALID_ARGUMENT,
        EMPTY_INPUT
    }

    private final Reason reason;

    /**
     * Constructs an InputException with the given reason and message.
     * @param reason the reason for the exception
     * @param message the message to be displayed to the user
     */
    public InputException(Reason reason, String message) {
        super(message);
        this.reason = reason;
    }

    public Reason getReason() {
        return reason;
    }
}
