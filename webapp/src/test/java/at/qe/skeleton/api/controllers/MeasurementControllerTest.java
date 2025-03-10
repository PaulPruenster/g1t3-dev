package at.qe.skeleton.api.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import java.util.HashMap;
import java.util.Map;

import at.qe.skeleton.api.exceptions.MeasurementNotFoundException;
import at.qe.skeleton.api.exceptions.SensorNotFoundException;
import at.qe.skeleton.model.MeasurementData;
import at.qe.skeleton.services.MeasurementDataService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


@SpringBootTest
class MeasurementControllerTest {

    @Mock
    private MeasurementDataService measurementDataService;
    @InjectMocks
    private MeasurementController measurementController;


    @Test
    void createMeasurementFailTest() {
        Long sensorId = 1L;
        Map<String, Object> request = new HashMap<>();
        request.put("sensorId", sensorId);
        request.put("measurement", new MeasurementData());
        try {
            when(measurementDataService.addMeasurementData(eq(sensorId), any(MeasurementData.class)))
                    .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));
        } catch (ResponseStatusException e) {
            assertThrows(ResponseStatusException.class, () -> measurementController.createMeasurement(request), HttpStatus.NOT_FOUND.toString());
        } catch (SensorNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    void getOneMeasurementTest() throws MeasurementNotFoundException {
        long measurementId = 1L;
        MeasurementData measurementData = new MeasurementData();
        when(measurementDataService.loadMeasurementData(measurementId)).thenReturn(measurementData);

        MeasurementData result = measurementController.getOneMeasurement(measurementId);

        verify(measurementDataService).loadMeasurementData(measurementId);
        assertEquals(measurementData, result);
    }

    @Test
    void getOneMeasurementFailTest() throws MeasurementNotFoundException {
        long measurementId = 1L;
        when(measurementDataService.loadMeasurementData(measurementId)).thenThrow(new MeasurementNotFoundException());

        assertThrows(MeasurementNotFoundException.class, () -> measurementController.getOneMeasurement(measurementId));
    }

    @Test
    void updateMeasurementTest() {
        MeasurementData measurementData = new MeasurementData();
        when(measurementDataService.saveMeasurementData(measurementData)).thenReturn(measurementData);

        MeasurementData result = measurementController.updateMeasurement(measurementData);

        verify(measurementDataService).saveMeasurementData(measurementData);
        assertEquals(measurementData, result);
    }
}
