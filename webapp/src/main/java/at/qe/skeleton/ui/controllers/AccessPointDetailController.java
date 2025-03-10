package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.api.exceptions.AccessPointNotFoundException;
import at.qe.skeleton.model.AccessPoint;
import at.qe.skeleton.services.AccessPointService;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.io.Serializable;


/**
 * Controller for managing access point details.
 * This controller is used for the access point details page.
 * It is used to load the access point from the database and to save changes to the database.
 * It is also used to reload the access point from the database.
 * @see AccessPoint
 * @see AccessPointService
 * @see AccessPointNotFoundException
 */
@Component
@Scope("view")
public class AccessPointDetailController implements Serializable {

    @Autowired
    private transient AccessPointService accessPointService;

    private AccessPoint accessPoint;

    /**
     * Sets the access point to be managed by this controller.
     * @param accessPoint The access point to manage.
     */
    public void setAccessPoint(AccessPoint accessPoint) {
        this.accessPoint = accessPoint;
        doReloadAccessPoint();
    }

    /**
     * Gets the access point managed by this controller.
     * @return The access point managed by this controller.
     */
    public AccessPoint getAccessPoint() {
        return accessPoint;
    }

    /**
     * Reloads the access point from the database.
     */
    public void doReloadAccessPoint() {
        try {
            accessPoint = accessPointService.loadAccessPoint(accessPoint.getAccessPointId());
        }
        catch (AccessPointNotFoundException e) {
            FacesMessage msg = new FacesMessage("Error while reloading Access Point", e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    /**
     * Saves changes to the access point to the database.
     */
    public void doSaveAccessPoint() {
        if(this.accessPoint != null){
            accessPointService.saveAccessPoint(this.accessPoint);
        }
    }

    /**
     * Deletes the access point from the database.
     */
    public void doDeleteAccessPoint() {
        accessPointService.deleteAccessPoint(accessPoint);
    }

    public void setAccessPointFromAccessPointId (Long id) throws AccessPointNotFoundException {
        this.accessPoint = this.accessPointService.loadAccessPoint(id);
    }
}
