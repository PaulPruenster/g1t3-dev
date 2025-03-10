package at.qe.skeleton.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DashboardItemTest {

    @Test
    void testEqualsAndHashCode() {
        DashboardItem item1 = new DashboardItem();
        DashboardItem item2 = new DashboardItem();
        item1.setDashboardItemId(1L);
        item2.setDashboardItemId(1L);
        SensorStation station1 = new SensorStation();
        SensorStation station2 = new SensorStation();
        station1.setSensorStationId(1L);
        station2.setSensorStationId(1L);
        item1.setSensorStation(station1);
        item2.setSensorStation(station2);
        item1.setItemType(SensorTyp.HUMIDITY);
        item2.setItemType(SensorTyp.HUMIDITY);

        Assertions.assertEquals(item1, item2);
        Assertions.assertEquals(item1.hashCode(), item2.hashCode());
    }

    @Test
    void testCompareTo() {
        DashboardItem item1 = new DashboardItem();
        item1.setDashboardItemId(1L);
        DashboardItem item2 = new DashboardItem();
        item2.setDashboardItemId(2L);

        Assertions.assertTrue(item1.compareTo(item2) < 0);
        Assertions.assertTrue(item2.compareTo(item1) > 0);
        Assertions.assertEquals(0, item1.compareTo(item1));
    }

    @Test
    void testIsNew() {
        DashboardItem item1 = new DashboardItem();
        item1.setDashboardItemId(1L);
        DashboardItem item2 = new DashboardItem();

        Assertions.assertFalse(item1.isNew());
        Assertions.assertTrue(item2.isNew());
    }

}

