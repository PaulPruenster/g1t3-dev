package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.model.Sensor;
import at.qe.skeleton.services.SensorService;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Scope("view")
public class SensorDetailController implements Serializable {

    @Autowired
    private transient SensorService sensorService;

    private Sensor sensor;

    /**
     * Sets the plant to be managed by this controller.
     * @param sensor The plant to manage.
     */
    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
        doReloadSensor();
    }

    /**
     * Gets the plant managed by this controller.
     * @return The plant managed by this controller.
     */
    public Sensor getSensor() {
        return sensor;
    }

    /**
     * Reloads the plant from the database.
     */
    public void doReloadSensor() {
        if (sensor.getId() != null) {
            try{
                sensor = sensorService.loadSensor(sensor.getId());

            }catch (Exception e){
                FacesMessage msg = new FacesMessage("Error while reloading Sensor", e.getMessage());
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        }
    }

    /**
     * Saves changes to the plant to the database.
     */
    public void doSaveSensor() {
        if (sensor.getLowerLimit() > sensor.getUpperLimit())
            sensor.setLowerLimit(sensor.getUpperLimit());

        sensorService.saveSensor(sensor);
    }


    /**
     * Deletes the plant from the database.
     */
    public void doDeleteSensor() {
        sensorService.deleteSensor(sensor);
    }


    public void setSensorFromId(Long id) {
        try {
            this.setSensor(sensorService.loadSensor(id));
        } catch (Exception e) {
            FacesMessage msg = new FacesMessage("Error while loading Sensor Station", e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
}
