package at.qe.skeleton.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MeasurementLabelTest {

    @Test
    void testValues() {
        // Ensure that the values of the enum are correct
        assertEquals(MeasurementLabel.NORMAL, MeasurementLabel.valueOf("NORMAL"));
        assertEquals(MeasurementLabel.LOWER_THRESHOLD_EXCEEDED, MeasurementLabel.valueOf("LOWER_THRESHOLD_EXCEEDED"));
        assertEquals(MeasurementLabel.UPPER_THRESHOLD_EXCEEDED, MeasurementLabel.valueOf("UPPER_THRESHOLD_EXCEEDED"));

        // Ensure that the values are returned to the correct order
        MeasurementLabel[] roles = MeasurementLabel.values();
        assertEquals(MeasurementLabel.UPPER_THRESHOLD_EXCEEDED, roles[0]);
        assertEquals(MeasurementLabel.LOWER_THRESHOLD_EXCEEDED, roles[1]);
        assertEquals(MeasurementLabel.NORMAL, roles[2]);
    }
}

