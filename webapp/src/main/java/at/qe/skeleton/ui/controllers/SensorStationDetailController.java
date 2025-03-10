package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.api.exceptions.SensorStationNotFoundException;
import at.qe.skeleton.model.SensorStation;
import at.qe.skeleton.services.SensorStationService;
import at.qe.skeleton.services.UserService;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.io.Serializable;


/**
 * Controller for managing plant details.
 */
@Component
@Scope("view")
public class SensorStationDetailController implements Serializable {

    @Autowired
    private transient SensorStationService sensorStationService;

    @Autowired
    private transient UserService userService;

    private SensorStation sensorStation;

    private String sensorStationGardenName;
    /**
     * Sets the plant to be managed by this controller.
     * @param sensorStation The plant to manage.
     */
    public void setSensorStation(SensorStation sensorStation) {
        this.sensorStation = sensorStation;
        doReloadSensorStation();
    }

    /**
     * Gets the plant managed by this controller.
     * @return The plant managed by this controller.
     */
    public SensorStation getSensorStation() {
        return sensorStation;
    }

    /**
     * Reloads the plant from the database.
     */
    public void doReloadSensorStation() {
        try {
            if (sensorStation.getId() != null) {
                sensorStation = sensorStationService.loadSensorStation(sensorStation.getId());
                if(this.sensorStation.getGaertner() != null){
                    this.sensorStationGardenName = this.sensorStation.getGaertner().getUsername();
                }
                else{
                    this.sensorStationGardenName = "";
                }
            }
        } catch (SensorStationNotFoundException e) {
            FacesMessage msg = new FacesMessage("Error while reloading Sensor Station", e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    /**
     * Saves changes to the plant to the database.
     */
    public void doSaveSensorStation() {
        if(this.sensorStation.getGaertner() == null || !this.sensorStationGardenName.equals(sensorStation.getGaertner().getUsername())){
            this.sensorStation.setGaertner(userService.loadUser(this.sensorStationGardenName));
        }
        sensorStationService.saveSensorStation(sensorStation);
    }

    /**
     * Deletes the plant from the database.
     */
    public void doDeleteSensorStation() {
        sensorStationService.deleteSensorStation(sensorStation);
    }

    public String getUrlToImages(){
        if (this.sensorStation == null) {
            return "";
        }
        return "/public/images.xhtml?id=" + sensorStation.getSensorStationId() + "&faces-redirect=true";

    }

    public SensorStationService getSensorStationService() {
        return sensorStationService;
    }

    public void setSensorStationService(SensorStationService sensorStationService) {
        this.sensorStationService = sensorStationService;
    }

    public String getSensorStationGardenName() {
        return sensorStationGardenName;
    }

    public void setSensorStationGardenName(String sensorStationGardenName) {
        this.sensorStationGardenName = sensorStationGardenName;
    }

    public void setSensorStationFromId(Long id) {
        try {
            this.setSensorStation(sensorStationService.loadSensorStation(id));

        } catch (SensorStationNotFoundException e) {
            FacesMessage msg = new FacesMessage("Error while loading Sensor Station", e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
}
