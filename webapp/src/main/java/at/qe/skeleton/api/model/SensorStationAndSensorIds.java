package at.qe.skeleton.api.model;

import java.io.Serializable;

public class SensorStationAndSensorIds implements Serializable {

    private Long sensorStationId;

    private Long fototransistorId;

    private Long luftsensorId;

    private Long hygrometerId;


    public SensorStationAndSensorIds() {
    }

    public SensorStationAndSensorIds(Long sensorStationId, Long fototransistorid, Long luftsensorId, Long hygrometerId) {
        this.sensorStationId = sensorStationId;
        this.fototransistorId = fototransistorid;
        this.luftsensorId = luftsensorId;
        this.hygrometerId = hygrometerId;
    }

    public Long getSensorStationId() {
        return sensorStationId;
    }

    public void setSensorStationId(Long sensorStationId) {
        this.sensorStationId = sensorStationId;
    }

    public Long getFototransistorId() {
        return fototransistorId;
    }

    public void setFototransistorId(Long fototransistorId) {
        this.fototransistorId = fototransistorId;
    }

    public Long getLuftsensorId() {
        return luftsensorId;
    }

    public void setLuftsensorId(Long luftsensorId) {
        this.luftsensorId = luftsensorId;
    }

    public Long getHygrometerId() {
        return hygrometerId;
    }

    public void setHygrometerId(Long hygrometerId) {
        this.hygrometerId = hygrometerId;
    }
}
