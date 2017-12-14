package at.ac.tuwien.inso.sepm.ticketline.server.exception;

public class CustomerNotValidException extends Exception {

    public CustomerNotValidException() {
    }

    public CustomerNotValidException(String message) {
        super(message);
    }
}
