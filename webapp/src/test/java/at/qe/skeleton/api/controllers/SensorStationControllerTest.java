package at.qe.skeleton.api.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import at.qe.skeleton.api.exceptions.SensorStationNotFoundException;
import at.qe.skeleton.model.SensorStation;
import at.qe.skeleton.services.SensorStationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@SpringBootTest
@AutoConfigureMockMvc
class SensorStationControllerTest {
    @Mock
    private SensorStationService sensorStationService;

    @InjectMocks
    private SensorStationController sensorStationController;

    private SensorStation sensorStation;

    @BeforeEach
    void setup() {
        sensorStation = new SensorStation();
        sensorStation.setSensorStationId(1L);
        sensorStation.setName("testSensorStation1");
    }

    @Test
    void testCreateSensorStation() throws Exception {
        // Arrange
        SensorStation expectedSensorStation = new SensorStation();
        expectedSensorStation.setName("testPost1");
        when(sensorStationService.addSensorStation((1L), (expectedSensorStation))).thenReturn(null);

        sensorStationService.addSensorStation(1L, expectedSensorStation);

        // Assert
        verify(sensorStationService, times(1)).addSensorStation((1L), (expectedSensorStation));
    }

    @Test
    void testGetSensorStation() throws Exception {
        when(sensorStationService.loadSensorStation(1L)).thenReturn(sensorStation);

        SensorStation result = sensorStationController.getSensorStation(1L);

        assertThat(result).isEqualTo(sensorStation);
    }

    @Test
    void testGetSensorStationNotFound() throws SensorStationNotFoundException {
        Long nonExistentId = 1L; // An ID that does not correspond to an existing SensorStation
        when(sensorStationService.loadSensorStation(nonExistentId))
                .thenThrow(new SensorStationNotFoundException(nonExistentId));
        assertThrows(SensorStationNotFoundException.class, () -> {
            sensorStationController.getSensorStation(nonExistentId);
        });
    }

    @Test
    void testGetSensorStations() {
        when(sensorStationService.getAllSensorStations()).thenReturn(Collections.singletonList(sensorStation));

        Iterable<SensorStation> result = sensorStationController.getSensorStations();

        assertThat(result).containsExactly(sensorStation);
    }

    @Test
    void testUpdateSensorStation() {
        SensorStation expectedSensorStation = new SensorStation();
        expectedSensorStation.setName("testPut1");
        when(sensorStationService.saveSensorStation(expectedSensorStation)).thenReturn(expectedSensorStation);

        SensorStation result = sensorStationController.updateSensorStation(expectedSensorStation);

        assertThat(result).isEqualTo(expectedSensorStation);
    }

    @Test
    void testNewAccessPoint() {
        SensorStation expectedSensorStation = new SensorStation();
        expectedSensorStation.setName("testPut1");
        when(sensorStationService.saveSensorStation(expectedSensorStation)).thenReturn(expectedSensorStation);

        SensorStation result = sensorStationController.updateSensorStation(expectedSensorStation);

        assertThat(result).isEqualTo(expectedSensorStation);
    }

    @Test
    void testIsSensorStationExisting() throws SensorStationNotFoundException {
        when(sensorStationService.loadSensorStation(1L)).thenReturn(sensorStation);

        boolean result = sensorStationController.isSensorStationExisting(1L);

        assertThat(result).isTrue();
    }


}
