package at.ac.tuwien.inso.sepm.ticketline.client.exception;

public class BlockedUserException extends Exception {

    public BlockedUserException() {
    }

    public BlockedUserException(String message) {
        super(message);
    }

    public BlockedUserException(String message, Throwable cause) {
        super(message, cause);
    }
}
