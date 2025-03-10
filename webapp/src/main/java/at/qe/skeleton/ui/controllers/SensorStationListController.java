package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.model.AccessPoint;
import at.qe.skeleton.model.SensorStation;
import at.qe.skeleton.services.SensorStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;

@Component
@Scope("view")
public class SensorStationListController implements Serializable {

    @Autowired
    private transient SensorStationService sensorStationService;

    /**
     * Returns a list of all plants.
     * @return all SensorStations in the database
     */
    public Collection<SensorStation> getAllSensorStations() {
        return sensorStationService.getAllSensorStations();
    }


    public Collection<SensorStation> getAllSensorStationsByAccessPointId(AccessPoint accessPoint) {
        return sensorStationService.getAllSensorStationsByAccessPoint(accessPoint);
    }
}