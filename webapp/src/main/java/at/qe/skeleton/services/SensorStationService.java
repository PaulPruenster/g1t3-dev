package at.qe.skeleton.services;

import at.qe.skeleton.api.exceptions.AccessPointNotFoundException;
import at.qe.skeleton.api.exceptions.SensorStationNotFoundException;
import at.qe.skeleton.model.AccessPoint;
import at.qe.skeleton.model.SensorStation;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.repositories.SensorStationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.Collection;


/**
 * Service for accessing and manipulating sensor stations.
 * Can be used for generic CRUD operations on sensor stations.
 * @see SensorStation
 * @see SensorStationRepository
 * @see SensorStationNotFoundException
 */
@Component
@Scope("application")
public class SensorStationService {

    @Autowired
    private SensorStationRepository sensorStationRepository;

    @Autowired
    private AccessPointService accessPointService;

    /**
     * Returns a list of all plants.
     * @return A collection of all plants.
     */
    @PreAuthorize("permitAll()")
    public Collection<SensorStation> getAllSensorStations() {
        return sensorStationRepository.findAll();
    }


    /**
     * Loads a plant by its ID.
     * @param id The ID of the plant to load.
     * @return The plant with the given ID, or null if no such plant exists.
     */
    @PreAuthorize("permitAll()")
    public SensorStation loadSensorStation(long id) throws SensorStationNotFoundException {
        SensorStation sensorStation = sensorStationRepository.findFirstBySensorStationId(id);
        if(sensorStation == null){
            throw new SensorStationNotFoundException("SensorStation not found");
        }
        return sensorStation;

    }

    /**
     * Saves a plant to the database.
     * @param sensorStation The plant to save.
     */
    @PreAuthorize("hasAuthority('GARDENER') or hasAuthority('ADMIN')")
    public SensorStation saveSensorStation(SensorStation sensorStation) {
        return sensorStationRepository.save(sensorStation);
    }

    /**
     * Deletes a plant from the database.
     * @param sensorStation The plant to delete.
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteSensorStation(SensorStation sensorStation) {
        sensorStationRepository.delete(sensorStation);
    }


    /**
     * Adds a plant to the database.
     * @param AccessPointId The ID of the accessPoint to which the plant is added.
     * @param sensorStation The plant to add.
     */
    @PreAuthorize("permitAll()")
    public SensorStation addSensorStation(Long accessPointId, SensorStation sensorStation) throws AccessPointNotFoundException {
        if(accessPointId != null){
            AccessPoint accessPoint = accessPointService.loadAccessPoint(accessPointId);
            sensorStation.setAccessPoint(accessPoint);
        }
        return sensorStationRepository.save(sensorStation);
    }

    /**
     * Returns a list of all plants.
     * @param gaertner The gaertner of the plants to load.
     * @return A collection of all plants.
     */
    @PreAuthorize("permitAll()")
    public Collection<SensorStation> getAllSensorStationsByGaertner(Userx gaertner) {
        return sensorStationRepository.findAllByGaertner(gaertner);
    }

    /**
     * Returns a list of all plants.
     * @param accessPoint The accessPoint of the plants to load.
     * @return A collection of all plants.
     */
    public Collection<SensorStation> getAllSensorStationsByAccessPoint(AccessPoint accessPoint) {
        return sensorStationRepository.findAllByAccessPoint(accessPoint);
    }



}
