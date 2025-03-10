package at.qe.skeleton.model;

import jakarta.persistence.*;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;


/**
 * Entity representing a sensor.
 * It contains a {@link SensorStation}.
 * It also contains a name, a {@link SensorTyp}, a {@link SensorUnit}, an upper and a lower limit.
 * It is used to store the sensor data in the database.
 */
@Entity
public class Sensor  implements Persistable<Long>, Serializable, Comparable<Sensor>{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_gen_sensor")
    @SequenceGenerator(name = "id_gen_sensor", initialValue = 100)
    @Column
    private Long sensorId;

    @Column
    private String name;

    @Enumerated(EnumType.STRING)
    private SensorTyp sensorTyp;

    @Enumerated(EnumType.STRING)
    private SensorUnit sensorUnit;

    @Column
    private double upperLimit;

    @Column
    private double lowerLimit;

    @Column
    private boolean currentThresholdWarning;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sensorStationId")
    private SensorStation sensorStation;

    public Sensor() {
        // Empty Constructor
    }

    public Long getSensorId() {
        return sensorId;
    }

    public void setSensorId(Long sensorId) {
        this.sensorId = sensorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SensorTyp getSensorTyp() {
        return sensorTyp;
    }

    public void setSensorTyp(SensorTyp sensorTyp) {
        this.sensorTyp = sensorTyp;
    }

    public SensorUnit getSensorUnit() {
        return sensorUnit;
    }

    public void setSensorUnit(SensorUnit sensorUnit) {
        this.sensorUnit = sensorUnit;
    }

    public double getUpperLimit() {
        return upperLimit;
    }

    public void setUpperLimit(double upperLimit) {
        this.upperLimit = upperLimit;
    }

    public double getLowerLimit() {
        return lowerLimit;
    }

    public void setLowerLimit(double lowerLimit) {
        this.lowerLimit = lowerLimit;
    }

    public boolean isCurrentThresholdWarning() {
        return currentThresholdWarning;
    }

    public void setCurrentThresholdWarning(boolean currentThresholdWarning) {
        this.currentThresholdWarning = currentThresholdWarning;
    }

    public SensorStation getSensorStation() {
        return sensorStation;
    }

    public void setSensorStation(SensorStation sensorStation) {
        this.sensorStation = sensorStation;
    }

    @Override
    public int compareTo(Sensor other) {
        return Comparator.comparing(Sensor::getSensorId).compare(this, other);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sensor sensor)) return false;
        return Double.compare(sensor.upperLimit, upperLimit) == 0 &&
                Double.compare(sensor.lowerLimit, lowerLimit) == 0 &&
                currentThresholdWarning == sensor.currentThresholdWarning &&
                Objects.equals(sensorId, sensor.sensorId) &&
                Objects.equals(name, sensor.name) &&
                sensorTyp == sensor.sensorTyp &&
                sensorUnit == sensor.sensorUnit &&
                Objects.equals(sensorStation, sensor.sensorStation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sensorId, name, sensorTyp, sensorUnit, upperLimit, lowerLimit, currentThresholdWarning, sensorStation);
    }

    @Override
    public Long getId() {
        return this.sensorId;
    }

    @Override
    public boolean isNew() {
        return this.sensorId == null;
    }


    @Override
    public String toString() {
        return "at.qe.skeleton.model.Sensor{" +
                "sensorId=" + sensorId +
                ", name='" + name + '\'' +
                ", sensorTyp=" + sensorTyp +
                ", sensorUnit=" + sensorUnit +
                ", upperLimit=" + upperLimit +
                ", lowerLimit=" + lowerLimit +
                ", currentThresholdWarning=" + currentThresholdWarning +
                ", sensorStation=" + sensorStation +
                '}';
    }
}
