package at.qe.skeleton.repositories;

import at.qe.skeleton.model.AccessPoint;
import at.qe.skeleton.model.SensorStation;
import at.qe.skeleton.model.Userx;

import java.util.List;

public interface SensorStationRepository extends AbstractRepository<SensorStation, Long> {
    /**
     * Finds the first sensor station by its sensor station ID.
     * @param plantId the ID to search for
     * @return the found sensor station
     */
    SensorStation findFirstBySensorStationId(Long plantId);

    /**
     * Finds all sensor stations by their user.
     * @param gaertner the ID to search for
     * @return the found sensor station
     */
    List<SensorStation> findAllByGaertner(Userx gaertner);

    /**
     * Finds all sensorStation by name.
     * @param name the ID to search for
     * @return the found sensor station
     */
    List<SensorStation> findByName(String name);

    /**
     * Finds all sensorStation by name, containing the given string.
     * @param name the ID to search for
     * @return the found sensor station
     */
    List<SensorStation> findByNameContaining(String name);


    /**
     * Finds all sensorStation by name, containing the given string, ignoring case.
     * @param name the ID to search for
     * @return the found sensor station
     */
    List<SensorStation> findByNameContainingIgnoreCase(String name);

    /**
     * Finds all sensorStation by AccessPoint.
     * @param accessPoint
     * @return
     */
    List<SensorStation> findAllByAccessPoint(AccessPoint accessPoint);
}