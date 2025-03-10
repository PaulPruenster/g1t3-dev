package at.qe.skeleton.view;

import at.qe.skeleton.model.AccessPoint;
import at.qe.skeleton.model.Sensor;
import at.qe.skeleton.model.SensorStation;
import at.qe.skeleton.model.UserRole;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.component.organigram.OrganigramHelper;
import org.primefaces.event.organigram.OrganigramNodeDragDropEvent;
import org.primefaces.model.DefaultOrganigramNode;
import org.primefaces.model.OrganigramNode;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Component
@Scope("view")
@Named
public class OrganigramView extends DefaultOrganigramView implements Serializable  {

    private List<AccessPoint> accessPoints = null;

    private List<AccessPoint> selectedAccessPoints = new ArrayList<>();

    private boolean isAdmin = false;

    private boolean isGardener = false;

    private boolean showSensors = true;


    /**
     * Adds nodes to the Organigram for an admin user, based on the selected access points and sensor stations.
     */
    public void addNodesForAdmin(){
        if (this.rootNode == null) {
            // return ifn o rootNode is set
            return;
        }
        // remove all nodes from root
        this.rootNode.setChildren(new ArrayList<>());

        for (AccessPoint accessPoint : this.selectedAccessPoints) {
            // ad AccessPointNoodes to root
            OrganigramNode accessPointNode = this.addAccessPoint(accessPoint, rootNode);
            ArrayList<SensorStation> sensorStations = new ArrayList<>(accessPoint.getSensorStations());

           // TODO: accesspoint has dubplicate sensorsations; remove them
            for (int i = 0; i < sensorStations.size(); i++) {
                for (int j = i + 1; j < sensorStations.size(); j++) {
                    if (sensorStations.get(i).equals(sensorStations.get(j))) {
                        sensorStations.remove(j);
                        j--;
                    }
                }
            }
            for (SensorStation sensorStation : sensorStations) {
                // add sensorStationNodes for the AccessPoint
                OrganigramNode sensorStationNode = addSensorStation(sensorStation, accessPointNode);
                if (this.showSensors) {
                    // Add Sensors if flag is set
                    for (Sensor sensor : sensorStation.getSensors()) {
                        addSensor(sensor, sensorStationNode);
                    }

                }
            }
        }
    }

    /**
     * Adds sensor stations and their sensors to the organigram tree for the current gardener user.
     * If the 'showSensors' flag is set to false, only the sensor stations are added to the tree.
     *
     */
    public void addNodesForGaertner(){
        this.rootNode.setChildren(new ArrayList<>());
        ArrayList<SensorStation> sensorStations = new ArrayList<>(this.sensorStationService.getAllSensorStationsByGaertner(this.sessionInfoBean.getCurrentUser()));
        for (SensorStation sensorStation : sensorStations) {
            OrganigramNode sensorStationNode = addSensorStation(sensorStation, rootNode);
            if(this.showSensors){
                for (Sensor sensor : sensorStation.getSensors()) {
                    addSensor(sensor, sensorStationNode);
                }
            }
        }
    }

    /**
     * Initializes the organigram view by setting up the selection, root node, and populating the organigram with nodes
     * based on the user role. If the user is an admin, all access points are shown, and if the user is a gardener, only the
     * sensor stations assigned to them are shown.
     */
    @PostConstruct
    public void init() {
        // set the selectionNode to null, because at the beginning nothing is selected
        selection = new DefaultOrganigramNode(null, null, null);

        // set up root node
        rootNode = new DefaultOrganigramNode("root", "System", null);
        rootNode.setCollapsible(false);
        rootNode.setSelectable(true);

        // check if user is admin or gardener and call the corresponding method
        if(this.isAdmin()){
            accessPoints = new ArrayList<>(this.accessPointService.getAllAccessPoints());
            this.selectedAccessPoints = new ArrayList<>(this.accessPoints);
            this.addNodesForAdmin();
        } else {
            this.addNodesForGaertner();
        }
    }



