package nixchats.exception;

public class InputException extends NixChatsException {
    public enum Reason {
        UNKNOWN_COMMAND,
        MISSING_ARGUMENT,
        INVALID_ARGUMENT,
        EMPTY_INPUT
    }

    private final Reason reason;

    public InputException(Reason reason, String message) {
        super(message);
        this.reason = reason;
    }

    public Reason getReason() {
        return reason;
    }
}