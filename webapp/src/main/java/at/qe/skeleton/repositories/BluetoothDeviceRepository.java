package at.qe.skeleton.repositories;

import at.qe.skeleton.model.AccessPoint;
import at.qe.skeleton.model.BluetoothDevice;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

/**
 * Repository for managing a {@link BluetoothDevice} entities.
 */
public interface BluetoothDeviceRepository extends AbstractRepository<BluetoothDevice, Long>{

    /**
     * Finds the first bluetooth device by its bluetooth device ID.
     * @param bluetoothDeviceId the ID to search for
     * @return the found bluetooth device
     */
    BluetoothDevice findFirstByBluetoothDeviceId(Long bluetoothDeviceId);


    /**
     * Finds all bluetooth devices by their access point ID.
     * @param accessPointId the access point ID to search for
     * @return  the found bluetooth devices
     */
    Collection<BluetoothDevice> findAllByAccessPointId(Long accessPointId);


    /**
     * Deletes a bluetooth device by its bluetooth device ID.
     * @param bluetoothDeviceId the ID to search for
     */
    void deleteByBluetoothDeviceId(Long bluetoothDeviceId);


    /**
     * Deletes all bluetooth devices.
     */
    void deleteAll();

    /**
     * Deletes all bluetooth devices by their access point ID.
     * @param accessPoint the access point ID to search for
     */
    void deleteAllByAccessPoint(AccessPoint accessPoint);


    @Modifying
    @Query("DELETE FROM BluetoothDevice b WHERE b.accessPoint=?1")
    void deleteAllByAccessPointId(AccessPoint accessPoint);


    BluetoothDevice findFirstByAccessPointAndShouldConnect(AccessPoint accessPoint, boolean shouldConnect);

    @Modifying
    void deleteAllByAccessPointAndShouldConnectIsFalse(AccessPoint accessPointId);


}
