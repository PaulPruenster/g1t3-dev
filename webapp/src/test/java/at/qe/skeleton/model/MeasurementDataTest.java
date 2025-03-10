package at.qe.skeleton.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MeasurementDataTest {



    @Test
    void testCompareTo() {
        MeasurementData measurement1 = new MeasurementData();
        measurement1.setMeasurementId(1L);
        MeasurementData measurement2 = new MeasurementData();
        measurement2.setMeasurementId(2L);

        assertEquals(-1, measurement1.compareTo(measurement2));
        assertEquals(0, measurement1.compareTo(measurement1));
        assertEquals(1, measurement2.compareTo(measurement1));

    }

    @Test
    void testEquals(){
        Sensor sensor1 = new Sensor();
        sensor1.setSensorId(1L);

        Sensor sensor2  = new Sensor();
        sensor2.setSensorId(2L);

        MeasurementData measurement1 = new MeasurementData();
        measurement1.setMeasurementId(1L);
        measurement1.setMeasurementLabel(MeasurementLabel.NORMAL);
        measurement1.setSensor(sensor1);
        measurement1.setVal(1);

        // same as measurement1
        MeasurementData measurement2 = new MeasurementData();
        measurement2.setMeasurementId(1L);
        measurement2.setMeasurementLabel(MeasurementLabel.NORMAL);
        measurement2.setSensor(sensor1);
        measurement2.setVal(1);

        // different MeasurementId
        MeasurementData measurement3 = new MeasurementData();
        measurement3.setMeasurementId(2L);
        measurement3.setMeasurementLabel(MeasurementLabel.NORMAL);
        measurement3.setSensor(sensor1);
        measurement3.setVal(1);

        // different MeasurementLabel
        MeasurementData measurement4 = new MeasurementData();
        measurement4.setMeasurementId(1l);
        measurement4.setMeasurementLabel(MeasurementLabel.LOWER_THRESHOLD_EXCEEDED);
        measurement4.setSensor(sensor1);
        measurement4.setVal(1);

        // different Sensor
        MeasurementData measurement5 = new MeasurementData();
        measurement5.setMeasurementId(1L);
        measurement5.setMeasurementLabel(MeasurementLabel.NORMAL);
        measurement5.setSensor(sensor2);
        measurement5.setVal(1);

        // different Value
        MeasurementData measurement6 = new MeasurementData();
        measurement6.setMeasurementId(1L);
        measurement6.setMeasurementLabel(MeasurementLabel.NORMAL);
        measurement6.setSensor(sensor1);
        measurement6.setVal(2);


        assertEquals(measurement1, measurement1);
        assertEquals(measurement1, measurement2);
        assertNotEquals(measurement1, measurement3);
        assertNotEquals(measurement1, measurement4);
        assertNotEquals(measurement1, measurement5);
        assertNotEquals(measurement1, measurement6);

    }
}
