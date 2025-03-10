package at.qe.skeleton.api.controllers;

import at.qe.skeleton.api.exceptions.AccessPointNotFoundException;
import at.qe.skeleton.model.*;
import at.qe.skeleton.services.AccessPointService;
import at.qe.skeleton.services.BluetoothDeviceService;
import at.qe.skeleton.services.SensorService;
import at.qe.skeleton.services.SensorStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class AccessPointController {

    @Autowired
    private AccessPointService accessPointService;

    @Autowired
    private SensorStationService sensorStationService;

    @Autowired
    private SensorService sensorService;


    @Autowired
    private BluetoothDeviceService bluetoothDeviceService;

    /**
     * This method is used to get an access point by id
     * url: /api/accessPoint/{id}
     * @param id of the access point
     * @return the access point
     * @throws AccessPointNotFoundException if the access point is not found
     */
    @GetMapping("/api/accessPoint/{id}")
    public AccessPoint getAccessPoint(@PathVariable Long id) throws AccessPointNotFoundException {
            return accessPointService.loadAccessPoint(id);
    }

    /**
     * This method is used to get all access points
     * url: /api/accessPoint
     * @return all access points
     */
    @GetMapping("/api/accessPoint")
    public Iterable<AccessPoint> getAllAccessPoints()
    {
        return accessPointService.getAllAccessPoints();
    }

    /**
     * This method is used to add a new access point
     * url: /api/accessPoint/new
     * @return the id of the new access point
     */
    @PostMapping("/api/accessPoint/new")
    public Long getNewAccessPoint()
    {
        AccessPoint accessPoint = new AccessPoint();
        accessPoint.setName("new AccessPoint");
        AccessPoint savedAccessPoint = accessPointService.saveAccessPoint(accessPoint);
        return savedAccessPoint.getAccessPointId();
    }

    /**
     * This method is used to add a new sensor station to an access point
     * url: /api/accessPoint/{id}/newSensorStation
     * @param id of the access point
     * @return the id of the new sensor station
     * @throws AccessPointNotFoundException if the access point is not found
     */
    @PostMapping("/api/accessPoint/{id}/newSensorStation")
    public Long getNewAccessPoint(@PathVariable Long id) throws AccessPointNotFoundException {
        SensorStation sensorStation = new SensorStation();
        sensorStation.setName("new SensorStation");
        AccessPoint accessPoint = accessPointService.loadAccessPoint(id);
        sensorStation.setAccessPoint(accessPoint);
        sensorStation.setUpdateInterval(30);
        SensorStation savedSensorStation = sensorStationService.saveSensorStation(sensorStation);
        return savedSensorStation.getSensorStationId();
    }

    /**
     * This method is used to add a new sensor to a sensor station
     * url: /api/accessPoint/{id}/newSensor
     * @param id of the sensor station
     * @return the id of the new sensor
     * @throws AccessPointNotFoundException if the access point is not found
     */
    @GetMapping("/api/accessPoint/{id}/makeConnection")
    public boolean makeConnection(@PathVariable Long id) throws AccessPointNotFoundException {
        AccessPoint accessPoint = accessPointService.loadAccessPoint(id);
        return accessPoint.isMakeConnection();
    }

    /**
     *  This method is used to add a new sensor to a sensor station
     * @param id of the sensor station
     * @throws AccessPointNotFoundException if the access point is not found
     */
    @PostMapping("/api/accessPoint/{id}/connectionSuccess")
    public void deleteBluetoothDevice(@PathVariable Long id) throws AccessPointNotFoundException {
        AccessPoint accessPoint = accessPointService.loadAccessPoint(id);
        this.bluetoothDeviceService.deleteAllByAccessPointAndShouldConnectIsFalse(accessPoint);
        BluetoothDevice bluetoothDevice = bluetoothDeviceService.findFirstByAccessPointAndShouldConnect(accessPoint, true);
        if(bluetoothDevice != null){
            bluetoothDevice.setShouldConnect(false);
            bluetoothDevice.setConnected(true);
            bluetoothDeviceService.saveBluetoothDevice(bluetoothDevice);
        }
        accessPoint.setMakeConnection(false);
        accessPointService.saveAccessPoint(accessPoint);

    }

    @PostMapping("/api/accessPoint/{id}/addBluetoothDevice")
    public void getNewBluetoothDevice( @PathVariable Long id, @RequestBody List<String> deviceName) throws AccessPointNotFoundException {
        AccessPoint accessPoint = accessPointService.loadAccessPoint(id);
        bluetoothDeviceService.deleteAllByAccessPointAndShouldConnectIsFalse(accessPoint);
        BluetoothDevice shouldConnect = bluetoothDeviceService.findFirstByAccessPointAndShouldConnect(accessPoint, true);

        for(String name : deviceName){
            if (shouldConnect != null && name.equals(shouldConnect.getDeviceName())) continue;

            BluetoothDevice bluetoothDevice = new BluetoothDevice();
            bluetoothDevice.setDeviceName(name);
            bluetoothDevice.setAccessPoint(accessPointService.loadAccessPoint(id));
            bluetoothDeviceService.saveBluetoothDevice(bluetoothDevice);
        }
    }


    @GetMapping("/api/accessPoint/{id}/shouldConnectBluetoothDevice")
    public String shouldConnectBluetoothDevice(@PathVariable Long id) throws AccessPointNotFoundException {
        AccessPoint accessPoint = accessPointService.loadAccessPoint(id);
        BluetoothDevice bluetoothDevice =  bluetoothDeviceService.findFirstByAccessPointAndShouldConnect(accessPoint, true);
        if(bluetoothDevice == null){
            return null;
        }
        return bluetoothDevice.getDeviceName();
    }
}
