package at.ac.tuwien.inso.sepm.ticketline.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// TODO (Verena) Welchen HTTP Status?
@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class CustomerNotValidException extends RuntimeException {

    public CustomerNotValidException() {
    }

    public CustomerNotValidException(String message) {
        super("CustomerNotValidException: " + message);
    }
}
