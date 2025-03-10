package at.qe.skeleton.api.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SensorStationAndSensorIdsTest {

    @Test
    void testConstructor() {
        Long sensorStationId = 1L;
        Long fototransistorId = 2L;
        Long luftsensorId = 3L;
        Long hygrometerId = 4L;

        SensorStationAndSensorIds sensorStationAndSensorIds = new SensorStationAndSensorIds(sensorStationId, fototransistorId, luftsensorId, hygrometerId);

        assertEquals(sensorStationId, sensorStationAndSensorIds.getSensorStationId());
        assertEquals(fototransistorId, sensorStationAndSensorIds.getFototransistorId());
        assertEquals(luftsensorId, sensorStationAndSensorIds.getLuftsensorId());
        assertEquals(hygrometerId, sensorStationAndSensorIds.getHygrometerId());
    }
}

