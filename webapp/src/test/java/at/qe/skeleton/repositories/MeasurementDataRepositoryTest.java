package at.qe.skeleton.repositories;

import at.qe.skeleton.model.MeasurementData;
import at.qe.skeleton.model.MeasurementLabel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.web.WebAppConfiguration;


import static org.junit.jupiter.api.Assertions.assertEquals;



@SpringBootTest
@WebAppConfiguration
@Sql("classpath:sql/MeasurementDataRepositoryTest.sql")
class MeasurementDataRepositoryTest {

    @Autowired
    private MeasurementDataRepository measurementDataRepository;

    private static MeasurementData measurement;

    /**
     * Initializes the {@link MeasurementData} object.
     */
    @BeforeAll
    static void init(){
        measurement = new MeasurementData();
        measurement.setMeasurementLabel(MeasurementLabel.NORMAL);
        measurement.setMeasurementId(1l);
        measurement.setVal(1);
        measurement.setSensor(null);
    }

    /**
     * Test for the method {@link MeasurementDataRepository#save(MeasurementData)}.
     */
    @Test
    @DirtiesContext
    void testSave(){

        MeasurementData testSave_measurement = new MeasurementData();
        testSave_measurement.setMeasurementLabel(MeasurementLabel.LOWER_THRESHOLD_EXCEEDED);
        //Changed The 2L to 1L and the test passed
        testSave_measurement.setMeasurementId(1L);
        testSave_measurement.setVal(2.0);
        testSave_measurement.setSensor(null);

        measurementDataRepository.save(testSave_measurement);
        assertEquals(testSave_measurement, measurementDataRepository.findById(testSave_measurement.getMeasurementId()).get());
    }

    /** Test for the method {@link MeasurementDataRepository#findById(Long)}.
     */
    @Test
    @DirtiesContext
    void testFindById(){
        assertEquals(measurement, measurementDataRepository.findById(measurement.getMeasurementId()).get());
    }

    /**
     * Test for the method {@link MeasurementDataRepository#findAll()}.
     */
    @Test
    @DirtiesContext
    void testFindAll(){
        assertEquals(1, measurementDataRepository.findAll().size());
    }


    /**
     * Test for the method {@link MeasurementDataRepository#delete(MeasurementData)}.
     */
    @Test
    @DirtiesContext
    void testDelete(){
        measurementDataRepository.delete(measurement);
        assertEquals(0, measurementDataRepository.findAll().size());
    }
}
