package at.ac.tuwien.inso.sepm.ticketline.server.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class AlreadyExistsException extends RuntimeException {
    public AlreadyExistsException(String message) {
        super("AlreadyExistsException: " + message);
    }

    public AlreadyExistsException() {

    }
}
