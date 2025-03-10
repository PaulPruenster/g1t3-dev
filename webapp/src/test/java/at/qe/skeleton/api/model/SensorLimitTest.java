package at.qe.skeleton.api.model;

import at.qe.skeleton.model.Sensor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SensorLimitTest {

    @Test
    void testConstructorSensorLimit() {
        Sensor sensor = new Sensor();
        sensor.setLowerLimit(1.0);
        sensor.setUpperLimit(2.0);
        SensorLimit sensorLimit = new SensorLimit(sensor);
        assertEquals(1.0, sensorLimit.getLowerLimit());
        assertEquals(2.0, sensorLimit.getUpperLimit());
    }
}
