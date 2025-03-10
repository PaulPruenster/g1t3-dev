package at.qe.skeleton.ui.beans;

import at.qe.skeleton.ui.controllers.SensorDetailController;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Scope("view")
public class SensorDetailBean implements Serializable {

    @Autowired
    private SensorDetailController sensorDetailController;

    private String sensorId;


    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        Long id = Long.parseLong(sensorId);
        this.sensorDetailController.setSensorFromId(id);
        this.sensorId = sensorId;
    }


}
