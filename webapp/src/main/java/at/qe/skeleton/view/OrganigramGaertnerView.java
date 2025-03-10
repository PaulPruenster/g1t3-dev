package at.qe.skeleton.view;

import at.qe.skeleton.model.*;
import at.qe.skeleton.services.UserService;
import at.qe.skeleton.ui.controllers.SensorStationDetailController;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.inject.Named;
import org.primefaces.event.organigram.OrganigramNodeDragDropEvent;
import org.primefaces.model.DefaultOrganigramNode;
import org.primefaces.model.OrganigramNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Component
@Scope("view")
@Named
public class OrganigramGaertnerView extends DefaultOrganigramView implements Serializable {

    @Autowired
    private SensorStationDetailController sensorStationDetailController;

    @Autowired
    private transient UserService userService;

    private List<Userx> selectedUsers = new ArrayList<>() ;

    private List<Userx>  users = new ArrayList<>();

    private boolean showOnlyLoggedInUser = false;

    private boolean showNotAssigned = true;

    private boolean showSensors = true;

    /**
     * Initializes the Organigram view by setting the selection node to null,
     * creating the root node, getting all gardeners, resetting the filter,
     * and applying the filter to the Organigram.
     */
    @PostConstruct
    public void init(){
        // set the selectionNode to null, because at the beginning nothing is selected
        selection  = new DefaultOrganigramNode(null, null, null);

        // init root Node
        this.rootNode = new DefaultOrganigramNode("root", "System", null);
        this.rootNode.setCollapsible(false);
        this.rootNode.setSelectable(false);
        this.rootNode.setSelectable(true);

        // Get all Gardeners
        this.users = new ArrayList<>(this.userService.getAllUsersByRole(UserRole.GARDENER));

        // for start set Filter to default
        this.resetFilter();

        // Add all the Nodes to the Organigram
        this.applyFilterToOrganigram();

    }

    public void applyFilterToOrganigram(){
        // remove all Nodes from root
        this.rootNode.setChildren(new ArrayList<>());

        // get all Users selected user
        ArrayList<Userx>  renderUsers = new ArrayList<>(this.selectedUsers);

        if(this.showOnlyLoggedInUser){
            // only have the logged-in user
            renderUsers.removeIf(user -> !user.equals(this.sessionInfoBean.getCurrentUser()));
        }
        for (Userx user : renderUsers){

            // set up user node
            OrganigramNode userNode = addUser(user, rootNode);
            ArrayList<SensorStation> sensorStations = new ArrayList<>(this.sensorStationService.getAllSensorStationsByGaertner(user));
            for (SensorStation sensorStation : sensorStations) {
                // set up sensor station node
                OrganigramNode sensorStationNode = addSensorStation(sensorStation, userNode);
                if(this.showSensors) {
                    for (Sensor sensor : sensorStation.getSensors()) {
                        addSensor(sensor, sensorStationNode);
                    }
                }
            }
        }
        if(this.showNotAssigned){
            // set up not assigned node
            OrganigramNode notAssignedNode = new DefaultOrganigramNode("notAssigned", "Not assigned", rootNode);
            notAssignedNode.setDroppable(true);
            notAssignedNode.setDraggable(false);
            notAssignedNode.setSelectable(true);
            notAssignedNode.setCollapsible(true);
            notAssignedNode.setExpanded(true);

            //Add SensorStations without Gardener to the notAssigned Gardeners OrganigramNode
            ArrayList<SensorStation> sensorStations = new ArrayList<>(this.sensorStationService.getAllSensorStationsByGaertner(null));
            for (SensorStation sensorStation : sensorStations) {
                OrganigramNode sensorStationNode = this.addSensorStation(sensorStation, notAssignedNode);
                // Add Sensors to SensorStation if option is set to True
                if(this.showSensors){
                    for (Sensor sensor : sensorStation.getSensors()) {
                        addSensor(sensor, sensorStationNode);
                    }
                }
            }
        }
    }

