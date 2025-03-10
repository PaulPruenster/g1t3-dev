package at.qe.skeleton.converter;

import at.qe.skeleton.api.exceptions.SensorStationNotFoundException;
import at.qe.skeleton.model.SensorStation;
import at.qe.skeleton.services.SensorStationService;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;

@FacesConverter(forClass = SensorStation.class)
public class SensorStationConverter implements Converter<SensorStation> {
    @Override
    public SensorStation getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty() || value.equals("Select Station...")){
            return null;
        }

        // Get the service bean from the application scope
        SensorStationService sensorStationService = context.getApplication()
                .evaluateExpressionGet(context, "#{sensorStationService}", SensorStationService.class);
        // Convert the value to a Long object
        long sensorStationId = Long.parseLong(value);

        // Retrieve the SensorStation object using the service
        try {
            return sensorStationService.loadSensorStation(sensorStationId);
        } catch (SensorStationNotFoundException e) {
            FacesMessage msg = new FacesMessage("SensorStation not found", e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, SensorStation value) {
        if (value == null || value.getSensorStationId() == null) {
            return null;
        }

        // Convert the SensorStation object to its sensorStationId
        return value.getSensorStationId().toString();
    }
}