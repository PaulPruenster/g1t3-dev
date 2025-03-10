package at.qe.skeleton.repositories;

import at.qe.skeleton.model.DashboardItem;
import at.qe.skeleton.model.Userx;

import java.util.Collection;

/**
 * Repository for managing a {@link DashboardItem} entities.
 * @see DashboardItem
 * @see AbstractRepository
 * @see DashboardItemRepository
 */
public interface DashboardItemRepository extends AbstractRepository<DashboardItem, Long> {

    /**
     *  Finds the first dashboard item by its dashboard item ID.
     * @param dashboardItemId the ID to search for
     * @return the found dashboard item
     */
    DashboardItem findFirstByDashboardItemId(Long dashboardItemId);

    /**
     * Finds all dashboard items by their user.
     * @param userx the user to search for
     * @return the found dashboard items
     */
    Collection<DashboardItem> getDashboardItemsByUser(Userx userx);

    /**
     * Deletes a dashboard item by its dashboard item ID.
     * @param dashboardItemId the ID to search for
     */
    void deleteById(Long dashboardItemId);

    /**
     * Deletes all dashboard items.
     */
    void deleteAll();

}
