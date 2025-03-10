package at.qe.skeleton.view;

import at.qe.skeleton.model.*;
import at.qe.skeleton.services.AccessPointService;
import at.qe.skeleton.services.MeasurementDataService;
import at.qe.skeleton.services.SensorService;
import at.qe.skeleton.services.SensorStationService;
import at.qe.skeleton.ui.beans.SessionInfoBean;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import org.primefaces.event.organigram.OrganigramNodeCollapseEvent;
import org.primefaces.event.organigram.OrganigramNodeExpandEvent;
import org.primefaces.event.organigram.OrganigramNodeSelectEvent;
import org.primefaces.model.OrganigramNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

@Component
@Scope("view")
@Named
public class DefaultOrganigramView {

    @Autowired
    protected SessionInfoBean sessionInfoBean;

    @Autowired
    protected AccessPointService accessPointService;

    @Autowired
    protected SensorStationService sensorStationService;

    @Autowired
    protected SensorService sensorService;

    @Autowired
    protected MeasurementDataService measurementDataService;


    protected OrganigramNode rootNode;

    protected OrganigramNode selection;


    /**
     * Handles the node select event for organigram nodes.
     *
     * @param event The event triggered by the node select action.
     */
    public void nodeSelectListener(OrganigramNodeSelectEvent event) {
        // Empty Constructor
    }

    /**
     * Handles the node collapse event for organigram nodes.
     *
     * @param event The event triggered by the node collapse action.
     */
    public void nodeCollapseListener(OrganigramNodeCollapseEvent event) {
        this.setMessage("Node collapsed.", FacesMessage.SEVERITY_INFO);
    }

    /**
     * Handles the node expand event for organigram nodes.
     *
     * @param event The event triggered by the node expand action.
     */
    public void nodeExpandListener(OrganigramNodeExpandEvent event) {
        this.setMessage("Node expanded.", FacesMessage.SEVERITY_INFO);
    }


    public SessionInfoBean getSessionInfoBean() {
        return sessionInfoBean;
    }

    public void setSessionInfoBean(SessionInfoBean sessionInfoBean) {
        this.sessionInfoBean = sessionInfoBean;
    }


    public SensorStationService getSensorStationService() {
        return sensorStationService;
    }

    public void setSensorStationService(SensorStationService sensorStationService) {
        this.sensorStationService = sensorStationService;
    }


    /**
     * add a Message to the growl, the growl need to be updated, for it to work
     *
     * @param msg: message to display
     * @param severity: Type of message
     */
    protected void setMessage(String msg, FacesMessage.Severity severity) {
        FacesMessage message = new FacesMessage();
        message.setSummary(msg);
        message.setSeverity(severity);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }



    /**
     * Updates the selected Sensor node with the latest data from the database.
     * Displays a success message if the update is successful and an error message
     * otherwise.
     */
    public void updateSensorNode(){
        if (this.selection != null){
            Sensor selec  = (Sensor) this.selection.getData();
            if (selec != null){
                try{
                    Sensor loadedSensor = sensorService.loadSensor(selec.getSensorId());
                    this.selection.setData(loadedSensor);

                    // show message for SUCCESS
                    setMessage("Updated Sensor " + loadedSensor.getName() + "(" + loadedSensor.getSensorId() + ")", FacesMessage.SEVERITY_INFO );
                }catch (Exception e) {
                    // show message for ERROR
                    setMessage("Updated Sensor " + selec.getName() + "(" + selec.getSensorId() + ")", FacesMessage.SEVERITY_ERROR );
                }
            }
        }

    }

    public void updateSensorStationNode(){
        if (this.selection != null){
            SensorStation selec  = (SensorStation) this.selection.getData();
            if (selec != null){
                try{
                    SensorStation updatedSensorStation = sensorStationService.loadSensorStation(selec.getSensorStationId());
                    this.selection.setData(updatedSensorStation);

                    // update Organigram if the updated SensorStation has a different Gardener
                    // show message for SUCCESS
                    setMessage("Updated sensorstation " + updatedSensorStation.getName() + "(" + updatedSensorStation.getSensorStationId() + ")", FacesMessage.SEVERITY_INFO );
                }catch (Exception e) {
                    // show message for ERROR
                    setMessage("Could not update sensorstation " + selec.getName() + "(" + selec.getSensorStationId() + ")", FacesMessage.SEVERITY_ERROR );
                }
            }
        }
    }


    /**
     *
     * @param sensorStation
     * @return
     */
    public boolean sensorStationIsOnline(SensorStation sensorStation){
        return sensorStation != null;
    }


    public void changeUrl(String url){
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
        try{
            externalContext.redirect(request.getContextPath() + url);
        }catch (IOException e){
            this.setMessage("Could not redirect to " + url, FacesMessage.SEVERITY_ERROR);
        }
    }
    public void changeUrl(){
        if (this.selection == null) return;

        String url = "";
        switch (this.selection.getType()){
            case "sensorStation":
                SensorStation sensorStation = (SensorStation) this.selection.getData();
                url = "/detail/sensorStationDetail.xhtml?id=" + sensorStation.getSensorStationId() + "&faces-redirect=true";
                break;
            case "sensor":
                Sensor sensor = (Sensor) this.selection.getData();
                url = "/detail/sensorDetail.xhtml?id=" + sensor.getSensorId() + "&faces-redirect=true";
                break;
            case "accessPoint":
                AccessPoint accessPoint = (AccessPoint) this.selection.getData();
                url = "/detail/accessPointDetail.xhtml?id=" + accessPoint.getAccessPointId() + "&faces-redirect=true";
                break;
            case "user":
                url = "/admin/users.xhtml?faces-redirect=true";
                break;
            default:

                break;
        }
        if(!url.equals("")){
            this.changeUrl(url);
        }
    }


    public String getLastUpdate(Long id){
        MeasurementData measurementData = this.measurementDataService.getLatestMeasurementDataBySensorId(id);
        if (measurementData == null){
            return "No data available";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return measurementData.getMeasurementTimestamp().toLocalDateTime().format(formatter);
    }


    public OrganigramNode getRootNode() {
        return rootNode;
    }

    public void setRootNode(OrganigramNode rootNode) {
        this.rootNode = rootNode;
    }

    public OrganigramNode getSelection() {
        return selection;
    }

    public void setSelection(OrganigramNode selection) {
        this.selection = selection;
    }
}