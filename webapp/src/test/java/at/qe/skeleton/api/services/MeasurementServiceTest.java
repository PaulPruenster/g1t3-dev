package at.qe.skeleton.api.services;

import at.qe.skeleton.api.exceptions.MeasurementNotFoundException;
import at.qe.skeleton.api.model.Measurement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MeasurementServiceTest {

    private MeasurementService measurementService;

    @BeforeEach
    public void setup() {
        measurementService = new MeasurementService();
    }

    @Test
    void addMeasurementTest() {
        Measurement measurement = new Measurement();
        measurement.setPlantID(1);
        measurement.setType("type");
        measurement.setUnit("unit");
        measurement.setValue(1.0);

        Measurement addedMeasurement = measurementService.addMeasurement(measurement);

        Assertions.assertNotNull(addedMeasurement.getId());
        Assertions.assertEquals(measurement.getPlantID(), addedMeasurement.getPlantID());
        Assertions.assertEquals(measurement.getType(), addedMeasurement.getType());
        Assertions.assertEquals(measurement.getUnit(), addedMeasurement.getUnit());
        Assertions.assertEquals(measurement.getValue(), addedMeasurement.getValue());
    }

    @Test
    void findOneMeasurementTest() throws MeasurementNotFoundException {
        Measurement measurement = new Measurement();
        measurement.setPlantID(1);
        measurement.setType("type");
        measurement.setUnit("unit");
        measurement.setValue(1.0);

        Measurement addedMeasurement = measurementService.addMeasurement(measurement);

        Measurement foundMeasurement = measurementService.findOneMeasurement(addedMeasurement.getId());

        Assertions.assertEquals(addedMeasurement.getId(), foundMeasurement.getId());
        Assertions.assertEquals(addedMeasurement.getPlantID(), foundMeasurement.getPlantID());
        Assertions.assertEquals(addedMeasurement.getType(), foundMeasurement.getType());
        Assertions.assertEquals(addedMeasurement.getUnit(), foundMeasurement.getUnit());
        Assertions.assertEquals(addedMeasurement.getValue(), foundMeasurement.getValue());
    }

    @Test
    void findOneMeasurementNotFoundTest() {
        Assertions.assertThrows(MeasurementNotFoundException.class,
                () -> measurementService.findOneMeasurement(999L));
    }

    @Test
    void updateMeasurementTest() {
        Measurement measurement = new Measurement();
        measurement.setPlantID(1);
        measurement.setType("type");
        measurement.setUnit("unit");
        measurement.setValue(1.0);

        Measurement addedMeasurement = measurementService.addMeasurement(measurement);

        Measurement updatedMeasurement = new Measurement();
        updatedMeasurement.setPlantID(2);
        updatedMeasurement.setType("new type");
        updatedMeasurement.setUnit("new unit");
        updatedMeasurement.setValue(2.0);

        Measurement resultMeasurement = measurementService.updateMeasurement(addedMeasurement.getId(), updatedMeasurement);

        Assertions.assertEquals(addedMeasurement.getId(), resultMeasurement.getId());
        Assertions.assertEquals(updatedMeasurement.getPlantID(), resultMeasurement.getPlantID());
        Assertions.assertEquals(updatedMeasurement.getType(), resultMeasurement.getType());
        Assertions.assertEquals(updatedMeasurement.getUnit(), resultMeasurement.getUnit());
        Assertions.assertEquals(updatedMeasurement.getValue(), resultMeasurement.getValue());
    }
}


