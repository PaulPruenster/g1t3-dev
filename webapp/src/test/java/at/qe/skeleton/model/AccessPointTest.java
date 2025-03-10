package at.qe.skeleton.model;

import org.junit.jupiter.api.Test;

import javax.persistence.Access;

import static org.junit.jupiter.api.Assertions.*;

class AccessPointTest {

    /**
     * Test if the compareTo method works correctly
     */
    @Test
    void testCompareTo() {
        AccessPoint accessPoint1 = new AccessPoint();
        accessPoint1.setAccessPointId(1L);
        AccessPoint accessPoint2 = new AccessPoint();
        accessPoint2.setAccessPointId(2L);

        assertEquals(-1, accessPoint1.compareTo(accessPoint2));
        assertEquals(0, accessPoint1.compareTo(accessPoint1));
        assertEquals(1, accessPoint2.compareTo(accessPoint1));

    }
/** Test if the equals method works correctly */
    @Test
    void testEquals() {
        AccessPoint accessPoint1 = new AccessPoint();
        accessPoint1.setAccessPointId(1L);
        accessPoint1.setName("Test");

        AccessPoint accessPoint2 = new AccessPoint();
        accessPoint2.setAccessPointId(2L);
        accessPoint2.setName("Test");

        AccessPoint accessPoint3 = new AccessPoint();
        accessPoint3.setAccessPointId(1L);
        accessPoint3.setName("Test");

        AccessPoint accessPoint4 = new AccessPoint();
        accessPoint4.setAccessPointId(1L);
        accessPoint4.setName("Test2");

        assertNotEquals(accessPoint1, accessPoint2);
        assertEquals(accessPoint1, accessPoint1);
        assertEquals(accessPoint1, accessPoint3);
        assertNotEquals(accessPoint1, accessPoint4);

    }
}
