package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.model.SensorUnit;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Scope("view")
public class SensorUnitListController implements Serializable {

    public SensorUnit[] getSensorUnits() {
        return SensorUnit.values();
    }
}
