package at.qe.skeleton.tests;

import at.qe.skeleton.model.Userx;
import at.qe.skeleton.model.UserRole;
import at.qe.skeleton.services.UserService;
import at.qe.skeleton.ui.beans.SessionInfoBean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.reflect.Field;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class SessionInfoBeanTest {

    @Mock
    private UserService userService;

    private SessionInfoBean sessionInfoBean;

    @BeforeEach
    void setUp() {
        sessionInfoBean = new SessionInfoBean();
        setField(sessionInfoBean, "userService", userService);
    }

    @Test
    void testLoggedIn() {
        Userx mockUser = new Userx();
        mockUser.setUsername("user1");
        when(userService.loadUser(mockUser.getUsername())).thenReturn(mockUser);
        Authentication auth = new UsernamePasswordAuthenticationToken(mockUser.getUsername(), null, Collections.singleton(() -> "USER"));
        SecurityContextHolder.getContext().setAuthentication(auth);
        assertTrue(sessionInfoBean.isLoggedIn(), "sessionInfoBean.isLoggedIn does not return true for authenticated user");
        assertEquals("user1", sessionInfoBean.getCurrentUserName(), "sessionInfoBean.getCurrentUserName does not return authenticated user's name");
        assertEquals(mockUser, sessionInfoBean.getCurrentUser(), "sessionInfoBean.getCurrentUser does not return authenticated user");
        assertEquals("USER", sessionInfoBean.getCurrentUserRoles(), "sessionInfoBean.getCurrentUserRoles does not return authenticated user's roles");
        assertTrue(sessionInfoBean.hasRole("USER"), "sessionInfoBean.hasRole does not return true for a role the authenticated user has");
        assertFalse(sessionInfoBean.hasRole("ADMIN"), "sessionInfoBean.hasRole does not return false for a role the authenticated user does not have");
    }

    @Test
    void testNotLoggedIn() {
        SecurityContextHolder.clearContext();
        assertFalse(sessionInfoBean.isLoggedIn(), "sessionInfoBean.isLoggedIn does return true for not authenticated user");
        assertEquals("", sessionInfoBean.getCurrentUserName(), "sessionInfoBean.getCurrentUserName does not return empty string when not logged in");
        assertNull(sessionInfoBean.getCurrentUser(), "sessionInfoBean.getCurrentUser does not return null when not logged in");
        assertEquals("", sessionInfoBean.getCurrentUserRoles(), "sessionInfoBean.getCurrentUserRoles does not return empty string when not logged in");
        for (UserRole role : UserRole.values()) {
            assertFalse(sessionInfoBean.hasRole(role.name()), "sessionInfoBean.hasRole does not return false for all possible roles");
        }
    }

    // Helper method to set a private field using reflection
    private void setField(Object targetObject, String fieldName, Object value) {
        try {
            Field field = targetObject.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(targetObject, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
