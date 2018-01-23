package at.ac.tuwien.inso.sepm.ticketline.client.exception;

public class EmptyValueException extends Exception {
    public EmptyValueException() {
    }

    public EmptyValueException( String message ) {
        super(message);
    }

    public EmptyValueException( String message, Throwable cause ) {
        super(message, cause);
    }

    public EmptyValueException( Throwable cause ) {
        super(cause);
    }
}
