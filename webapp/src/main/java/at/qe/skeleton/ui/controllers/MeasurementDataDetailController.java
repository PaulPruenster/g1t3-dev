package at.qe.skeleton.ui.controllers;


import at.qe.skeleton.api.exceptions.MeasurementNotFoundException;
import at.qe.skeleton.model.MeasurementData;
import at.qe.skeleton.services.MeasurementDataService;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Scope("view")
public class MeasurementDataDetailController implements Serializable {


    @Autowired
    private transient MeasurementDataService measurementDataService;

    private MeasurementData measurement;

    /**
     * Sets the plant to be managed by this controller.
     * @param measurement The plant to manage.
     */
    public void setMeasurement(MeasurementData measurement) {
        this.measurement = measurement;
        doReloadMeasurement();
    }

    /**
     * Gets the plant managed by this controller.
     * @return The plant managed by this controller.
     */
    public MeasurementData   getMeasurement() {
        return measurement;
    }

    /**
     * Reloads the plant from the database.
     */
    public void doReloadMeasurement() {
        try {
            if(measurement.getId() != null){
                measurement = measurementDataService.loadMeasurementData(measurement.getId());
            }
        }catch (MeasurementNotFoundException e){
            FacesMessage msg = new FacesMessage("Error while reloading Measurement", e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    /**
     * Saves changes to the plant to the database.
     */
    public void doSaveMeasurement() {
        measurementDataService.saveMeasurementData(measurement);
    }

    /**
     * Deletes the plant from the database.
     */
    public void doDeleteMeasurement() {
        measurementDataService.deleteMeasurementData(measurement);
    }


}
