package at.qe.skeleton.services;

import at.qe.skeleton.api.exceptions.AccessPointNotFoundException;
import at.qe.skeleton.model.AccessPoint;
import at.qe.skeleton.repositories.AccessPointRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import java.util.Collection;

/**
 * Service for accessing and manipulating access points.
 * Can be used for generic CRUD operations on access points.
 * @see AccessPoint
 * @see AccessPointRepository
 * @see AccessPointNotFoundException
 */
@Component
@Scope("application")
public class AccessPointService {

    @Autowired
    private AccessPointRepository accessPointRepository;

    @Autowired
    private BluetoothDeviceService bluetoothDeviceService;

    /**
     * Returns a list of all access points.
     * @return
     */
    @PreAuthorize("permitAll()")
    public Collection<AccessPoint> getAllAccessPoints() {
        return accessPointRepository.findAll();
    }

    /**
     * Loads an access point by its access point ID.
     * @param accessPointId
     * @return
     */
    @PreAuthorize("permitAll()")
    public AccessPoint loadAccessPoint(Long accessPointId) throws AccessPointNotFoundException {
        AccessPoint accessPoint =  accessPointRepository.findFirstByAccessPointId(accessPointId);
        if (accessPoint == null){
            throw new AccessPointNotFoundException("Access point not found");
        }
        return accessPoint;
    }

    /**
     * Saves an access point.
     * @param accessPoint
     */
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public AccessPoint saveAccessPoint(AccessPoint accessPoint) {
        return accessPointRepository.save(accessPoint);
    }

    /**
     * Deletes an access point.
     * @param accessPoint
     */
    @Transactional
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public void deleteAccessPoint(AccessPoint accessPoint) {
        this.bluetoothDeviceService.deleteAllByAccessPoint(accessPoint);
        accessPointRepository.delete(accessPoint);


    }



}
