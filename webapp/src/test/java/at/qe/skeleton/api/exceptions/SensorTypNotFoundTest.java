package at.qe.skeleton.api.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SensorTypNotFoundTest {

    @Test
    void testSensorTypNotFound() {
        String expectedMessage = "The sensorTyp does not exist";

        SensorTypNotFound exception = assertThrows(SensorTypNotFound.class, () -> {
            throw new SensorTypNotFound(expectedMessage);
        });

        assertEquals(expectedMessage, exception.getMessage());
    }
}
