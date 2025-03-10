package at.qe.skeleton.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UserxRoleTest {

    @Test
    void testValues() {
        // Ensure that the values of the enum are correct
        assertEquals(UserRole.ADMIN, UserRole.valueOf("ADMIN"));
        //assertEquals(UserRole.GARTNER, UserRole.valueOf("STUDENT"));

        // Ensure that the values are returned to the correct order
        UserRole[] roles = UserRole.values();
        assertEquals(UserRole.ADMIN, roles[0]);
        //assertEquals(UserRole.GARTNER, roles[1]);
    }
}

