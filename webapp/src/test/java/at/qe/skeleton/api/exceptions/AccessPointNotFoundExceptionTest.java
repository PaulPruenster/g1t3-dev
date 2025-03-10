package at.qe.skeleton.api.exceptions;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

class AccessPointNotFoundExceptionTest {

    @Test
    void testNoArgConstructor() {
        AccessPointNotFoundException exception = new AccessPointNotFoundException();
        assertNull(exception.getMessage());
    }

    @Test
    void testMessageConstructor() {
        String message = "This is a test message";
        AccessPointNotFoundException exception = new AccessPointNotFoundException(message);
        assertEquals(message, exception.getMessage());
    }

    @Test
    void testIdConstructor() {
        Long id = 123L;
        AccessPointNotFoundException exception = new AccessPointNotFoundException(id);
        assertEquals("AccessPoint with id " + id + " not found", exception.getMessage());
    }

    @Test
    void testResponseStatus() {
        AccessPointNotFoundException exception = new AccessPointNotFoundException();
        ResponseStatus statusAnnotation = exception.getClass().getAnnotation(ResponseStatus.class);
        assertNotNull(statusAnnotation);
        assertEquals(HttpStatus.NOT_FOUND, statusAnnotation.code());
        assertEquals("The access point with the given id does not exist", statusAnnotation.reason());
    }
}
