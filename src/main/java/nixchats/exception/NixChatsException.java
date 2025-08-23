package nixchats.exception;

public class NixChatsException extends Exception {
    public NixChatsException(String message) {
        super(message);
    }
    public NixChatsException(String message, Throwable cause) {
        super(message, cause);
    }
}