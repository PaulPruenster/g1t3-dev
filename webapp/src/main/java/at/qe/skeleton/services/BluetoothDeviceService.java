package at.qe.skeleton.services;

import at.qe.skeleton.model.AccessPoint;
import at.qe.skeleton.model.BluetoothDevice;
import at.qe.skeleton.repositories.BluetoothDeviceRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * Service for accessing and manipulating bluetooth devices.
 * Can be used for generic CRUD operations on bluetooth devices.
 * @see BluetoothDevice
 * @see BluetoothDeviceRepository
 */
@Component
@Scope("application")
public class BluetoothDeviceService {

    @Autowired
    private BluetoothDeviceRepository bluetoothDeviceRepository;


    /**
     * Returns a list of all bluetooth devices.
     * @return
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public Collection<BluetoothDevice> getAllBluetoothDevices() {
        return bluetoothDeviceRepository.findAll();
    }

    /**
     * Loads a bluetooth device by its bluetooth device ID.
     * @param bluetoothDeviceId
     * @return
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public BluetoothDevice loadBluetoothDevice(Long bluetoothDeviceId) {
        return bluetoothDeviceRepository.findFirstByBluetoothDeviceId(bluetoothDeviceId);
    }


    /**
     * Saves a bluetooth device.
     * @param bluetoothDevice the bluetooth device to save
     * @return the updated bluetooth device
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public BluetoothDevice saveBluetoothDevice(BluetoothDevice bluetoothDevice) {
        return bluetoothDeviceRepository.save(bluetoothDevice);
    }


    /**
     * Deletes a bluetooth device.
     * @param bluetoothDevice the bluetooth device to delete
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteBluetoothDevice(BluetoothDevice bluetoothDevice) {
        bluetoothDeviceRepository.delete(bluetoothDevice);
    }


    /**
    * Returns a list of all bluetooth devices.
    * @return Collection of all bluetooth devices
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public Collection<BluetoothDevice> getAllBluetoothDevicesByAccessPointId(Long accessPointId) {
        return bluetoothDeviceRepository.findAllByAccessPointId(accessPointId);
    }

    /**
     * Deletes all bluetooth devices.
     * @param accessPoint the access point to delete all bluetooth devices from
     */
    @Transactional
    @PreAuthorize("hasAuthority('ACCESS_POINT') or hasAuthority('ADMIN')")
    public void deleteAllByAccessPoint(AccessPoint accessPoint) {
        bluetoothDeviceRepository.deleteAllByAccessPoint(accessPoint);
    }


    @Transactional
    @PreAuthorize("hasAuthority('ACCESS_POINT') or hasAuthority('ADMIN')")
    public void deleteAllByAccessPointAndShouldConnectIsFalse(AccessPoint accessPoint) {
        bluetoothDeviceRepository.deleteAllByAccessPointAndShouldConnectIsFalse(accessPoint);
    }



    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('ACCESS_POINT')")
    public BluetoothDevice findFirstByAccessPointAndShouldConnect(AccessPoint accessPoint, boolean shouldConnect) {
        return bluetoothDeviceRepository.findFirstByAccessPointAndShouldConnect(accessPoint, shouldConnect);
    }

}
