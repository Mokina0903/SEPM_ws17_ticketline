package at.ac.tuwien.inso.sepm.ticketline.server.exception;

public class InvalidIdException extends Exception {

    public InvalidIdException() {
    }

    public InvalidIdException(String message) {
        super(message);
    }
}
