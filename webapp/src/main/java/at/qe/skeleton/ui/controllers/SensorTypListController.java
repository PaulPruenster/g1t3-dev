package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.model.SensorTyp;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Scope("view")
public class SensorTypListController  implements Serializable {


    public SensorTyp[] getItemTypes() {
        return SensorTyp.values();
    }
}
