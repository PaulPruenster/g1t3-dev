package at.qe.skeleton.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "SensorStation with the given id does not exist")
public class SensorStationNotFoundException extends Exception {

    public SensorStationNotFoundException() {

    }

    public SensorStationNotFoundException(String message) {
        super(message);
    }

    public SensorStationNotFoundException(Long id) {
        super("SensorStation with id " + id + " not found");
    }
}