    /**
     * Handles the drag and drop event for organigram nodes.
     *
     * @param event The event triggered by the drag and drop action.
     */
    public void nodeDragDropListener(OrganigramNodeDragDropEvent event) {
        OrganigramNode sourceNode = event.getSourceOrganigramNode();
        OrganigramNode targetNode = event.getTargetOrganigramNode();
        OrganigramNode draggedNode = event.getOrganigramNode();

        // check if drop is allowed and SensorStation is moved to not assigned
        if(!draggedNode.getType().equals(NodeType.SENSORSTATION.toString())){
            targetNode.getChildren().remove(draggedNode);
            sourceNode.getChildren().add(draggedNode);
            this.setMessage("Only sensor stations can be moved.", FacesMessage.SEVERITY_ERROR);
            return;
        }
        // check if drop is allowed and SensorStation is moved to not assigned
        if(targetNode.getType().equals(NodeType.NOTASSIGNED.toString())){
            SensorStation sensorStation = (SensorStation)draggedNode.getData();
            sensorStation.setGaertner(null);
            this.sensorStationService.saveSensorStation(sensorStation);
            this.setMessage("Sensor station " + sensorStation.getName() + " moved to not assigned.", FacesMessage.SEVERITY_INFO);
            return;
        }

        // check if drop is allowed and SensorStation is moved to user
        if(targetNode.getType().equals(NodeType.USER.toString())){
            SensorStation sensorStation = (SensorStation)draggedNode.getData();
            Userx user = (Userx)targetNode.getData();
            sensorStation.setGaertner(user);
            this.sensorStationService.saveSensorStation(sensorStation);
            this.setMessage("Sensor station " + sensorStation.getName() + " moved to user " + user.getUsername() + ".", FacesMessage.SEVERITY_INFO);
        }
    }

    public void updateSensorStationNodeAndSave(){
        this.sensorStationDetailController.doSaveSensorStation();
        this.updateSensorStationNode();
        this.applyFilterToOrganigram();
    }


    /**
     * adds a new OrganigramNode, of the type User
     * @param user: the user for the OrganigramNoe
     * @param organigramNode: the parentNode, to add the new Child
     */
    public OrganigramNode addUser(Userx user, OrganigramNode organigramNode){
        OrganigramNode userNode = new DefaultOrganigramNode("user", user, organigramNode);
        userNode.setSelectable(true);
        userNode.setCollapsible(true);
        userNode.setDraggable(false);
        userNode.setDroppable(true);
        return userNode;

    }

    /**
     * adds a new OrganigramNode, of the type sensorStation
     *
     * @param sensorStation: the SensorStation for the OrganigramNoe
     * @param organigramNode: the parentNode, to add the new Child
     */
    public OrganigramNode addSensorStation(SensorStation sensorStation, OrganigramNode organigramNode){
        OrganigramNode sensorStationNode = new DefaultOrganigramNode("sensorStation", sensorStation, organigramNode);
        sensorStationNode.setCollapsible(true);
        sensorStationNode.setSelectable(true);
        sensorStationNode.setDroppable(false);
        sensorStationNode.setDraggable(true);
        return  sensorStationNode;
    }

    /**
     * adds a new OrganigramNode, of the type sensor
     *
     * @param sensor: the SensorStation for the OrganigramNoe
     * @param organigramNode: the parentNode, to add the new Child
     */
    public OrganigramNode addSensor(Sensor sensor, OrganigramNode organigramNode){
        OrganigramNode sensorNode = new DefaultOrganigramNode(NodeType.SENSOR.toString(), sensor, organigramNode);
        sensorNode.setSelectable(true);
        sensorNode.setCollapsible(true);
        sensorNode.setDroppable(false);
        sensorNode.setDraggable(false);
        return  sensorNode;
    }

    /**
     * sets filter to default
     *
     */
    public void resetFilter(){
        this.showSensors = true;
        this.selectedUsers = new ArrayList<>(this.users);
        this.showNotAssigned = true;
        this.showOnlyLoggedInUser = false;
        this.applyFilterToOrganigram();
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

    public List<Userx> getSelectedUsers() {
        return selectedUsers;
    }

    public void setSelectedUsers(List<Userx> selectedUsers) {
        this.selectedUsers = selectedUsers;
    }

    public List<Userx> getUsers() {
        return users;
    }

    public void setUsers(List<Userx> users) {
        this.users = users;
    }

    public boolean getShowOnlyLoggedInUser() {
        return showOnlyLoggedInUser;
    }

    public void setShowOnlyLoggedInUser(boolean showOnlyLoggedInUser) {
        this.showOnlyLoggedInUser = showOnlyLoggedInUser;
    }

    public boolean getShowNotAssigned() {
        return showNotAssigned;
    }

    public void setShowNotAssigned(boolean showNotAssigned) {
        this.showNotAssigned = showNotAssigned;
    }

    public boolean getShowSensors() {
        return showSensors;
    }

    public void setShowSensors(boolean showSensors) {
        this.showSensors = showSensors;
    }

}


