package at.qe.skeleton.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "The sensorTyp does not exist")
public class SensorTypNotFound extends Exception{

    public SensorTypNotFound() {

    }
    public SensorTypNotFound(String message) {
        super(message);
    }
}
