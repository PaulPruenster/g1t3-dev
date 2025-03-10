package at.qe.skeleton.repositories;

import at.qe.skeleton.model.MeasurementData;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * Repository for managing a {@link MeasurementData} entities.
 * @see MeasurementData
 * @see AbstractRepository
 */
public interface MeasurementDataRepository extends AbstractRepository<MeasurementData, Long> {

    /**
     * Finds the first measurement data by its measurement ID.
     * @param sensorId the ID to search for
     * @return the found measurement data
     */
    @Query("SELECT m FROM MeasurementData m WHERE m.sensor.sensorId=?1")
    List<MeasurementData> findMeasurementDataBySensorId(long sensorId);

    /**
     * Finds the first measurement data by its measurement ID
     * @param id the ID to search for
     * @return the found measurement data
     */
    MeasurementData findFirstByMeasurementId(long id);

    /**
     * Finds the first measurement data by its measurement ID
     * @param sensorId the ID to search for
     * @return the found measurement data
     */
    MeasurementData findFirstBySensorIdOrderByMeasurementIdDesc(long sensorId);


    /**
     * Finds all measurement data by their sensor ID.
     * @param sensorId the sensor ID to search for
     * @param start the beginning of the time interval
     * @param end the end of the time interval
     * @return the found measurement data
     */
    @Query("SELECT m FROM MeasurementData m WHERE m.sensor.sensorId=?1 AND m.measurementTimestamp BETWEEN ?2 AND ?3")
    List<MeasurementData> findAllByMeasurementTimestampBetweenAndBySensorId(long sensorId, Date start, Date end);
}