package at.qe.skeleton.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class SensorStationTest {

    @Test
    void testConstructor() {
        SensorStation sensorStation = new SensorStation();
        Assertions.assertNotNull(sensorStation);
    }

    @Test
    void testEquals() {
        Userx user1 = new Userx();
        user1.setUsername("user1");
        Userx user2 = new Userx();
        user2.setUsername("user2");

        SensorStation sensorStation1 = new SensorStation();
        sensorStation1.setSensorStationId(1L);
        sensorStation1.setName("sensorStation1");
        sensorStation1.setGaertner(user1);

        SensorStation sensorStation2 = new SensorStation();
        sensorStation2.setSensorStationId(1L);
        sensorStation2.setName("sensorStation1");
        sensorStation2.setGaertner(user1);

        SensorStation sensorStation3 = new SensorStation();
        sensorStation3.setSensorStationId(1L);
        sensorStation3.setName("sensorStation1");
        sensorStation3.setGaertner(user2);

        Assertions.assertEquals(sensorStation1, sensorStation2);
        Assertions.assertEquals(sensorStation2, sensorStation1);
        Assertions.assertNotEquals(sensorStation1, sensorStation3);
    }

    @Test
    void testHashCode() {
        SensorStation sensorStation1 = new SensorStation();
        sensorStation1.setSensorStationId(1L);

        SensorStation sensorStation2 = new SensorStation();
        sensorStation2.setSensorStationId(1L);

        Assertions.assertEquals(sensorStation1.hashCode(), sensorStation2.hashCode());

        SensorStation sensorStation3 = new SensorStation();

        Assertions.assertNotEquals(sensorStation1.hashCode(), sensorStation3.hashCode());
    }
}
