package at.qe.skeleton.api.exceptions;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

class SensorNotFoundExceptionTest {

    @Test
    void testNoArgConstructor() {
        SensorNotFoundException exception = new SensorNotFoundException();
        assertNull(exception.getMessage());
    }

    @Test
    void testMessageConstructor() {
        String message = "This is a test message";
        SensorNotFoundException exception = new SensorNotFoundException(message);
        assertEquals(message, exception.getMessage());
    }

    @Test
    void testIdConstructor() {
        Long id = 123L;
        SensorNotFoundException exception = new SensorNotFoundException(id);
        assertEquals("Sensor with id " + id + " not found", exception.getMessage());
    }

    @Test
    void testResponseStatus() {
        SensorNotFoundException exception = new SensorNotFoundException();
        ResponseStatus statusAnnotation = exception.getClass().getAnnotation(ResponseStatus.class);
        assertNotNull(statusAnnotation);
        assertEquals(HttpStatus.NOT_FOUND, statusAnnotation.code());
        assertEquals("The sensor with the given id does not exist", statusAnnotation.reason());
    }
}

