package at.qe.skeleton.repositories;

import at.qe.skeleton.model.Sensor;
import at.qe.skeleton.model.SensorStation;
import at.qe.skeleton.model.SensorTyp;

import java.util.List;


/**
 * Repository for managing a {@link Sensor} entities.
 * @see Sensor
 * @see AbstractRepository
 * @see SensorRepository
 */
public interface SensorRepository extends AbstractRepository<Sensor, Long> {

    /**
     * Finds the first sensor by its sensor ID.
     * @param sensorId the ID to search for
     * @return the found sensor
     */
    Sensor findFirstBySensorId(Long sensorId);

    /**
     * Finds all sensors by their name.
     * @param name the name to search for
     * @return the found sensors
     */
    List<Sensor> findSensorsByNameContains(String name);

    /**
     * Deletes a sensor by its sensor ID.
     * @param sensorId the ID to search for
     */
    void deleteById(Long sensorId);

    /**
     * Deletes all sensors.
     */
    void deleteAll();


    /**
     * Finds all sensors by their sensor station.
     * @param sensorStation the sensor station to search for
     * @return the found sensors
     */
    List<Sensor>findAllBySensorStation(SensorStation sensorStation);

    Sensor findAllBySensorStationAndSensorTyp(SensorStation sensorStation, SensorTyp type);

}
