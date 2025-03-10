package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.services.MeasurementDataService;
import at.qe.skeleton.model.MeasurementData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;

@Component
@Scope("view")
public class MeasurementDataListController implements Serializable {

    @Autowired
    private transient MeasurementDataService measurementDataService;

    public Collection<MeasurementData> getAllMeasurementDatas(){

        return measurementDataService.getAllMeasurementDatas();
    }
}
