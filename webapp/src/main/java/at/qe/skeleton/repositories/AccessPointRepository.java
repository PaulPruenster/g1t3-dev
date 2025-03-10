package at.qe.skeleton.repositories;

import at.qe.skeleton.model.AccessPoint;

import java.util.List;


/**
 * Repository for managing a {@link AccessPoint} entities.
 */
public interface AccessPointRepository extends AbstractRepository<AccessPoint, Long> {

    /**
     * Finds the first access point by its access point ID.
     * @param accessPointId the ID to search for
     * @return the found access point
     */
    AccessPoint findFirstByAccessPointId(Long accessPointId);

    /**
     * Finds all access points by their name.
     * @param name the name to search for
     * @return  the found access points
     */
    List<AccessPoint> findAccessPointsByNameContains(String name);



}