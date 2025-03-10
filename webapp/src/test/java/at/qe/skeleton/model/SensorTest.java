package at.qe.skeleton.model;

import at.qe.skeleton.model.Sensor;
import at.qe.skeleton.model.SensorUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class SensorTest {

    @Test
    void testEquals() {
        Sensor sensor1 = new Sensor();
        sensor1.setSensorId(1L);
        sensor1.setName("Sensor 1");
        sensor1.setSensorTyp(SensorTyp.HYGROMETER);
        sensor1.setSensorUnit(SensorUnit.CELSIUS);
        sensor1.setUpperLimit(100);
        sensor1.setLowerLimit(0);
        sensor1.setCurrentThresholdWarning(false);

        Sensor sensor2 = new Sensor();
        sensor2.setSensorId(1L);
        sensor2.setName("Sensor 1");
        sensor2.setSensorTyp(SensorTyp.HYGROMETER);
        sensor2.setSensorUnit(SensorUnit.CELSIUS);
        sensor2.setUpperLimit(100);
        sensor2.setLowerLimit(0);
        sensor2.setCurrentThresholdWarning(false);

        Sensor sensor3 = new Sensor();
        sensor3.setSensorId(2L);
        sensor3.setName("Sensor 2");
        sensor3.setSensorTyp(SensorTyp.LUFTSENSOR);
        sensor3.setSensorUnit(SensorUnit.PERCENT);
        sensor3.setUpperLimit(90);
        sensor3.setLowerLimit(10);
        sensor3.setCurrentThresholdWarning(true);

        assertEquals(sensor1, sensor2); // sensors with same properties should be equal
        assertNotEquals(sensor1, sensor3); // sensors with different properties should not be equal
    }

    @Test
    void testHashCode() {
        Sensor sensor1 = new Sensor();
        sensor1.setSensorId(1L);
        sensor1.setName("Sensor 1");
        sensor1.setSensorTyp(SensorTyp.HYGROMETER);
        sensor1.setSensorUnit(SensorUnit.CELSIUS);
        sensor1.setUpperLimit(100);
        sensor1.setLowerLimit(0);
        sensor1.setCurrentThresholdWarning(false);

        Sensor sensor2 = new Sensor();
        sensor2.setSensorId(1L);
        sensor2.setName("Sensor 1");
        sensor2.setSensorTyp(SensorTyp.HYGROMETER);
        sensor2.setSensorUnit(SensorUnit.CELSIUS);
        sensor2.setUpperLimit(100);
        sensor2.setLowerLimit(0);
        sensor2.setCurrentThresholdWarning(false);

        assertEquals(sensor1.hashCode(), sensor2.hashCode()); // sensors with same properties should have same hash code
    }

    @Test
    void testCompareTo() {
        Sensor sensor1 = new Sensor();
        sensor1.setSensorId(1L);

        Sensor sensor2 = new Sensor();
        sensor2.setSensorId(2L);

        assertEquals(-1, sensor1.compareTo(sensor2)); // sensors with lower sensorId should come first in order
        assertEquals(1, sensor2.compareTo(sensor1)); // sensors with higher sensorId should come later in order
        assertEquals(0, sensor1.compareTo(sensor1)); // a sensor should be equal to itself
    }

    @Test
    void testIsNew() {
        Sensor sensor1 = new Sensor();
        Assertions.assertTrue(sensor1.isNew()); // a new sensor should be marked as new

        Sensor sensor2 = new Sensor();
        sensor2.setSensorId(1L);
        Assertions.assertFalse(sensor2.isNew()); // a sensor with an ID should not be marked as new
    }

}
