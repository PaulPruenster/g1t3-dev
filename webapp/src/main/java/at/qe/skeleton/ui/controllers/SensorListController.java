package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.model.Sensor;
import at.qe.skeleton.services.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;

@Component
@Scope("view")
public class SensorListController implements Serializable {

    @Autowired
    private transient SensorService sensorService;

    /**
     * Returns a list of all plants.
     * @return all sensors in the database
     */
    public Collection<Sensor> getAllSensors() {
        return sensorService.getAllSensors();
    }
}
