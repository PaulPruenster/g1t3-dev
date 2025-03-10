package at.qe.skeleton.converter;

import at.qe.skeleton.model.SensorStation;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SensorStationConverterTest
{

    @Test
    void testGetAsObjectWithNullInputValue() {
        SensorStationConverter converter = new SensorStationConverter();
        FacesContext context = mock(FacesContext.class);
        UIComponent component = mock(UIComponent.class);

        SensorStation result = converter.getAsObject(context, component, null);

        assertNull(result);
    }

    @Test
    void testGetAsObjectWithEmptyInputValue() {
        SensorStationConverter converter = new SensorStationConverter();
        FacesContext context = mock(FacesContext.class);
        UIComponent component = mock(UIComponent.class);
        String value = "";

        SensorStation result = converter.getAsObject(context, component, value);

        assertNull(result);
    }

    @Test
    void testGetAsStringWithNullSensorStation() {
        SensorStationConverter converter = new SensorStationConverter();
        FacesContext context = mock(FacesContext.class);
        UIComponent component = mock(UIComponent.class);

        String result = converter.getAsString(context, component, null);

        assertNull(result);
    }

}

