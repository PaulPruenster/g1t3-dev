package at.qe.skeleton.services;

import at.qe.skeleton.model.DashboardItem;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.repositories.DashboardItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * Service for accessing and manipulating dashboard items.
 * Can be used for generic CRUD operations on dashboard items.
 * Used for the dashboard.
 * @see DashboardItem
 * @see DashboardItemRepository
 */
@Component
@Scope("application")
public class DashboardItemService {

    @Autowired
    private DashboardItemRepository dashboardItemRepository;

    /**
     * Returns a list of all access points.
     * @return
     */
    @PreAuthorize("permitAll()")
    public Collection<DashboardItem> getAllDashboardItems() {
        return dashboardItemRepository.findAll();
    }

    /**
     * Returns a list of all dashboard items filtesed by user.
     * @return
     */
    @PreAuthorize("permitAll()")
    public Collection<DashboardItem> getAllDashboardItemsByUser(Userx userx) {
        return dashboardItemRepository.getDashboardItemsByUser(userx);
    }

    /**
     * Loads an access point by its access point ID.
     * @param dashboardItemId
     * @return
     */
    @PreAuthorize("permitAll()")
    public DashboardItem loadDashboardItem(Long dashboardItemId) {
        return dashboardItemRepository.findFirstByDashboardItemId(dashboardItemId);
    }

    /**
     * Saves an access point.
     * @param dashboardItem
     */
    @PreAuthorize("permitAll()")
    public DashboardItem saveDashboardItem(DashboardItem dashboardItem) {
        return dashboardItemRepository.save(dashboardItem);
    }

    /**
     * Deletes an item.
     * @param dashboardItem
     */
    @PreAuthorize("permitAll()")
    public void deleteDashboardItem(DashboardItem dashboardItem) {
        dashboardItemRepository.delete(dashboardItem);
    }

    /**
     * Deletes an item by the id.
     * @param dashboardItemId
     */
    @PreAuthorize("permitAll()")
    public void deleteDashboardItemById(long dashboardItemId) {
        dashboardItemRepository.deleteById(dashboardItemId);
    }
}

