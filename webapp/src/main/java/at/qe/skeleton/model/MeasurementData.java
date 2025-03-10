package at.qe.skeleton.model;

import jakarta.persistence.*;
import org.springframework.data.domain.Persistable;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.Objects;


/**
 * Entity representing a measurement data.
 * It contains a {@link Sensor} and a {@link MeasurementLabel}.
 * It also contains a value and a timestamp.
 * It is used to store the sensor data in the database.
 */
@Entity
public class MeasurementData implements Persistable<Long>, Serializable, Comparable<MeasurementData>{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_gen_mes")
    @SequenceGenerator(name = "id_gen_mes", initialValue = 100)
    @Column(nullable = false, unique = true)
    private Long measurementId;

    @Column
    private double val;

    @Column
    private Timestamp measurementTimestamp;

    @ManyToOne
    @JoinColumn(name = "sensor_id")
    private Sensor sensor;

    @Enumerated(EnumType.STRING)
    private MeasurementLabel measurementLabel;

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }


    public Long getMeasurementId() {
        return measurementId;
    }

    public void setMeasurementId(Long measurementId) {
        this.measurementId = measurementId;
    }

    public double getVal() {
        return val;
    }

    public void setVal(double val) {
        this.val = val;
    }

    public Timestamp getMeasurementTimestamp() {
        return measurementTimestamp;
    }

    public void setMeasurementTimestamp(Timestamp measurementTimestamp) {
        this.measurementTimestamp = measurementTimestamp;
    }

    public MeasurementLabel getMeasurementLabel() {
        return measurementLabel;
    }

    public void setMeasurementLabel(MeasurementLabel measurementLabel) {
        this.measurementLabel = measurementLabel;
    }

    @Override
    public int compareTo(@NotNull MeasurementData o) {
        return Comparator.comparing(MeasurementData::getMeasurementId).compare(this, o);
    }

    @Override
    public Long getId() {
            return this.measurementId;
            }

    @Override
    public boolean isNew() {
            return this.measurementId == null;
            }


    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.measurementId);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        MeasurementData other = (MeasurementData) obj;
        return Double.compare(other.val, this.val) == 0 &&
                Objects.equals(this.measurementId, other.measurementId) &&
                Objects.equals(this.measurementTimestamp, other.measurementTimestamp) &&
                Objects.equals(this.sensor, other.sensor) &&
                Objects.equals(this.measurementLabel, other.measurementLabel);
    }

    @Override
    public String toString() {
        return "at.qe.skeleton.model.Measurement{" +
                "measurementId=" + measurementId +
                ", value=" + val +
                ", timestamp='" + measurementTimestamp + '\'' +
                ", sensor=" + sensor +
                ", measurementLabels=" + measurementLabel +
                '}';
    }


}


