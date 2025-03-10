package at.qe.skeleton.api.controllers;

import at.qe.skeleton.services.AccessPointService;
import at.qe.skeleton.services.BluetoothDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class BluetoothDeviceController {


    @Autowired
    private BluetoothDeviceService bluetoothDeviceService;

    @Autowired
    private AccessPointService accessPointService;



}
