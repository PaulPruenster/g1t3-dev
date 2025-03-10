package at.qe.skeleton.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class SensorStationImageTest {
    private SensorStation sensorStation;

    @BeforeEach
    void setUp() {
        sensorStation = new SensorStation();
        sensorStation.setSensorStationId(1L);

        SensorStationImage image = new SensorStationImage();
        image.setImageId(1L);
        image.setSensorStation(sensorStation);
        image.setName("testImage");
        image.setType("jpg");
        image.setImageData(new byte[] {0x00, 0x01, 0x02, 0x03});
    }

    @Test
    void testEqualsAndHashCode() {
        SensorStationImage image1 = new SensorStationImage();
        image1.setImageId(1L);
        image1.setSensorStation(sensorStation);
        image1.setName("testImage");
        image1.setType("jpg");
        image1.setImageData(new byte[]{0x00, 0x01, 0x02, 0x03});

        SensorStationImage image2 = new SensorStationImage();
        image2.setImageId(1L);
        image2.setSensorStation(sensorStation);
        image2.setName("testImage");
        image2.setType("jpg");
        image2.setImageData(new byte[]{0x00, 0x01, 0x02, 0x03});

        assertEquals(image1, image2);
        assertEquals(image1.hashCode(), image2.hashCode());

        image2.setName("testImage2");

        assertNotEquals(image1, image2);
        assertNotEquals(image1.hashCode(), image2.hashCode());
    }

    @Test
    void testEquals() {
        SensorStationImage image1 = new SensorStationImage();
        image1.setImageId(1L);
        image1.setSensorStation(sensorStation);
        image1.setName("testImage");
        image1.setType("jpg");
        image1.setImageData(new byte[]{0x00, 0x01, 0x02, 0x03});

        SensorStationImage image2 = new SensorStationImage();
        image2.setImageId(1L);
        image2.setSensorStation(sensorStation);
        image2.setName("testImage");
        image2.setType("jpg");
        image2.setImageData(new byte[]{0x00, 0x01, 0x02, 0x03});

        assertEquals(image1, image2);
        assertEquals(image1.hashCode(), image2.hashCode());

        image2.setName("testImage2");

        assertNotEquals(image1, image2);
        assertNotEquals(image1.hashCode(), image2.hashCode());
    }
}
