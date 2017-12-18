package at.ac.tuwien.inso.sepm.ticketline.client.exception;

public class SearchNoMatchException extends Exception{

    public SearchNoMatchException() {
    }

    public SearchNoMatchException(String message) {
        super(message);
    }

    public SearchNoMatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public SearchNoMatchException(Throwable cause) {
        super(cause);
    }
}
