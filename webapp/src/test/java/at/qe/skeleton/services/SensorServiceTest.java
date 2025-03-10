package at.qe.skeleton.services;

import at.qe.skeleton.api.exceptions.SensorNotFoundException;
import at.qe.skeleton.api.exceptions.SensorStationNotFoundException;
import at.qe.skeleton.model.Sensor;
import at.qe.skeleton.repositories.SensorRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.web.WebAppConfiguration;

import java.rmi.NoSuchObjectException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@WebAppConfiguration
@SpringBootTest
public class SensorServiceTest {

    @Mock
    private SensorRepository sensorRepository;

    @InjectMocks
    private SensorService sensorService;

    private static Sensor sensor1;
    private static Sensor sensor2;

    /**
     * This method is called before all tests and initializes the test data
     */
    @BeforeAll
    static void init() {
        sensor1 = new Sensor();
        sensor1.setSensorId(1L);
        sensor1.setName("Sensor1");

        sensor2 = new Sensor();
        sensor2.setSensorId(2L);
        sensor2.setName("Sensor2");
    }

    /**
     * This method is called before each test and adds the test mocking
     */
    void addTestMocking() {
        // Mocking the repository to return the sensor1 and sensor2 when the getAllSensors method is called
        when(sensorRepository.findAll()).thenReturn(List.of(new Sensor[]{sensor1, sensor2}));

        // Mocking the repository to return the sensor1 when the loadSensor method is called
        when(sensorRepository.findFirstBySensorId(sensor1.getSensorId())).thenReturn(sensor1);

        // Mocking the repository to return the sensor2 when the loadSensor method is called
        when(sensorRepository.findFirstBySensorId(sensor2.getSensorId())).thenReturn(sensor2);

        // Mocking the repository to return null when the loadSensor method is called with a non existing sensorId
        when(sensorRepository.findFirstBySensorId(3L)).thenReturn(null);

    }

    /**
     * Test for the method {@link SensorService#loadSensor(Long)}
     */
    @Test
    @DirtiesContext
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testInitialization(){
        addTestMocking();
        assertNotNull(sensor1, "Sensor1 is null");
        assertNotNull(sensor2, "Sensor2 is null");

        assertEquals(2, sensorService.getAllSensors().size(), "getAllSensors() does not return the correct number of sensors");

        assertDoesNotThrow(() ->{
            assertEquals(sensor1, sensorService.loadSensor(sensor1.getSensorId()), "loadSensor() does not return the correct sensor");
            assertEquals(sensor2, sensorService.loadSensor(sensor2.getSensorId()), "loadSensor() does not return the correct sensor");
        });
        assertThrows(SensorNotFoundException.class, () -> sensorService.loadSensor(3L), "loadSensor() does not throw an exception when the sensor does not exist");
    }

    /**
     * Test for the method {@link SensorService#getAllSensors()}
     */
    @Test
    @DirtiesContext
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetAllSensors() {
        addTestMocking();
        Collection<Sensor> sensors = sensorService.getAllSensors();
        assertEquals(2, sensors.size(), "getAllSensors() does not return the correct number of sensors");
        assertTrue(sensors.contains(sensor1), "getAllSensors() does not return the correct sensors");
        assertTrue(sensors.contains(sensor2), "getAllSensors() does not return the correct sensors");
    }

    /**
     * Test for the method {@link SensorService#loadSensor(Long)}
     */
    @Test
    @DirtiesContext
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testLoadSensor() {
        addTestMocking();
        assertDoesNotThrow(() ->{
            assertEquals(sensor1, sensorService.loadSensor(sensor1.getSensorId()), "loadSensor() does not return the correct sensor");
            assertEquals(sensor2, sensorService.loadSensor(sensor2.getSensorId()), "loadSensor() does not return the correct sensor");
        });
    }

    /**
     * Test for the method {@link SensorService#loadSensor(Long)}
     */
    @Test
    @DirtiesContext
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testLoadSensorNotExisting() {
        addTestMocking();
        assertThrows(SensorNotFoundException.class,() ->{
            assertNull(sensorService.loadSensor(3L), "loadSensor() does not return null for a non existing sensorId");
        });
    }

