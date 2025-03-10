package at.qe.skeleton.ui.beans;

import at.qe.skeleton.model.SensorStation;
import at.qe.skeleton.model.Sensor;
import at.qe.skeleton.services.SensorService;
import at.qe.skeleton.services.SensorStationService;
import jakarta.faces.context.FacesContext;
import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Scope("view")
public class SensorStationSaveBean {

    @Autowired
    private SensorStationService sensorStationService;

    @Autowired
    private SensorService sensorService;

    public void save(SensorStation sensorStation){
        if(sensorStation != null){
            sensorStationService.saveSensorStation(sensorStation);
            for(Sensor sensor: sensorStation.getSensors() ){
                sensorService.saveSensor(sensor);
            }
        }

    }

    public void openImages(SensorStation s) {
        String path = "/public/images.xhtml?id=" + s.getSensorStationId();
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(path);
        } catch (IOException err) {
            // take the not so clean way to redirect if the other does not work
            PrimeFaces.current().executeScript("window.location.href=%s".formatted(path));
        }
    }
}
