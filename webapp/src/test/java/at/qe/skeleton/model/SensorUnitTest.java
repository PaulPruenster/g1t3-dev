package at.qe.skeleton.model;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SensorUnitTest {


    @Test
    void testValues() {
        // Ensure that the values of the enum are correct
        assertEquals(SensorUnit.CELSIUS, SensorUnit.valueOf("CELSIUS"));
        assertEquals(SensorUnit.PERCENT, SensorUnit.valueOf("PERCENT"));
        assertEquals(SensorUnit.LITER, SensorUnit.valueOf("LITER"));
        assertEquals(SensorUnit.MM_PER_CUBIC_CENTIMETER, SensorUnit.valueOf("MM_PER_CUBIC_CENTIMETER"));
        assertEquals(SensorUnit.PASCAL, SensorUnit.valueOf("PASCAL"));
        assertEquals(SensorUnit.METER, SensorUnit.valueOf("METER"));
        assertEquals(SensorUnit.LUX, SensorUnit.valueOf("LUX"));
        assertEquals(SensorUnit.GRAMS_PER_CUBIC_CENTIMETER, SensorUnit.valueOf("GRAMS_PER_CUBIC_CENTIMETER"));

        // Ensure that the values are returned to the correct order
        SensorUnit[] roles = SensorUnit.values();
        assertEquals(SensorUnit.CELSIUS, roles[0]);
        assertEquals(SensorUnit.PERCENT, roles[1]);
        assertEquals(SensorUnit.LITER, roles[2]);
        assertEquals(SensorUnit.MM_PER_CUBIC_CENTIMETER, roles[3]);
        assertEquals(SensorUnit.PASCAL, roles[4]);
        assertEquals(SensorUnit.METER, roles[5]);
        assertEquals(SensorUnit.LUX, roles[6]);
        assertEquals(SensorUnit.GRAMS_PER_CUBIC_CENTIMETER, roles[7]);

    }
}
