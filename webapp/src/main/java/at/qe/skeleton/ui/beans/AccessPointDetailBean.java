package at.qe.skeleton.ui.beans;

import at.qe.skeleton.api.exceptions.AccessPointNotFoundException;
import  javax.faces.context.FacesContext;

import at.qe.skeleton.model.SensorStation;
import at.qe.skeleton.services.SensorStationService;
import at.qe.skeleton.ui.controllers.AccessPointDetailController;
import at.qe.skeleton.ui.controllers.SensorStationListController;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

@Component
@Scope("view")
public class AccessPointDetailBean implements Serializable {

    @Autowired
    private AccessPointDetailController accessPointDetailController;


    @Autowired
    private SensorStationListController sensorStationListController;

    @Autowired
    private transient SensorStationService sensorStationService;

    private String accessPointId;




    public void init() throws AccessPointNotFoundException {


        if(this.accessPointDetailController.getAccessPoint() != null ) return;

        Map<String, String> params;
        FacesContext context = FacesContext.getCurrentInstance();
        params = context.getExternalContext().getRequestParameterMap();
        String idString = params.get("id");
        Long id = Long.parseLong(idString);
        this.accessPointDetailController.setAccessPointFromAccessPointId(id);



    }

    public String getAccessPointId() {
        return accessPointId;
    }

    public void setAccessPointId(String accessPointId) throws AccessPointNotFoundException {
        Long id = Long.parseLong(accessPointId);
        this.accessPointDetailController.setAccessPointFromAccessPointId(id);
        this.accessPointId = accessPointId;
        System.out.println("AccessPointDetailBean: setAccessPointId: " + accessPointId);
    }

    public Collection<SensorStation> getSensorStationListFromAccessPoint(){
        return this.sensorStationService.getAllSensorStationsByAccessPoint(this.accessPointDetailController.getAccessPoint());

    }

}