    /**
     * Creates a new OrganigramNode for an AccessPoint and adds it as a child node to the given parent node.
     * @param accessPoint The AccessPoint for which the node should be created.
     * @param organigramNode The parent node to which the new node should be added as a child.
     * @return The newly created OrganigramNode representing the AccessPoint.
     */
    public OrganigramNode addAccessPoint(AccessPoint accessPoint, OrganigramNode organigramNode){
        OrganigramNode accessPointNode = new DefaultOrganigramNode("accessPoint", accessPoint, organigramNode);
        accessPointNode.setDroppable(false);
        accessPointNode.setDraggable(false);
        accessPointNode.setSelectable(true);
        accessPointNode.setCollapsible(true);
        return  accessPointNode;
    }


    /**
     * Adds a new organigram node for a sensor station with the given information to the parent node.
     * @param sensorStation the sensor station to add to the organigram
     * @param organigramNode the parent node to add the sensor station node to
     * @return the created sensor station node
     */
    public OrganigramNode addSensorStation(SensorStation sensorStation, OrganigramNode organigramNode){
        OrganigramNode sensorStationNode = new DefaultOrganigramNode("sensorStation", sensorStation, organigramNode);
        sensorStationNode.setCollapsible(true);
        sensorStationNode.setSelectable(true);
        sensorStationNode.setDroppable(false);
        sensorStationNode.setDraggable(false);
        return  sensorStationNode;
    }

    /**
     * Creates and adds a sensor node to the specified parent node.
     * @param sensor the sensor to be added as a node
     * @param organigramNode the parent node to which the new sensor node should be added
     */
    public OrganigramNode addSensor(Sensor sensor, OrganigramNode organigramNode){
        OrganigramNode sensorNode = new DefaultOrganigramNode("sensor", sensor, organigramNode);
        sensorNode.setSelectable(true);
        sensorNode.setCollapsible(true);
        sensorNode.setDroppable(false);
        sensorNode.setDraggable(false);
        return  sensorNode;
    }




    /**
     * Applies a filter to the organigram by either adding all access points and sensor stations for the administrator
     * or adding only the access points and sensor stations that are assigned to the current user.
     */
    public void applyFilterToOrganigram(){
        if(this.isAdmin()){
            this.addNodesForAdmin();
        }else{
            this.addNodesForGaertner();
        }
    }

    /**
     * Resets the filter by setting the selected access points to all access points and applies the filter to the organigram.
     */
    public void resetFilter(){
        this.selectedAccessPoints = new ArrayList<>(this.accessPoints);
        this.applyFilterToOrganigram();
    }



    /**
     * Checks if the current user has admin privileges.
     * @return true if the user is an admin, false otherwise
     */
    public boolean isAdmin(){
        return this.sessionInfoBean.getCurrentUser().getRoles().contains(UserRole.ADMIN);
    }




    /**
     * Handles the drag and drop of nodes within the organigram, updating the relationships between the dragged node and its
     * new parent node in the process. If the drag and drop is not allowed based on the node types involved, the node is moved
     * back to its original parent node.
     * @param event The OrganigramNodeDragDropEvent containing the source, target, and dragged nodes.
     */
    public void nodeDragDropListener(OrganigramNodeDragDropEvent event) {
        OrganigramNode sourceNode = event.getSourceOrganigramNode();
        OrganigramNode targetNode = event.getTargetOrganigramNode();
        OrganigramNode draggedNode = event.getOrganigramNode();
        boolean isDropAllowed = true;
        if(!sourceNode.getType().equals(targetNode.getType())){
            targetNode.getChildren().remove(draggedNode);
            sourceNode.getChildren().add(draggedNode);
            isDropAllowed = false;
        }
        else if(targetNode.getType().equals("accessPoint")){
            AccessPoint accessPoint = (AccessPoint) targetNode.getData();
            SensorStation sensorStation = (SensorStation) draggedNode.getData();
            sensorStation.setAccessPoint(accessPoint);
            this.sensorStationService.saveSensorStation(sensorStation);
        }
        else if(targetNode.getType().equals("sensorStation")){
            SensorStation sensorStation = (SensorStation) targetNode.getData();
            Sensor sensor = (Sensor) draggedNode.getData();
            sensor.setSensorStation(sensorStation);
            this.sensorService.saveSensor(sensor);
        }
        FacesMessage message = new FacesMessage();
        message.setSummary("(Allowed: " + isDropAllowed +") Node '" + event.getOrganigramNode().getType()
                + "' moved from " + event.getSourceOrganigramNode().getType() + " to '" + event.getTargetOrganigramNode().getType() + "'");
        message.setSeverity(FacesMessage.SEVERITY_INFO);

        FacesContext.getCurrentInstance().addMessage(null, message);
    }




