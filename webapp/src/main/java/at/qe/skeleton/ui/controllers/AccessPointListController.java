package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.model.AccessPoint;
import at.qe.skeleton.services.AccessPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;

@Component
@Scope("view")
public class AccessPointListController implements Serializable {

    @Autowired
    private transient AccessPointService accessPointService;

    /**
     * Returns a list of all access points.
     * @return
     */
    public Collection<AccessPoint> getAllAccessPoints() {
        return accessPointService.getAllAccessPoints();
    }
}
