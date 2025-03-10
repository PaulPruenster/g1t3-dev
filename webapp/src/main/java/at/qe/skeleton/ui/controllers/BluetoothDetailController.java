package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.model.BluetoothDevice;
import at.qe.skeleton.services.BluetoothDeviceService;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;


/**
 * Controller for managing BluetoothDevice details.
 * This controller is used for the BluetoothDevice details page.
 * It is used to load the BluetoothDevice from the database and to save changes to the database.
 * It is also used to reload the BluetoothDevice from the database.
 * @see BluetoothDevice
 * @see BluetoothDeviceService
 */
@Component
@Scope("view")
public class BluetoothDetailController implements Serializable {

    @Autowired
    private transient BluetoothDeviceService bluetoothDeviceService;

    private BluetoothDevice bluetoothDevice;

    /**
     * Sets the BluetoothDevice to be managed by this controller
     * @param bluetoothDevice The BluetoothDevice to manage
     */
    public void setBluetoothDevice(BluetoothDevice bluetoothDevice){
        this.bluetoothDevice = bluetoothDevice;

    }

    /**
     * Returns the BluetoothDevice managed by this controller
     * @return The BluetoothDevice managed by this controller
     */
    public BluetoothDevice getBluetoothDevice(){
        return this.bluetoothDevice;
    }


    /**
     *  Reloads the BluetoothDevice from the database
     */
    public void doReloadBluetoothDevice(){
        try {
            bluetoothDevice = bluetoothDeviceService.loadBluetoothDevice(bluetoothDevice.getBluetoothDeviceId());
        }catch (Exception e){
            FacesMessage msg = new FacesMessage("Error while reloading BluetoothDevice", e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    /**
     * Saves changes to the BluetoothDevice to the database
     */
    public void doSaveBluetoothDevice(){
        if(this.bluetoothDevice != null){
            this.bluetoothDeviceService.saveBluetoothDevice(this.bluetoothDevice);
        }
    }

    /**
     * Deletes the BluetoothDevice from the database
     */
    public void doDeleteBluetoothDevice(){
        if(this.bluetoothDevice != null){
            this.bluetoothDeviceService.deleteBluetoothDevice(this.bluetoothDevice);
        }
    }

    /**
     * Set the BluetoothDevice from its ID
     * @param id The ID of the BluetoothDevice
     */
    public void setBluetoothDeviceServiceFromBluetoothDeviceId(Long id){
        this.bluetoothDevice = this.bluetoothDeviceService.loadBluetoothDevice(id);
    }
}
