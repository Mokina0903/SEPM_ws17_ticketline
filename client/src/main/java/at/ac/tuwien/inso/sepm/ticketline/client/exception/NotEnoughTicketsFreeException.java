package at.ac.tuwien.inso.sepm.ticketline.client.exception;

public class NotEnoughTicketsFreeException extends Exception {
    public NotEnoughTicketsFreeException() {
    }

    public NotEnoughTicketsFreeException(String message) {
        super("NotEnoughTicketsFreeException: "+message);
    }
}
