package at.ac.tuwien.inso.sepm.ticketline.client.exception;

public class TicketAlreadyExistsException extends Exception{
    public TicketAlreadyExistsException() {
    }

    public TicketAlreadyExistsException( String message ) {
        super(message);
    }

    public TicketAlreadyExistsException( String message, Throwable cause ) {
        super(message, cause);
    }

    public TicketAlreadyExistsException( Throwable cause ) {
        super(cause);
    }
}