    /**
     * Test for the method {@link SensorService#saveSensor(Sensor)}
     */
    @Test
    @DirtiesContext
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testSaveSensor() {
        addTestMocking();

        Sensor sensor3 = new Sensor();
        sensor3.setSensorId(3L);
        sensor3.setName("Sensor3");

         sensorService.saveSensor(sensor3);

         // Mocking the repository to return the sensor3 when the saveSensor method is called
         when(sensorRepository.save(sensor3)).thenReturn(sensor3);

        // Mocking the repository to return the sensor1, sensor2 and sensor3 when the getAllSensors method is called
        when(sensorRepository.findAll()).thenReturn(List.of(new Sensor[]{sensor1, sensor2, sensor3}));

        // Mocking the repository to return the sensor3 when the loadSensor method is called
        when(sensorRepository.findFirstBySensorId(sensor3.getSensorId())).thenReturn(sensor3);
        assertEquals(3, sensorService.getAllSensors().size(), "getAllSensors() does not return the correct number of sensors");

        assertDoesNotThrow(() ->{
            assertEquals(sensor3, sensorService.loadSensor(sensor3.getSensorId()), "loadSensor() does not return the correct sensor");
        });
    }

    /**
     * Test for the method {@link SensorService#deleteSensor(Sensor)}
     */
    @Test
    @DirtiesContext
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testDeleteSensor() {
        addTestMocking();

        sensorService.deleteSensor(sensor1);

        // Mocking the repository to return the sensor2 when the getAllSensors method is called
        when(sensorRepository.findAll()).thenReturn(List.of(new Sensor[]{sensor2}));
        // Mocking the repository to return null when the loadSensor method is called with a non existing sensorId
        when(sensorRepository.findFirstBySensorId(sensor1.getSensorId())).thenReturn(null);
        assertThrows(SensorNotFoundException.class,() ->{
            assertNull(sensorService.loadSensor(sensor1.getSensorId()), "loadSensor() does not return null for a non existing sensorId");
        });
        assertDoesNotThrow(() -> {
            assertEquals(1, sensorService.getAllSensors().size(), "getAllSensors() does not return the correct number of sensors");
            assertEquals(sensor2, sensorService.loadSensor(sensor2.getSensorId()), "loadSensor() does not return the correct sensor");
        });
    }

    /**
     * Test for the method {@link SensorService#saveSensor(Sensor)}
     */
    @Test
    @DirtiesContext
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testUpdateSensor() {
        addTestMocking();

        sensor1.setName("Sensor1Updated");
        sensorService.saveSensor(sensor1);

        // Mocking the repository to return the sensor1Updated when the loadSensor method is called
        when(sensorRepository.findFirstBySensorId(sensor1.getSensorId())).thenReturn(sensor1);
        assertDoesNotThrow(() -> {
            assertEquals(sensor1, sensorService.loadSensor(sensor1.getSensorId()), "loadSensor() does not return the correct sensor");
        });
    }


    /*
    @Test
    @DirtiesContext
    void testSaveSensorNotAdmin() {
        addTestMocking();
        //TODO: Fix this test
        assertThrows(org.springframework.security.access.AccessDeniedException.class, () -> {
            Sensor laodedSensor = sensorService.loadSensor(sensor1.getSensorId());
            laodedSensor.setName("Sensor1Updated");
            sensorService.saveSensor(laodedSensor);
        }, "saveSensor() does not throw an AccessDeniedException when a non admin user tries to save a sensor");
    }

    @Test
    @DirtiesContext
    void testDeleteSensorNotAdmin() {
        addTestMocking();
        // Todo: Fix this test
        assertThrows(org.springframework.security.access.AccessDeniedException.class, () -> {
            sensorService.deleteSensor(sensor1);
        }, "deleteSensor() does not throw an AccessDeniedException when a non admin user tries to delete a sensor");
    }

     */
}
