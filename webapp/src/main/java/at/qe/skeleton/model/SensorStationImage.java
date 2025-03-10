
package at.qe.skeleton.model;

import jakarta.persistence.*;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

/**
 * Entity representing a sensor station image.
 * It contains a {@link SensorStation}.
 * It also contains a name, a type and the image data.
 * It is used to store the sensor station images in the database.
 */
@Entity
public class SensorStationImage  implements Persistable<Long>, Serializable, Comparable<SensorStationImage>{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_gen")
    @SequenceGenerator(name = "id_gen", initialValue = 1)
    @Column(nullable = false, unique = true)
    private Long imageId;

    @ManyToOne()
    @JoinColumn(name = "sensorStationId")
    private SensorStation sensorStation;

    @Column
    private String name;

    @Column
    private String type;

    @Column(name = "imagedata", length = 5242880*8)
    private byte[] imageData;


    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public SensorStation getSensorStation() {
        return sensorStation;
    }

    public void setSensorStation(SensorStation sensorStation) {
        this.sensorStation = sensorStation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    @Override
    public int compareTo(SensorStationImage other) {
        return Comparator.comparing(SensorStationImage::getImageId).compare(this, other);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SensorStationImage that = (SensorStationImage) o;
        return Objects.equals(imageId, that.imageId) && Objects.equals(sensorStation, that.sensorStation) && Objects.equals(name, that.name) && Objects.equals(type, that.type) && Arrays.equals(imageData, that.imageData);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(imageId, sensorStation, name, type);
        result = 31 * result + Arrays.hashCode(imageData);
        return result;
    }

    @Override
    public Long getId() {
        return this.imageId;
    }

    @Override
    public boolean isNew() {
        return this.imageId == null;
    }

    @Override
    public String toString() {
        return "SensorStationImage{" +
                "imageId=" + imageId +
                ", sensorStation=" + sensorStation +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", imageData=" + Arrays.toString(imageData) +
                '}';
    }
}
