package at.qe.skeleton.model;

import jakarta.persistence.*;
import org.springframework.data.domain.Persistable;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;


/**
 * Entity representing an item on the dashboard.
 * It contains a {@link SensorStation} and a {@link Userx}.
 * It also contains a {@link SensorTyp} to determine the type of the item.
 * It is used to display the sensor data on the dashboard.
 */
@Entity
public class DashboardItem implements Persistable<Long>, Serializable, Comparable<DashboardItem>{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_gen_dashboard")
    @SequenceGenerator(name = "id_gen_dashboard", initialValue = 100)
    @Column(nullable = false, unique = true)
    private Long dashboardItemId;

    @ManyToOne
    @JoinColumn(name = "sensor_station_id")
    private SensorStation sensorStation;

    @ManyToOne
    private Userx user;

    @Enumerated(EnumType.STRING)
    private SensorTyp itemType;

    public Long getDashboardItemId() {
        return dashboardItemId;
    }

    public void setDashboardItemId(Long dashboardItemId) {
        this.dashboardItemId = dashboardItemId;
    }

    public SensorStation getSensorStation() {
        return sensorStation;
    }

    public void setSensorStation(SensorStation sensorStation) {
        this.sensorStation = sensorStation;
    }

    public Userx getUser() {
        return user;
    }

    public void setUser(Userx user) {
        this.user = user;
    }

    public SensorTyp getItemType() {
        return itemType;
    }

    public void setItemType(SensorTyp itemType) {
        this.itemType = itemType;
    }

    @Override
    public int compareTo(@NotNull DashboardItem o) {
        return Comparator.comparing(DashboardItem::getDashboardItemId).compare(this, o);
    }

    @Override
    public Long getId() {
            return this.dashboardItemId;
            }

    @Override
    public boolean isNew() {
            return this.dashboardItemId == null;
            }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DashboardItem that = (DashboardItem) o;
        return Objects.equals(dashboardItemId, that.dashboardItemId) && Objects.equals(sensorStation, that.sensorStation) && itemType == that.itemType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(dashboardItemId, sensorStation, itemType);
    }

    @Override
    public String toString() {
        return "DashboardItem{" +
                "dashboardItemId=" + dashboardItemId +
                ", sensorStation=" + sensorStation +
                ", itemType=" + itemType +
                '}';
    }
}


