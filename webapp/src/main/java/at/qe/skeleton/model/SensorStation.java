package at.qe.skeleton.model;

import jakarta.persistence.*;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;


/**
 * Entity representing a sensor station.
 * It contains a {@link Userx}.
 * It also contains a name and an update interval.
 * It contains a list of {@link Sensor}s, representing the sensors of the sensor station.
 * It is used to store the sensorStation data in the database.
 *
 */
@Entity
public class SensorStation implements Persistable<Long>, Serializable, Comparable<SensorStation>  {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_gen_sensorStation")
    @SequenceGenerator(name = "id_gen_sensorStation", initialValue = 100)
    @Column
    private Long sensorStationId;

    @Column
    private String name;

    @Column
    private int updateInterval;

    @ManyToOne()
    @JoinColumn(nullable = true)
    private Userx gaertner;

    @OneToMany(mappedBy = "sensorStation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Sensor> sensors;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "access_point_id")
    private AccessPoint accessPoint;


    public int getUpdateInterval() {
        return updateInterval;
    }

    public void setUpdateInterval(int updateInterval) {
        this.updateInterval = updateInterval;
    }

    public Long getSensorStationId() {
        return sensorStationId;
    }

    public void setSensorStationId(Long sensorStationId) {
        this.sensorStationId = sensorStationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Userx getGaertner() {
        return gaertner;
    }

    public void setGaertner(Userx gaertner) {
        this.gaertner = gaertner;
    }

    public List<Sensor> getSensors() {
        return sensors;
    }

    public void setSensors(List<Sensor> sensors) {
        this.sensors = sensors;
    }


    public AccessPoint getAccessPoint() {
        return accessPoint;
    }

    public void setAccessPoint(AccessPoint accessPoint) {
        this.accessPoint = accessPoint;
    }

    public SensorStation() {
        this.sensors = new ArrayList<>();
    }


    @Override
    public Long getId() {
        return this.sensorStationId;
    }

    @Override
    public boolean isNew() {
        return this.sensorStationId == null;
    }


    @Override
    public int compareTo(SensorStation other) {
        return Comparator.comparing(SensorStation::getSensorStationId).compare(this, other);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SensorStation that)) return false;
        return Objects.equals(sensorStationId, that.sensorStationId) &&
                Objects.equals(name, that.name) &&
                Objects.equals(gaertner, that.gaertner) &&
                Objects.equals(updateInterval, that.updateInterval);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sensorStationId);
    }


    @Override
    public String toString() {
        return "at.qe.skeleton.model.SensorStation{" +
                "sensorStationId=" + sensorStationId +
                ", name='" + name + '\'' +
                ", gaertner=" + gaertner +
                //", sensors=" + sensors +
                //", accessPoint=" + accessPoint +
                '}';
    }
}
