package at.qe.skeleton.model;


import jakarta.persistence.*;
import org.springframework.data.domain.Persistable;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.*;

/**
 * Entity representing an access point.
 * It contains a list of {@link SensorStation}s.
 * it stands for the raspberry pi that is connected to the sensors
 */
@Entity
public class AccessPoint implements Persistable<Long>, Serializable, Comparable<AccessPoint>{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_gen_accessPoint")
    @SequenceGenerator(name = "id_gen_accessPoint", initialValue = 100)
    @Column
    private Long accessPointId;

    @Column
    private String name;

    @Column
    private boolean makeConnection;

    @OneToMany(mappedBy = "accessPoint", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SensorStation> sensorStations;


    public AccessPoint() {
        // Empty Constructor
    }



    public Long getAccessPointId() {
        return accessPointId;
    }

    public void setAccessPointId(Long accessPointId) {
        this.accessPointId = accessPointId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SensorStation> getSensorStations() {
        return sensorStations;
    }

    public void setSensorStations(List<SensorStation> sensorStations) {
        this.sensorStations = sensorStations;
    }

    @Override
    public int compareTo(@NotNull AccessPoint o) {
        return Comparator.comparing(AccessPoint::getAccessPointId).compare(this, o);
    }

    @Override
    public Long getId() {
        return this.accessPointId;
    }

    public boolean isMakeConnection() {
        return makeConnection;
    }

    public void setMakeConnection(boolean makeConnection) {
        this.makeConnection = makeConnection;
    }

    @Override
    public boolean isNew() {
        return this.accessPointId == null;
    }


    @Override
    public int hashCode() {
        return Objects.hash(this.accessPointId, this.name);
    }



    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        AccessPoint other = (AccessPoint) obj;
        return Objects.equals(this.accessPointId, other.getAccessPointId()) &&
                Objects.equals(this.name, other.getName());
    }


    @Override
    public String toString() {
        return "at.qe.skeleton.model.AccessPoint{" +
                "accessPointId=" + accessPointId +
                ", name='" + name + '\'' +
                //", sensorStations=" + sensorStations +
                '}';
    }
}
