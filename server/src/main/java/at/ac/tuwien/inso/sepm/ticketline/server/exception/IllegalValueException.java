package at.ac.tuwien.inso.sepm.ticketline.server.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class IllegalValueException extends RuntimeException {
    public IllegalValueException(String message) {
        super("IllegalValueException: " + message);
    }

    public IllegalValueException() {

    }
}