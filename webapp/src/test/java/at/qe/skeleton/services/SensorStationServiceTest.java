package at.qe.skeleton.services;

import at.qe.skeleton.api.exceptions.SensorStationNotFoundException;
import at.qe.skeleton.model.SensorStation;
import at.qe.skeleton.repositories.SensorStationRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Incubating;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.web.WebAppConfiguration;

import java.nio.file.AccessDeniedException;
import java.rmi.NoSuchObjectException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@WebAppConfiguration
@SpringBootTest
public class SensorStationServiceTest {

    @Mock
    private SensorStationRepository sensorStationRepository;

    @InjectMocks
    private SensorStationService sensorStationService;

    private static SensorStation sensorStation1;

    private static SensorStation sensorStation2;

    private static SensorStation sensorStation3;

    /**
     * This method is called before all tests and initializes the test data
     */
    @BeforeAll
    static void init() {
        sensorStation1 = new SensorStation();
        sensorStation1.setSensorStationId(1L);
        sensorStation1.setName("SensorStation1");

        sensorStation2 = new SensorStation();
        sensorStation2.setSensorStationId(2L);
        sensorStation2.setName("SensorStation2");
    }

    /**
     * This method is called before each test and adds the test mocking
     */
    void addTestMocking() {
        // Mocking the repository to return the sensorStation1 and sensorStation2 when the getAllSensorStations method is called
        when(sensorStationRepository.findAll()).thenReturn(List.of(new SensorStation[]{sensorStation1, sensorStation2}));

        // Mocking the repository to return the sensorStation1 when the loadSensorStation method is called
        when(sensorStationRepository.findFirstBySensorStationId(sensorStation1.getSensorStationId())).thenReturn(sensorStation1);

        // Mocking the repository to return the sensorStation2 when the loadSensorStation method is called
        when(sensorStationRepository.findFirstBySensorStationId(sensorStation2.getSensorStationId())).thenReturn(sensorStation2);

        // Mocking the repository to return the sensorStation3 when the loadSensorStation method is called
        when(sensorStationRepository.findFirstBySensorStationId(3L)).thenReturn(sensorStation3);
    }

    /**
     * Test for the methode {@link SensorStationService#getAllSensorStations()}
     */
    @Test
    @DirtiesContext
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testInitialization() {
        addTestMocking();
        assertNotNull(sensorStation1);
        assertNotNull(sensorStation2);

        assertEquals(2, sensorStationService.getAllSensorStations().size());
        assertDoesNotThrow(() ->{
            assertEquals(sensorStation1, sensorStationService.loadSensorStation(sensorStation1.getSensorStationId()));
            assertEquals(sensorStation2, sensorStationService.loadSensorStation(sensorStation2.getSensorStationId()));

        });

        assertThrows(SensorStationNotFoundException  .class, () ->{
            sensorStationService.loadSensorStation(3L);
        });
    }

    /**
     * Test for the methode {@link SensorStationService#getAllSensorStations()}
     */
    @Test
    @DirtiesContext
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testLoadSensorStation() {
        addTestMocking();
        assertDoesNotThrow(() ->{
            assertEquals(sensorStation1, sensorStationService.loadSensorStation(sensorStation1.getSensorStationId()));
            assertEquals(sensorStation2, sensorStationService.loadSensorStation(sensorStation2.getSensorStationId()));
        });
        assertThrows(SensorStationNotFoundException.class, () -> {
            sensorStationService.loadSensorStation(3L);
        });
    }

    /**
     * Test for the methode {@link SensorStationService#getAllSensorStations()}
     */
    @Test
    @DirtiesContext
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testLoadSensorStationNotExisting() {
        addTestMocking();
        assertThrows(SensorStationNotFoundException.class, () ->{
            sensorStationService.loadSensorStation(3L);
        });
    }

    /**
     * Test for the methode {@link SensorStationService#getAllSensorStations()}
     */
    @Test
    @DirtiesContext
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testSaveSensorStation() {
        addTestMocking();
        sensorStation3 = new SensorStation();
        sensorStation3.setSensorStationId(3L);
        sensorStation3.setName("SensorStation3");

        sensorStationService.saveSensorStation(sensorStation3);

        // Mocking the repository to return the sensorStation3 when the loadSensorStation method is called
        when(sensorStationRepository.findFirstBySensorStationId(sensorStation3.getSensorStationId())).thenReturn(sensorStation3);

        // Mocking the repository to return the sensorStation1, sensorStation2 and sensorStation3 when the getAllSensorStations method is called
        when(sensorStationRepository.findAll()).thenReturn(List.of(new SensorStation[]{sensorStation1, sensorStation2, sensorStation3}));
        assertDoesNotThrow(() ->{
            assertEquals(sensorStation3, sensorStationService.loadSensorStation(sensorStation3.getSensorStationId()));
        });
        assertEquals(3, sensorStationService.getAllSensorStations().size());
    }

    /**
     * Test for the methode {@link SensorStationService#getAllSensorStations()}
     */
    @Test
    @DirtiesContext
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testDeleteSensorStation() {
        addTestMocking();
        sensorStationService.deleteSensorStation(sensorStation1);

        // Mocking the repository to return the sensorStation2 when the getAllSensorStations method is called
        when(sensorStationRepository.findAll()).thenReturn(List.of(new SensorStation[]{sensorStation2}));

        // Mocking the repository to return null when the loadSensorStation method is called
        when(sensorStationRepository.findFirstBySensorStationId(sensorStation1.getSensorStationId())).thenReturn(null);

        assertThrows(SensorStationNotFoundException.class,() ->{
            assertNull(sensorStationService.loadSensorStation(sensorStation1.getSensorStationId()));
        });
        assertDoesNotThrow(() ->{
            assertEquals(1, sensorStationService.getAllSensorStations().size());
            assertEquals(sensorStation2, sensorStationService.loadSensorStation(sensorStation2.getSensorStationId()));
        });
    }

    /**
     * Test for the methode {@link SensorStationService#getAllSensorStations()}
     */
    @Test
    @DirtiesContext
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testUpdateSensor() {
        addTestMocking();
        sensorStation1.setName("SensorStation1Updated");
        sensorStationService.saveSensorStation(sensorStation1);

        // Mocking the repository to return the sensorStation1Updated when the loadSensorStation method is called
        when(sensorStationRepository.findFirstBySensorStationId(sensorStation1.getSensorStationId())).thenReturn(sensorStation1);
        assertDoesNotThrow(() ->{
            assertEquals(sensorStation1, sensorStationService.loadSensorStation(sensorStation1.getSensorStationId()));
            assertEquals("SensorStation1Updated", sensorStationService.loadSensorStation(sensorStation1.getSensorStationId()).getName());
        });
    }

    /*
    @Test
    @DirtiesContext
    void testSaveSensorStationNotAdmin() {
        addTestMocking();
        // TODO: Fix this test
        assertThrows(AccessDeniedException.class, () -> {
            SensorStation laodedSensorStation = sensorStationService.loadSensorStation(sensorStation1.getSensorStationId());
            laodedSensorStation.setName("SensorStation1Updated");
            sensorStationService.saveSensorStation(laodedSensorStation);
        });
    }

    @Test
    @DirtiesContext
    void testDeleteSensorStationNotAdmin() {
        addTestMocking();
        // TODO: Fix this test
        assertThrows(AccessDeniedException.class, () -> {
            sensorStationService.deleteSensorStation(sensorStation1);
        });
    }

     */
}
