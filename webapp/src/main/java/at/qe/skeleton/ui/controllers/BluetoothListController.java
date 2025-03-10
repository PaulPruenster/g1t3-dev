package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.model.BluetoothDevice;
import at.qe.skeleton.services.BluetoothDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;

/**
 * Controller for managing BluetoothDevice lists.
 * This controller is used for the BluetoothDevice list page.
 * It is used to load the list of BluetoothDevices from the database.
 * @see BluetoothDevice
 * @see BluetoothDeviceService
 * @see BluetoothDetailController
 */
@Component
@Scope("view")
public class BluetoothListController  implements Serializable {

    @Autowired
    private transient BluetoothDeviceService bluetoothDeviceService;

    private BluetoothDevice selectedDevice;

    /**
     * Returns a list of all bluetooth devices.
     * @return A list of all bluetooth devices
     */
    public Collection<BluetoothDevice> getAllBluetoothDevices() {
        return bluetoothDeviceService.getAllBluetoothDevices();
    }

    /**
     * Returns a list of devices sent by the given accessPoint
     * @param accessPointId id of the given accessPoint
     * @return list of devices the accessPoints sees
     */
    public Collection<BluetoothDevice> getAllBluetoothDevicesByAccessPointId(long accessPointId) {
        return bluetoothDeviceService.getAllBluetoothDevicesByAccessPointId(accessPointId);
    }

    public void connectDevice(BluetoothDevice device) {
        if (device != null) {
            device.setShouldConnect(!device.isShouldConnect());
            bluetoothDeviceService.saveBluetoothDevice(device);

            if (device.isShouldConnect())
                this.selectedDevice = device;
            else
                this.selectedDevice = null;
        }
    }

    public BluetoothDevice getSelectedDevice() {
        return selectedDevice;
    }

    public void setSelectedDevice(BluetoothDevice selectedDevice) {
        this.selectedDevice = selectedDevice;
    }
}
