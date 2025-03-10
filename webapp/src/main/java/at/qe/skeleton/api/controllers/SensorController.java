package at.qe.skeleton.api.controllers;

import at.qe.skeleton.api.exceptions.SensorNotFoundException;
import at.qe.skeleton.api.exceptions.SensorStationNotFoundException;
import at.qe.skeleton.api.model.SensorLimit;
import at.qe.skeleton.model.Sensor;
import at.qe.skeleton.services.SensorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;


@RestController
public class SensorController {

    @Autowired
    private SensorService sensorService;


    /**
     * Creates a new Sensor
     * @param request
     *
     * JSON:
     * {
     *   "sensorStationId": 1, // Optional
     *   "sensor" : {
     *     "name": "testPost1",
     *     "sensorTyp": "HYGROMETER",
     *     "sensorUnit": "PERCENT",
     *     "upperLimit": 60.0,
     *     "lowerLimit": 30.0,
     *     "currentThresholdWarning": false
     *   }
     * }
     */
    @PostMapping("/api/sensor")
    public void createSensor(@RequestBody Map<String, Object> request) throws SensorStationNotFoundException {

        Object sensorStationIdObject = request.get("sensorStationId");
        Object sensorObject = request.get("sensor");
        if(sensorObject == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "sensor is missing");
        }
        Long sensorStationId = null;
        if(sensorStationIdObject != null){
            sensorStationId = Long.parseLong(sensorStationIdObject.toString());
        }
        ObjectMapper mapper = new ObjectMapper();
        Sensor sensor = mapper.convertValue(sensorObject, Sensor.class);
        sensorService.addSensor(sensorStationId, sensor);

    }

    @GetMapping(value="/api/sensor/{id}")
    public Sensor getSensor(@PathVariable Long id) throws SensorNotFoundException {
        return sensorService.loadSensor(id);

    }

    @GetMapping(value="/api/sensor/{id}/limits")
    public SensorLimit getSensorLimits(@PathVariable Long id) throws SensorNotFoundException {
        Sensor sensor = sensorService.loadSensor(id);
        return new SensorLimit(sensor);

    }

    @GetMapping("/api/sensors")
    public Iterable<Sensor> getSensors()
    {
        return sensorService.getAllSensors();
    }


    @PatchMapping("/api/sensor")
    public Sensor updateSensor(@RequestBody Sensor sensor)
    {
        return sensorService.saveSensor(sensor);
    }
}
