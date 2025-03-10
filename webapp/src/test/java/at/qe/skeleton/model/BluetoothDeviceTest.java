package at.qe.skeleton.model;

import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class BluetoothDeviceTest {

    @Test
    void testConstructor() {
        String deviceName = "MyDevice";
        AccessPoint accessPoint = new AccessPoint();

        BluetoothDevice bluetoothDevice = new BluetoothDevice(deviceName, accessPoint);

        assertEquals(deviceName, bluetoothDevice.getDeviceName());
        assertEquals(accessPoint, bluetoothDevice.getAccessPoint());
        assertFalse(bluetoothDevice.isConnected());
    }

    @Test
    void testEquals() {
        AccessPoint accessPoint1 = new AccessPoint();

        BluetoothDevice device1 = new BluetoothDevice("Device 1", accessPoint1);
        BluetoothDevice device2 = new BluetoothDevice("Device 1", accessPoint1);
        BluetoothDevice device3 = new BluetoothDevice("Device 2", accessPoint1);

        // Test equal objects
        assertEquals(device1, device2);

        // Test unequal objects
        assertNotEquals(device1, device3);
    }

    @Test
    void testCompareTo() {
        BluetoothDevice bluetoothDevice = new BluetoothDevice();
        bluetoothDevice.setBluetoothDeviceId(1L);
        bluetoothDevice.setDeviceName("deviceName");
        BluetoothDevice bluetoothDevice1 = new BluetoothDevice();
        bluetoothDevice1.setBluetoothDeviceId(1L);
        bluetoothDevice1.setDeviceName("deviceName");
        assertEquals(0, bluetoothDevice.compareTo(bluetoothDevice1));
    }
}
