package at.qe.skeleton.api.exceptions;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.ResponseStatus;

class SensorStationNotFoundExceptionTest {

    @Test
    void testNoArgConstructor() {
        SensorStationNotFoundException exception = new SensorStationNotFoundException();
        assertNull(exception.getMessage());
    }

    @Test
    void testMessageConstructor() {
        String message = "This is a test message";
        SensorStationNotFoundException exception = new SensorStationNotFoundException(message);
        assertEquals(message, exception.getMessage());
    }

    @Test
    void testIdConstructor() {
        Long id = 123L;
        SensorStationNotFoundException exception = new SensorStationNotFoundException(id);
        assertEquals("SensorStation with id " + id + " not found", exception.getMessage());
    }

    @Test
    void testResponseStatus() {
        SensorStationNotFoundException exception = new SensorStationNotFoundException();
        ResponseStatus statusAnnotation = exception.getClass().getAnnotation(ResponseStatus.class);
        assertNotNull(statusAnnotation);
    }
}