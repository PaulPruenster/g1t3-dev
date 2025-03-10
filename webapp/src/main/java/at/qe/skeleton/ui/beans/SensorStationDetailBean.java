package at.qe.skeleton.ui.beans;

import at.qe.skeleton.model.Sensor;
import at.qe.skeleton.services.SensorService;
import at.qe.skeleton.ui.controllers.SensorListController;
import at.qe.skeleton.ui.controllers.SensorStationDetailController;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.Serializable;
import java.util.Collection;

@Component
@Scope("view")
public class SensorStationDetailBean implements Serializable {

    @Autowired
    private SensorStationDetailController sensorStationDetailController;

    @Autowired
    private transient SensorService sensorService;

    @Autowired
    private SensorListController sensorListController;

    private String sensorStationId;


    public String getSensorStationId() {
        return sensorStationId;
    }

    public void setSensorStationId(String sensorStationId) {
        Long id = Long.parseLong(sensorStationId);
        this.sensorStationDetailController.setSensorStationFromId(id);
        this.sensorStationId = sensorStationId;
        System.out.println("AccessPointDetailBean: setAccessPointId: " + sensorStationId);
    }

    public Collection<Sensor> getSensorStationListFromAccessPoint() {
        return this.sensorService.getSensorsBySensorStation(this.sensorStationDetailController.getSensorStation());

    }

}
