package at.ac.tuwien.inso.sepm.ticketline.server.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FAILED_DEPENDENCY)
public class OldVersionException extends RuntimeException {
    public OldVersionException(String message) {
        super(message);
    }

    public OldVersionException() {

    }


}