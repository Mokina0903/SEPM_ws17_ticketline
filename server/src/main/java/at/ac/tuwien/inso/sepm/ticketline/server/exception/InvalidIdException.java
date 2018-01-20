package at.ac.tuwien.inso.sepm.ticketline.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// TODO (Verena) Welchen HTTP Status?
@ResponseStatus(HttpStatus.NOT_FOUND)
public class InvalidIdException extends RuntimeException {

    public InvalidIdException() {
    }

    public InvalidIdException(String message) {
        super("CustomerNotValidException: " + message);
    }
}
