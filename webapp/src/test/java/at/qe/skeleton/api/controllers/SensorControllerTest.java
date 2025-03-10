package at.qe.skeleton.api.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.qe.skeleton.api.exceptions.SensorNotFoundException;
import at.qe.skeleton.model.Sensor;
import at.qe.skeleton.model.SensorTyp;
import at.qe.skeleton.model.SensorUnit;
import at.qe.skeleton.services.SensorService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@SpringBootTest
class SensorControllerTest {

    @Mock
    private SensorService sensorService;

    @InjectMocks
    private SensorController sensorController;

    @Test
    void testCreateSensor() throws Exception {
        Map<String, Object> requestBody = new HashMap<>();
        Map<String, Object> sensorData = new HashMap<>();
        sensorData.put("name", "testPost1");
        sensorData.put("sensorTyp", "HYGROMETER");
        sensorData.put("sensorUnit", "PERCENT");
        sensorData.put("upperLimit", 60.0);
        sensorData.put("lowerLimit", 30.0);
        sensorData.put("currentThresholdWarning", false);
        requestBody.put("sensorStationId", 1L);
        requestBody.put("sensor", sensorData);
        sensorController.createSensor(requestBody);
        verify(sensorService).addSensor(eq(1L), any(Sensor.class));
    }

    @Test
    void testCreateSensorWithMissingSensor() {
        // given
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("sensorStationId", 1L);

        // when
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> sensorController.createSensor(requestBody));

        // then
        assertEquals("sensor is missing", exception.getReason());
    }
    @Test
    void testCreateSensorWithInvalidSensorStationId() {
        Sensor sensor = new Sensor();
        sensor.setName("testPost1");
        sensor.setSensorTyp(SensorTyp.HYGROMETER);
        sensor.setSensorUnit(SensorUnit.PERCENT);
        sensor.setUpperLimit(60.0);
        sensor.setLowerLimit(30.0);
        sensor.setCurrentThresholdWarning(false);

        Map<String, Object> request = new HashMap<>();
        request.put("sensorStationId", "invalid_id");
        request.put("sensor", sensor);

        assertThrows(NumberFormatException.class, () -> {
            sensorController.createSensor(request);
        });
    }

    @Test
    void testGetSensor() throws Exception {
        Sensor sensor = new Sensor();
        when(sensorService.loadSensor(1L)).thenReturn(sensor);
        Sensor result = sensorController.getSensor(1L);
        assertSame(sensor, result);
    }

    @Test
    void testGetSensors() {
        List<Sensor> sensors = new ArrayList<>();
        Sensor sensor1 = new Sensor();
        sensor1.setSensorId(1L);
        sensor1.setName("testPost1");
        sensor1.setSensorTyp(SensorTyp.HYGROMETER);
        sensor1.setSensorUnit(SensorUnit.PERCENT);
        sensor1.setUpperLimit(60.0);
        sensor1.setLowerLimit(30.0);
        sensor1.setCurrentThresholdWarning(false);
        sensors.add(sensor1);
        Sensor sensor2 = new Sensor();
        sensor2.setSensorId(2L);
        sensor2.setName("testPost2");
        sensor2.setSensorTyp(SensorTyp.FOTOTRANSISTOR);
        sensor2.setSensorUnit(SensorUnit.CELSIUS);
        sensor2.setUpperLimit(25.0);
        sensor2.setLowerLimit(10.0);
        sensor2.setCurrentThresholdWarning(true);
        sensors.add(sensor2);

        when(sensorService.getAllSensors()).thenReturn(sensors);

        Iterable<Sensor> retrievedSensors = sensorController.getSensors();

        verify(sensorService, times(1)).getAllSensors();
        List<Sensor> retrievedSensorsList = new ArrayList<>();
        retrievedSensors.forEach(retrievedSensorsList::add);
        assertEquals(sensors, retrievedSensorsList);
    }

    @Test
    void testGetSensorWithUnknownId() throws SensorNotFoundException {
        // Arrange
        Long unknownId = 1L;
        when(sensorService.loadSensor(anyLong())).thenThrow(new SensorNotFoundException());

        // Act and Assert
        assertThrows(SensorNotFoundException.class, () -> {
            Sensor sensor = sensorController.getSensor(unknownId);
        });
    }

    @Test
    void testUpdateSensor() {
        Sensor sensor = new Sensor();
        sensor.setSensorId(1L);
        sensor.setName("testPost1");
        sensor.setSensorTyp(SensorTyp.HYGROMETER);
        sensor.setSensorUnit(SensorUnit.PERCENT);
        sensor.setUpperLimit(60.0);
        sensor.setLowerLimit(30.0);
        sensor.setCurrentThresholdWarning(false);
        when(sensorService.saveSensor(sensor)).thenReturn(sensor);
        Sensor updatedSensor = sensorController.updateSensor(sensor);
        assertEquals(sensor, updatedSensor);
    }
}

