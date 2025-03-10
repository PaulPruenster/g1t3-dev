package at.qe.skeleton.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SensorTypTest {

    @Test
    void testValues() {
        // Ensure that the values of the enum are correct
        assertEquals(SensorTyp.LUFTSENSOR, SensorTyp.valueOf("LUFTSENSOR"));
        assertEquals(SensorTyp.FOTOTRANSISTOR, SensorTyp.valueOf("FOTOTRANSISTOR"));
        assertEquals(SensorTyp.TEMPERATURE, SensorTyp.valueOf("TEMPERATURE"));
        assertEquals(SensorTyp.HUMIDITY, SensorTyp.valueOf("HUMIDITY"));
        assertEquals(SensorTyp.PRESSURE, SensorTyp.valueOf("PRESSURE"));
        assertEquals(SensorTyp.AIR_QUALITY, SensorTyp.valueOf("AIR_QUALITY"));
        assertEquals(SensorTyp.ALTITUDE, SensorTyp.valueOf("ALTITUDE"));
        assertEquals(SensorTyp.LIGHT, SensorTyp.valueOf("LIGHT"));
        assertEquals(SensorTyp.HYGROMETER, SensorTyp.valueOf("HYGROMETER"));

        // Ensure that the values are returned to the correct order
        SensorTyp[] roles = SensorTyp.values();
        assertEquals(SensorTyp.LUFTSENSOR, roles[0]);
        assertEquals(SensorTyp.FOTOTRANSISTOR, roles[1]);
        assertEquals(SensorTyp.TEMPERATURE, roles[2]);
        assertEquals(SensorTyp.HUMIDITY, roles[3]);
        assertEquals(SensorTyp.PRESSURE, roles[4]);
        assertEquals(SensorTyp.AIR_QUALITY, roles[5]);
        assertEquals(SensorTyp.ALTITUDE, roles[6]);
        assertEquals(SensorTyp.LIGHT, roles[7]);
        assertEquals(SensorTyp.HYGROMETER, roles[8]);
    }

}
