package at.qe.skeleton.services;

import at.qe.skeleton.api.exceptions.SensorNotFoundException;
import at.qe.skeleton.api.exceptions.SensorStationNotFoundException;
import at.qe.skeleton.model.Sensor;
import at.qe.skeleton.model.SensorStation;
import at.qe.skeleton.model.SensorTyp;
import at.qe.skeleton.repositories.SensorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * Service for accessing and manipulating sensors.
 * Can be used for generic CRUD operations on sensors.
 * @see Sensor
 * @see SensorRepository
 * @see SensorNotFoundException
 */
@Component
@Scope("application")
public class SensorService {

    @Autowired
    private SensorRepository sensorRepository;

    @Autowired
    private SensorStationService sensorStationService;


    /**
     * Returns a list of all sensors.
     * @return
     */
    @PreAuthorize("permitAll()")
    public Collection<Sensor> getAllSensors() {
        return sensorRepository.findAll();
    }

    /**
     * Loads a sensor by its sensor ID.
     * @param sensorId
     * @return
     */
    @PreAuthorize("permitAll()")
    public Sensor loadSensor(Long sensorId) throws SensorNotFoundException {

        Sensor sensor = sensorRepository.findFirstBySensorId(sensorId);
        if(sensor == null){
            throw new SensorNotFoundException("Sensor not found");
        }
        return sensor;
    }


    /**
     * Saves a sensor.
     * @param sensor
     */
    @PreAuthorize("hasAuthority('GARDENER') or hasAuthority('ADMIN')")
    public Sensor saveSensor(Sensor sensor) {
        return sensorRepository.save(sensor);
    }


    /**
     * Deletes a sensor.
     * @param sensor
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteSensor(Sensor sensor) {
        sensorRepository.delete(sensor);
    }

    /**
     * Adds a sensor to a sensor station.
     * @param sensorStationId the id of the sensor station
     * @param sensor the sensor to add
     * @return the added sensor
     * @throws SensorStationNotFoundException if the sensor station does not exist
     */
    @PreAuthorize("permitAll()")
    public Sensor addSensor(Long sensorStationId, Sensor sensor) throws SensorStationNotFoundException {
        if(sensorStationId != null){
            SensorStation sensorStation = sensorStationService.loadSensorStation(sensorStationId);
            sensor.setSensorStation(sensorStation);
        }
        return sensorRepository.save(sensor);
    }

    public Collection<Sensor> getSensorsBySensorStation(SensorStation sensorStation) {
        return sensorRepository.findAllBySensorStation(sensorStation);
    }

    @PreAuthorize("permitAll()")
    public Sensor getSensorsBySensorStationAndSensorTyp(SensorStation station, SensorTyp type) {
        return sensorRepository.findAllBySensorStationAndSensorTyp(station, type);
    }
}
