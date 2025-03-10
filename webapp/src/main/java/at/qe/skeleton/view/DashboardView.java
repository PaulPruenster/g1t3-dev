package at.qe.skeleton.view;

import at.qe.skeleton.model.*;
import at.qe.skeleton.services.DashboardItemService;
import at.qe.skeleton.services.MeasurementDataService;
import at.qe.skeleton.services.SensorService;
import at.qe.skeleton.services.SensorStationService;
import at.qe.skeleton.ui.beans.SessionInfoBean;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;

@Component
@Scope("view")
public class DashboardView implements Serializable {

    private Collection<SensorStation> sensorStations;
    private SensorTyp[] itemTypes;

    private SensorStation selectedSensorStation;
    private SensorTyp selectedType;

    private Collection<DashboardItem> dashboardItems;

    private boolean editMode = false;

    @Autowired
    private transient SensorService sensorService;

    @Autowired
    private transient SensorStationService sensorStationService;

    @Autowired
    private transient DashboardItemService dashboardItemService;

    @Autowired
    private transient MeasurementDataService measurementDataService;

    @Autowired
    private SessionInfoBean sessionInfoBean;

    @PostConstruct
    public void init() {
        sensorStations = sensorStationService.getAllSensorStations();
        dashboardItems = dashboardItemService.getAllDashboardItemsByUser(this.sessionInfoBean.getCurrentUser());
        itemTypes = SensorTyp.values();
    }

    public void toggleEdit() {
        this.editMode = !this.editMode;
    }

    public String getLastMeasurement(SensorStation station, SensorTyp type) {
        Sensor sensor = sensorService.getSensorsBySensorStationAndSensorTyp(station, type);
        if (sensor == null) return "Sensor not found";

        MeasurementData lastMeasurment = measurementDataService.getLatestMeasurementDataBySensorId(sensor.getSensorId());
        if (lastMeasurment == null) return "No data";

        return lastMeasurment.getVal() + " " + getLabelByUnit(sensor.getSensorUnit());
    }

    public void addDashboardItem() {
        DashboardItem newItem = new DashboardItem();
        if (this.selectedSensorStation == null) {
            FacesMessage message = new FacesMessage(
                    FacesMessage.SEVERITY_WARN,
                    "Sensor Station was not selected",
                    "Please select one so add new widget");
            addMessage(message);
            return;
        }

        newItem.setSensorStation(this.selectedSensorStation);
        newItem.setItemType(this.selectedType);
        newItem.setUser(this.sessionInfoBean.getCurrentUser());

        try {
            this.dashboardItemService.saveDashboardItem(newItem);
        } catch (Exception e) {
            FacesMessage message = new FacesMessage();
            message.setSeverity(FacesMessage.SEVERITY_WARN);
            message.setSummary("Error while saving new item");
            message.setDetail(e.getMessage());

            addMessage(message);
        }

        this.dashboardItems = dashboardItemService.getAllDashboardItemsByUser(this.sessionInfoBean.getCurrentUser());
    }

    public void deleteItem(long id) {
        try{
            dashboardItemService.deleteDashboardItemById(id);
        } catch (Exception e) {
            FacesMessage message = new FacesMessage();
            message.setSeverity(FacesMessage.SEVERITY_WARN);
            message.setSummary("Error while deleting new item");
            message.setDetail(e.getMessage());

            addMessage(message);
        }

        FacesMessage message = new FacesMessage();
        message.setSeverity(FacesMessage.SEVERITY_INFO);
        message.setSummary("Item " + id + " was deleted");

        addMessage(message);

        this.dashboardItems = dashboardItemService.getAllDashboardItemsByUser(this.sessionInfoBean.getCurrentUser());

    }

    public String getLabelByType(SensorTyp typ) {
        return switch (typ) {
            case LUFTSENSOR -> "Luftsensor";
            case FOTOTRANSISTOR -> "Fototransistor";

            case TEMPERATURE -> "Temperature";
            case HUMIDITY -> "Humidity";
            case PRESSURE -> "Air Pressure";
            case AIR_QUALITY -> "Air Quality";
            case ALTITUDE -> "Altitude";
            case LIGHT -> "Light level";
            case HYGROMETER -> "Hygrometer";
        };
    }

    public String getLabelByUnit(SensorUnit unit) {
        return switch (unit) {
            case CELSIUS -> "°C";
            case PERCENT -> "%";
            case LITER -> "Liter";
            case MM_PER_CUBIC_CENTIMETER -> "mm/cm^3";
            case PASCAL -> "Pascal";
            case METER -> "m";
            case LUX -> "Lux";
            case GRAMS_PER_CUBIC_CENTIMETER -> "g/cm^3";
        };
    }

    private void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public Collection<SensorStation> getSensorStations() {
        return sensorStations;
    }

    public void setSensorStations(Collection<SensorStation> sensorStations) {
        this.sensorStations = sensorStations;
    }

    public Collection<DashboardItem> getDashboardItems() {
        return dashboardItems;
    }

    public void setDashboardItems(Collection<DashboardItem> dashboardItems) {
        this.dashboardItems = dashboardItems;
    }

    public SensorTyp getSelectedType() {
        return selectedType;
    }

    public void setSelectedType(SensorTyp selectedType) {
        this.selectedType = selectedType;
    }

    public SensorStation getSelectedSensorStation() {
        return selectedSensorStation;
    }

    public void setSelectedSensorStation(SensorStation selectedSensorStation) {
        this.selectedSensorStation = selectedSensorStation;
    }

    public SensorTyp[] getItemTypes() {
        return itemTypes;
    }

    public void setItemTypes(SensorTyp[] itemTypes) {
        this.itemTypes = itemTypes;
    }
}