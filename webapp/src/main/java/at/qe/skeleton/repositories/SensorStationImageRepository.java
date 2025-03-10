package at.qe.skeleton.repositories;

import at.qe.skeleton.model.SensorStationImage;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repository for managing a {@link SensorStationImage} entities.
 * @see SensorStationImage
 * @see AbstractRepository
 * @see SensorStationImageRepository
 */
public interface SensorStationImageRepository extends AbstractRepository<SensorStationImage, Long> {

    /**
     *  Finds the first image by its image ID.
     * @param imageId the ID to search for
     * @return the found image
     */
    SensorStationImage findFirstByImageId(Long imageId);

    /**
     * Finds all images by their name.
     * @param name the name to search for
     * @return the found images
     */
    List<SensorStationImage> findSensorsByNameContains(String name);

    /**
     * Finds all images by their sensor station.
     * @param stationId the sensor station to search for
     * @return the found images
     */
    @Query("SELECT i FROM SensorStationImage i WHERE i.sensorStation.sensorStationId=?1")
    List<SensorStationImage> getAllImagesFromStationId(Long stationId);

    /**
     * Deletes an image by its image ID.
     * @param imageId the ID to search for
     */
    void deleteById(Long imageId);

    /**
     * Deletes all images.
     */
    void deleteAll();

}
