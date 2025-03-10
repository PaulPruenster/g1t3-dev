package at.qe.skeleton.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserxTest {

    private Userx user;

    @BeforeEach
    void setUp() {
        user = new Userx();
        user.setUsername("testUser");
        user.setPassword("testPassword");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setEmail("test.user@example.com");
        user.setPhone("+1234567890");
        user.setEnabled(true);

        Set<UserRole> roles = new HashSet<>();
        roles.add(UserRole.ADMIN);
        user.setRoles(roles);

        user.setCreateUser(user);
        user.setCreateDate(new Date());
    }

    @Test
    void testHashCode() {
        assertEquals(-1146745574, user.hashCode());
    }

    @Test
    void testEquals() {
        Userx user2 = new Userx();
        user2.setUsername("testUser");
        assertEquals(user, user2);

        user2.setUsername("otherUser");
        assertNotEquals(user, user2);
    }

    @Test
    void testCompareTo() {
        Userx user2 = new Userx();
        user2.setUsername("testUser2");
        assertTrue(user.compareTo(user2) < 0);
        assertTrue(user2.compareTo(user) > 0);

        Userx user3 = new Userx();
        user3.setUsername("testUser");
        assertEquals(0, user.compareTo(user3));
    }

}

