package at.qe.skeleton.api.controllers;

import at.qe.skeleton.api.exceptions.SensorNotFoundException;
import at.qe.skeleton.model.MeasurementData;
import at.qe.skeleton.services.MeasurementDataService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import at.qe.skeleton.api.exceptions.MeasurementNotFoundException;

import java.util.Map;

@RestController
public class MeasurementController {
    
    @Autowired
    private MeasurementDataService measurementDataService;

    @PostMapping("/api/measurements")
    public void createMeasurement(@RequestBody Map<String, Object> request) throws SensorNotFoundException {

        Long sensorId = Long.parseLong(request.get("sensorId").toString());
        ObjectMapper mapper = new ObjectMapper();
        MeasurementData measurement = mapper.convertValue(request.get("measurement"), MeasurementData.class);
        measurementDataService.addMeasurementData(sensorId, measurement);

    }

    @GetMapping("/api/measurements/{id}")
    public MeasurementData getOneMeasurement(@PathVariable Long id) throws MeasurementNotFoundException {
            return measurementDataService.loadMeasurementData(id);

    }

    @PatchMapping("/api/measurement")
    public MeasurementData updateMeasurement(@RequestBody MeasurementData measurement)
    {
        return measurementDataService.saveMeasurementData(measurement);
    }

}
