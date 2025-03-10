package at.qe.skeleton.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "The access point with the given id does not exist")
public class AccessPointNotFoundException extends Exception{
    public AccessPointNotFoundException() {
    }
    public AccessPointNotFoundException(String message) {
        super(message);
    }
    public AccessPointNotFoundException(Long id) {
        super("AccessPoint with id " + id + " not found");
    }
}
