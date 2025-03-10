package at.qe.skeleton.model;

import jakarta.persistence.*;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.util.Objects;


/**
 * Entity representing a bluetooth device.
 * It contains a name and a reference to the {@link AccessPoint} it is connected to.
 * It stands for the bluetooth device that can be connected to the access point
 */
@Entity
public class BluetoothDevice implements Serializable, Persistable<Long>, Comparable<BluetoothDevice> {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_gen_bluetoothDevice")
    @SequenceGenerator(name = "id_gen_bluetoothDevice", initialValue = 100)
    private Long bluetoothDeviceId;

    @Column
    private String deviceName;

    @Column
    private boolean shouldConnect;

    @Column
    private boolean connected;

    @ManyToOne()
    private AccessPoint accessPoint;

    public BluetoothDevice() {
    }


    public BluetoothDevice(String deviceName, AccessPoint accessPoint) {
        this.deviceName = deviceName;
        this.accessPoint = accessPoint;
    }

    public Long getBluetoothDeviceId() {
        return bluetoothDeviceId;
    }

    public void setBluetoothDeviceId(Long bluetoothDeviceId) {
        this.bluetoothDeviceId = bluetoothDeviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public AccessPoint getAccessPoint() {
        return accessPoint;
    }

    public void setAccessPoint(AccessPoint accessPoint) {
        this.accessPoint = accessPoint;
    }

    public boolean isShouldConnect() {
        return shouldConnect;
    }

    public void setShouldConnect(boolean shouldConnect) {
        this.shouldConnect = shouldConnect;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BluetoothDevice that)) return false;
        return Objects.equals(bluetoothDeviceId, that.bluetoothDeviceId) && Objects.equals(deviceName, that.deviceName) && Objects.equals(accessPoint, that.accessPoint);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bluetoothDeviceId, deviceName, accessPoint);
    }


    @Override
    public int compareTo(BluetoothDevice o) {
        return 0;
    }

    @Override
    public Long getId() {
        return this.bluetoothDeviceId;
    }

    @Override
    public boolean isNew() {
        return this.bluetoothDeviceId == null;
    }
}
