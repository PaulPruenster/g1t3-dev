package at.qe.skeleton.repositories;


import at.qe.skeleton.model.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@WebAppConfiguration
@Sql("classpath:sql/SensorRepositoryTest.sql")
class SensorRepositoryTest {

    @Autowired
    private SensorRepository sensorRepository;

    private static Sensor sensor;

    /**
     * Initializes the {@link Sensor} object.
     */
    @BeforeAll
    static void init(){
        sensor = new Sensor();
        sensor.setSensorId(1L);
        sensor.setName("Sensor1");
        sensor.setSensorStation(null);
        sensor.setSensorTyp(SensorTyp.LUFTSENSOR);
        sensor.setSensorUnit(SensorUnit.CELSIUS);
        sensor.setLowerLimit(0);
        sensor.setUpperLimit(100);
    }

    /**
     * Test for the method {@link SensorRepository#save(Sensor)}.
     */
    @Test
    @DirtiesContext
    void testSave(){
        Sensor testSave_sensor = new Sensor();
        testSave_sensor.setName("Sensor2");
        testSave_sensor.setSensorId(2L);
        testSave_sensor.setSensorStation(null);
        testSave_sensor.setSensorTyp(SensorTyp.LUFTSENSOR);
        testSave_sensor.setSensorUnit(SensorUnit.CELSIUS);
        testSave_sensor.setLowerLimit(0);
        testSave_sensor.setUpperLimit(100);

        assertTrue(sensorRepository.findById(testSave_sensor.getSensorId()).isPresent());

        Sensor saved = sensorRepository.save(testSave_sensor);
        assertEquals(testSave_sensor, saved);
        assertNotNull(saved);
    }

    /**
     * Test for the method {@link SensorRepository#findById(Long)}.
     */
    @Test
    @DirtiesContext
    void testFindById(){
        assertTrue(sensorRepository.findById(sensor.getSensorId()).isPresent());
        assertEquals(sensor, sensorRepository.findById(sensor.getSensorId()).get());
    }

    /**
     * Test for the method {@link SensorRepository#findAll()}.
     */
    @Test
    @DirtiesContext
    void testFindAll(){
        assertTrue(sensorRepository.findAll().size() > 0);
    }

    /**
     * Test for the method {@link SensorRepository#existsById(Long)}.
     */
    @Test
    @DirtiesContext
    void testDelete(){
        sensorRepository.delete(sensor);
        assertFalse(sensorRepository.findById(sensor.getSensorId()).isPresent());
    }

    /**
     * Test for the method {@link SensorRepository#deleteById(Long)}.
     */
    @Test
    @DirtiesContext
    void testDeleteById(){
        sensorRepository.deleteById(sensor.getSensorId());
        assertFalse(sensorRepository.findById(sensor.getSensorId()).isPresent());
    }

    /**
     * Test for the method {@link SensorRepository#deleteAll()}.
     */
    @Test
    @DirtiesContext
    void testDeleteAll(){
        sensorRepository.deleteAll();
        assertTrue(sensorRepository.findAll().isEmpty());
    }

}
