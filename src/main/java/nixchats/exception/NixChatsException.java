package nixchats.exception;

/**
 * Represents an error that occurs in NixChats.
 */
public class NixChatsException extends Exception {
    public NixChatsException(String message) {
        super(message);
    }
    public NixChatsException(String message, Throwable cause) {
        super(message, cause);
    }
}