    /**
     * Updates the selected access point node with the latest information from the database.
     */
    public void updateAccessPointNode(){
       if (this.selection != null){
            AccessPoint selec  = (AccessPoint) this.selection.getData();
            if (selec != null){
                try{
                    AccessPoint loadedAccessPoint = accessPointService.loadAccessPoint(selec.getAccessPointId());
                    this.selection.setData(loadedAccessPoint);
                    setMessage("Updated AccessPoint " + loadedAccessPoint.getName() + "(" + loadedAccessPoint.getAccessPointId() + ")", FacesMessage.SEVERITY_INFO );
                }catch (Exception e) {
                    setMessage("Could notUpdated AccessPoint " + selec.getName() + "(" + selec.getAccessPointId() + ")", FacesMessage.SEVERITY_INFO );
                }
            }
        }
    }

    /**
     * Deletes the selected sensor and its corresponding organigram node.
     */
    public void deleteSensor(){
        Sensor sensor = (Sensor) selection.getData();
        this.sensorService.deleteSensor(sensor);
    OrganigramNode currentSelection = OrganigramHelper.findTreeNode(rootNode, selection);
        currentSelection.getParent().getChildren().remove(currentSelection);
        setMessage("Sensor " + sensor.getName() + " wurde entfernt.", FacesMessage.SEVERITY_INFO);
    }

    /**
     * Deletes the selected sensor station and its corresponding organigram node.
     */
    public void deleteSensorStation(){
        SensorStation sensorstation = (SensorStation) selection.getData();
        this.sensorStationService.deleteSensorStation(sensorstation);
        OrganigramNode currentSelection = OrganigramHelper.findTreeNode(rootNode, selection);
        currentSelection.getParent().getChildren().remove(currentSelection);
        setMessage("SensorStation " + sensorstation.getName() + " wurde entfernt.", FacesMessage.SEVERITY_INFO);

    }

    /**
     * Deletes the selected access point and its corresponding organigram node.
     */
    public void deleteAccessPoint(){
        AccessPoint accessPoint = (AccessPoint) selection.getData();
        this.accessPointService.deleteAccessPoint(accessPoint);
        OrganigramNode currentSelection = OrganigramHelper.findTreeNode(rootNode, selection);
        currentSelection.getParent().getChildren().remove(currentSelection);
        setMessage("AccessPoint " + accessPoint.getName() + " wurde entfernt.", FacesMessage.SEVERITY_INFO);

        this.selectedAccessPoints =  new ArrayList<>(this.accessPointService.getAllAccessPoints());

        PrimeFaces.current().executeScript("location.reload()");


    }


    public void changeToImages(){
        if (this.selection == null){
            return;
        }
        SensorStation sensorstation = (SensorStation) selection.getData();
        if (sensorstation != null){
            String url = "/public/images.xhtml?id=";
            url += sensorstation.getSensorStationId();
            this.changeUrl(url);

        }
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

     public boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public boolean getGardener() {
        return isGardener;
    }

    public void setGardener(boolean gardener) {
        isGardener = gardener;
    }

    public List<AccessPoint> getAccessPoints() {
        return accessPoints;
    }

    public void setAccessPoints(List<AccessPoint> accessPoints) {
        this.accessPoints = accessPoints;
    }

    public List<AccessPoint> getSelectedAccessPoints() {
        return selectedAccessPoints;
    }

    public void setSelectedAccessPoints(List<AccessPoint> selectedAccessPoints) {
        this.selectedAccessPoints = selectedAccessPoints;
    }

    public boolean getShowSensors() {
        return showSensors;
    }

    public void setShowSensors(boolean showSensors) {
        this.showSensors = showSensors;
    }
}