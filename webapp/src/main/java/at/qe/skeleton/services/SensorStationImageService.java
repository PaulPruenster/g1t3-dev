package at.qe.skeleton.services;

import at.qe.skeleton.model.SensorStationImage;
import at.qe.skeleton.repositories.SensorStationImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import java.util.Collection;

/**
 * Service for accessing and manipulating sensors.
 * Can be used for generic CRUD operations on sensors.
 * @see SensorStationImage
 * @see SensorStationImageRepository
 */
@Component
@Scope("application")
public class SensorStationImageService {

    @Autowired
    private SensorStationImageRepository sensorStationImageRepository;

    /**
     * Returns a list of all sensors.
     * @return
     */
    @PreAuthorize("permitAll()")
    public Collection<SensorStationImage> getAllSensors() {
        return sensorStationImageRepository.findAll();
    }

    @PreAuthorize("permitAll()")
    public Collection<SensorStationImage> getAllImagesFromStationId(Long id) {
        return sensorStationImageRepository.getAllImagesFromStationId(id);
    }

    /**
     * Loads a sensor by its image ID.
     * @param imageId
     * @return
     */
    @PreAuthorize("permitAll()")
    public SensorStationImage loadSensorStationImage(Long imageId) {
        return sensorStationImageRepository.findFirstByImageId(imageId);
    }


    /**
     * Saves an image.
     * @param image
     */
    @PreAuthorize("permitAll()")
    public void saveSensorStationImage(SensorStationImage image) {
        sensorStationImageRepository.save(image);
    }


    /**
     * Deletes an image.
     * @param image
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteSensorStationImage(SensorStationImage image) {
        sensorStationImageRepository.delete(image);
    }
}
