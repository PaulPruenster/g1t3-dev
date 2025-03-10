package at.qe.skeleton.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "The measurement with the given id does not exist")
public class MeasurementNotFoundException extends Exception{

    public MeasurementNotFoundException() {
        // TODO:
    }
}
