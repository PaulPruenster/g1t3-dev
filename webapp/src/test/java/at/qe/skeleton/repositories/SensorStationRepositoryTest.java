package at.qe.skeleton.repositories;


import at.qe.skeleton.model.SensorStation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.jupiter.api.Assertions.*;

/**
@SpringBootTest
@WebAppConfiguration
@Sql("classpath:sql/SensorStationRepositoryTest.sql")
@Sql("classpath:sql/SensorRepositoryTest.sql")
class SensorStationRepositoryTest {

    @Autowired
    private SensorStationRepository sensorStationRepository;

    private static SensorStation sensorStation;


    @BeforeAll
    static void init(){
        sensorStation = new SensorStation();
        sensorStation.setSensorStationId(1l);
        sensorStation.setName("SensorStation1");
    }

    @Test
    @DirtiesContext
    void testSave(){
        SensorStation testSave_sensorStation = new SensorStation();
        testSave_sensorStation.setName("SensorStation2");
        testSave_sensorStation.setSensorStationId(100l);

        sensorStationRepository.save(testSave_sensorStation);
        assertEquals(testSave_sensorStation, sensorStationRepository.findById(testSave_sensorStation.getSensorStationId()).get());
    }

    @Test
    @DirtiesContext
    void testFindById(){
        assertTrue(sensorStationRepository.findById(sensorStation.getSensorStationId()).isPresent());
        assertEquals(sensorStation, sensorStationRepository.findById(sensorStation.getSensorStationId()).get());
    }

    @Test
    @DirtiesContext
    void testFindAll(){
        assertEquals(2, sensorStationRepository.findAll().size());
    }

    @Test
    @DirtiesContext
    void testDelete(){
        sensorStationRepository.delete(sensorStation);
        assertNotNull(sensorStationRepository.findById(sensorStation.getSensorStationId()));
    }

    @Test
    @DirtiesContext
    void testFindByName(){
        assertEquals(sensorStation, sensorStationRepository.findByName(sensorStation.getName()).get(0));
    }

    @Test
    @DirtiesContext
    void testFindByNameContaining(){
        assertEquals(sensorStation, sensorStationRepository.findByNameContaining(sensorStation.getName()).get(0));
    }

    @Test
    @DirtiesContext
    void testFindByNameContainingIgnoreCase(){
        assertEquals(sensorStation, sensorStationRepository.findByNameContainingIgnoreCase(sensorStation.getName()).get(0));
    }

}
*/