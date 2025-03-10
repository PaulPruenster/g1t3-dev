package at.qe.skeleton.api.controllers;

import at.qe.skeleton.api.exceptions.SensorStationNotFoundException;
import at.qe.skeleton.api.exceptions.SensorTypNotFound;
import at.qe.skeleton.model.Sensor;
import at.qe.skeleton.model.SensorStation;
import at.qe.skeleton.model.SensorTyp;
import at.qe.skeleton.model.SensorUnit;
import at.qe.skeleton.services.SensorService;
import at.qe.skeleton.services.SensorStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
public class SensorStationController {

    @Autowired
    private SensorStationService sensorStationService;

    @Autowired
    private SensorService sensorService;

    /**
     * Creates a new SensorStation
     * @param request
     * @return
     *
     * JSON:
     * {
     *   "accessPointId": 1, // Optional
     *   "sensorStation": {
     *     "name": "testPost1"
     *   }
     * }
     */
    @PostMapping("/api/sensorStation/{id}/addSensor")
    public Long createSensor(@PathVariable Long id, @RequestBody Map<String, Object> request) throws SensorStationNotFoundException, SensorTypNotFound {

        Object sensorStationObject = request.get("typ");
        String typ = (String) sensorStationObject;

        SensorStation sensorStation = sensorStationService.loadSensorStation(id);

        Sensor sensor = new Sensor();
        sensor.setSensorStation(sensorStation);

        switch (typ) {
            case "HYGROMETER" -> {
                sensor.setSensorTyp(SensorTyp.HYGROMETER);
                sensor.setSensorUnit(SensorUnit.PERCENT);
                sensor.setLowerLimit(0.5);
                sensor.setUpperLimit(0.9);
            }
            case "TEMPERATURE" -> {
                sensor.setSensorTyp(SensorTyp.TEMPERATURE);
                sensor.setSensorUnit(SensorUnit.CELSIUS);
                sensor.setUpperLimit(26);
                sensor.setLowerLimit(20);
            }
            case "PRESSURE" -> {
                sensor.setSensorTyp(SensorTyp.PRESSURE);
                sensor.setSensorUnit(SensorUnit.PASCAL);
                sensor.setUpperLimit(960);
                sensor.setLowerLimit(940);
            }
            case "ALTITUDE" -> {
                sensor.setSensorTyp(SensorTyp.ALTITUDE);
                sensor.setSensorUnit(SensorUnit.METER);
                sensor.setUpperLimit(600);
                sensor.setLowerLimit(500);
            }
            case "LIGHT" -> {
                sensor.setSensorTyp(SensorTyp.LIGHT);
                sensor.setSensorUnit(SensorUnit.LUX);
                sensor.setUpperLimit(2000);
                sensor.setLowerLimit(1);
            }
            case "HUMIDITY" -> {
                sensor.setSensorTyp(SensorTyp.HUMIDITY);
                sensor.setSensorUnit(SensorUnit.PERCENT);
                sensor.setLowerLimit(60);
                sensor.setUpperLimit(90);
            }
            case "AIR_QUALITY" -> {
                sensor.setSensorTyp(SensorTyp.AIR_QUALITY);
                sensor.setSensorUnit(SensorUnit.GRAMS_PER_CUBIC_CENTIMETER);
                sensor.setLowerLimit(100);
                sensor.setUpperLimit(500);
            }
            default -> {
                throw new SensorTypNotFound();
            }
        }
        return sensorService.saveSensor(sensor).getSensorId();

    }

    @GetMapping("/api/sensorStation/{id}")
    public SensorStation getSensorStation(@PathVariable Long id) throws SensorStationNotFoundException {
        return sensorStationService.loadSensorStation(id);

    }

    @GetMapping("/api/sensorStations")
    public Iterable<SensorStation> getSensorStations()
    {
        return sensorStationService.getAllSensorStations();
    }


    @PatchMapping("/api/sensorStation")
    public SensorStation updateSensorStation(@RequestBody SensorStation sensorStation){
        return sensorStationService.saveSensorStation(sensorStation);
    }


    @GetMapping("/api/sensorStation/{id}/newSensor")
    public Long getNewAccessPoint(@PathVariable Long id) throws SensorStationNotFoundException {
        Sensor sensor = new Sensor();
        sensor.setName("new Sensor");
        SensorStation sensorStation = sensorStationService.loadSensorStation(id);
        sensor.setSensorStation(sensorStation);
        Sensor savedSensor = sensorService.saveSensor(sensor);
        return savedSensor.getSensorId();
    }

    @GetMapping("/api/sensorStation/{id}/exists")
    public boolean isSensorStationExisting(@PathVariable Long id) {
        try{
            sensorStationService.loadSensorStation(id);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
