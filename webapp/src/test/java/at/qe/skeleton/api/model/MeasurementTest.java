package at.qe.skeleton.api.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MeasurementTest {

    private Measurement measurement;

    @BeforeEach
    public void setUp() {
        measurement = new Measurement();
    }

    @Test
    void testGetAndSetId() {
        Long id = 123L;
        assertNull(measurement.getId());
        measurement.setId(id);
        assertEquals(id, measurement.getId());
    }

    @Test
    void testGetAndSetPlantID() {
        Integer plantID = 456;
        assertNull(measurement.getPlantID());
        measurement.setPlantID(plantID);
        assertEquals(plantID, measurement.getPlantID());
    }

    @Test
    void testGetAndSetValue() {
        Double value = 1.23;
        assertNull(measurement.getValue());
        measurement.setValue(value);
        assertEquals(value, measurement.getValue());
    }

    @Test
    void testGetAndSetUnit() {
        String unit = "kg";
        assertNull(measurement.getUnit());
        measurement.setUnit(unit);
        assertEquals(unit, measurement.getUnit());
    }

    @Test
    void testGetAndSetType() {
        String type = "height";
        assertNull(measurement.getType());
        measurement.setType(type);
        assertEquals(type, measurement.getType());
    }
}

