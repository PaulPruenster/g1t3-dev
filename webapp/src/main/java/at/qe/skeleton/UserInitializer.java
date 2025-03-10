package at.qe.skeleton;

import at.qe.skeleton.model.*;
import at.qe.skeleton.repositories.AccessPointRepository;
import at.qe.skeleton.repositories.SensorRepository;
import at.qe.skeleton.repositories.SensorStationRepository;
import at.qe.skeleton.repositories.UserxRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashSet;

@Component
public class UserInitializer implements ApplicationRunner {

    private final UserxRepository userRepository;

    private final SensorStationRepository sensorStationRepository;

    private final AccessPointRepository accessPointRepository;

    private final SensorRepository sensorRepository;

    public UserInitializer(UserxRepository userRepository, AccessPointRepository accessPointRepository, SensorStationRepository sensorStationRepository, SensorRepository sensorRepository) {
        this.userRepository = userRepository;
        this.accessPointRepository = accessPointRepository;
        this.sensorStationRepository = sensorStationRepository;
        this.sensorRepository = sensorRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        this.addUserx();
        this.addAccessPoint1();
    }

    private void addAccessPoint1(){
        AccessPoint accessPoint1 = this.accessPointRepository.findFirstByAccessPointId(100L);
        SensorStation sensorStation1 = this.sensorStationRepository.findFirstBySensorStationId(100L);

        Sensor senor1 =  this.sensorRepository.findFirstBySensorId(1L);
        Sensor senor2 =  this.sensorRepository.findFirstBySensorId(2L);
        Sensor senor3 =  this.sensorRepository.findFirstBySensorId(3L);
        Sensor senor4 =  this.sensorRepository.findFirstBySensorId(4L);
        Sensor senor5 =  this.sensorRepository.findFirstBySensorId(5L);
        Sensor senor6 =  this.sensorRepository.findFirstBySensorId(6L);
        Sensor senor7 =  this.sensorRepository.findFirstBySensorId(7L);

        if(accessPoint1 == null){

            accessPoint1 = new AccessPoint();
            accessPoint1.setAccessPointId(100L);
            accessPoint1.setName("Test AccessPoint");
            accessPoint1.setMakeConnection(false);
            this.accessPointRepository.save(accessPoint1);
        }
        if(sensorStation1 == null) {
            AccessPoint accessPoint = this.accessPointRepository.findFirstByAccessPointId(100L);
            sensorStation1 = new SensorStation();
            sensorStation1.setSensorStationId(100L);
            sensorStation1.setName("Test SensorStation");
            sensorStation1.setAccessPoint(accessPoint);
            this.sensorStationRepository.save(sensorStation1);
        }
        if(senor1 == null){
            Sensor sensor = new Sensor();
            sensor.setSensorId(100L);
            sensor.setName("Test Sensor 1");
            sensor.setSensorTyp(SensorTyp.TEMPERATURE);
            sensor.setSensorUnit(SensorUnit.CELSIUS);
            sensor.setSensorStation(sensorStation1);
            this.sensorRepository.save(sensor);
        }
        if(senor2 == null){
            Sensor sensor = new Sensor();
            sensor.setSensorId(101L);
            sensor.setName("Test Sensor 2");
            sensor.setSensorTyp(SensorTyp.HUMIDITY);
            sensor.setSensorUnit(SensorUnit.PERCENT);
            sensor.setSensorStation(sensorStation1);
            this.sensorRepository.save(sensor);
        }
        if(senor3 == null){
            Sensor sensor = new Sensor();
            sensor.setSensorId(102L);
            sensor.setName("Test Sensor 3");
            sensor.setSensorTyp(SensorTyp.PRESSURE);
            sensor.setSensorUnit(SensorUnit.PASCAL);
            sensor.setSensorStation(sensorStation1);
            this.sensorRepository.save(sensor);
        }
        if(senor4 == null){
            Sensor sensor = new Sensor();
            sensor.setSensorId(103L);
            sensor.setName("Test Sensor 4");
            sensor.setSensorTyp(SensorTyp.TEMPERATURE);
            sensor.setSensorUnit(SensorUnit.CELSIUS);
            sensor.setSensorStation(sensorStation1);
            this.sensorRepository.save(sensor);
        }
        if(senor5 == null){
            Sensor sensor = new Sensor();
            sensor.setSensorId(104L);
            sensor.setName("Test Sensor 5");
            sensor.setSensorTyp(SensorTyp.HUMIDITY);
            sensor.setSensorUnit(SensorUnit.PERCENT);
            sensor.setSensorStation(sensorStation1);
            this.sensorRepository.save(sensor);
        }
        if(senor6 == null){
            Sensor sensor = new Sensor();
            sensor.setSensorId(105L);
            sensor.setName("Test Sensor 6");
            sensor.setSensorTyp(SensorTyp.PRESSURE);
            sensor.setSensorUnit(SensorUnit.PASCAL);
            sensor.setSensorStation(sensorStation1);
            this.sensorRepository.save(sensor);
        }
        if(senor7 == null){
            Sensor sensor = new Sensor();
            sensor.setSensorId(106L);
            sensor.setName("Test Sensor 7");
            sensor.setSensorTyp(SensorTyp.PRESSURE);
            sensor.setSensorUnit(SensorUnit.PASCAL);
            sensor.setSensorStation(sensorStation1);
            this.sensorRepository.save(sensor);
        }

    }
    private void addUserx(){
        if (userRepository.findAll().isEmpty()) {
            Userx userx1 = new Userx();
            userx1.setUsername("admin");
            userx1.setPassword("$2y$10$P7im4OMw6hsnPWLpJ1nVKup1jlEFsLIek9D3lglmZv.Tq05GDEhMS");
            userx1.setFirstName("Admin");
            userx1.setLastName("Admin");
            userx1.setCreateDate(new Date());
            userx1.setCreateUser(null);
            userx1.setEnabled(true);
            HashSet<UserRole> rolesUserx1 = new HashSet<>();
            rolesUserx1.add(UserRole.ADMIN);
            rolesUserx1.add(UserRole.USER);
            userx1.setRoles(rolesUserx1);
            userx1.setCreateUser(userx1);
            this.userRepository.save(userx1);


            Userx userx2 = new Userx();
            userx2.setEnabled(true);
            userx2.setCreateUser(userx1);
            userx2.setUsername("gardener");
            userx2.setPassword("$2y$10$P7im4OMw6hsnPWLpJ1nVKup1jlEFsLIek9D3lglmZv.Tq05GDEhMS");
            userx2.setFirstName("Gardener");
            userx2.setLastName("Gardener");
            userx2.setCreateDate(new Date());
            HashSet<UserRole> rolesUserx2 = new HashSet<>();
            rolesUserx2.add(UserRole.GARDENER);
            rolesUserx2.add(UserRole.USER);
            userx2.setRoles(rolesUserx2);
            this.userRepository.save(userx2);

        }
    }
}
