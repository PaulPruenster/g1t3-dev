package at.qe.skeleton.ui.beans;

import at.qe.skeleton.model.UserRole;
import at.qe.skeleton.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class SessionInfoBeanTest {

    @Mock
    private UserService userService;

    private SessionInfoBean sessionInfoBean;

    @BeforeEach
    public void setUp() {
        sessionInfoBean = new SessionInfoBean();
    }

    @Test
    @DisplayName("Test hasRole with valid role")
    void testHasRoleWithValidRole() {
        // Setup mock user
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.toString()));
        Authentication auth = new UsernamePasswordAuthenticationToken("user1", "password1", authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);

        Mockito.when(userService.loadUser(Mockito.anyString())).thenReturn(null);

        // Check if user has the role
        assertTrue(sessionInfoBean.hasRole(UserRole.ADMIN));
    }

    @Test
    @DisplayName("Test hasRole with invalid role")
    void testHasRoleWithInvalidRole() {
        // Setup mock user
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.toString()));
        Authentication auth = new UsernamePasswordAuthenticationToken("user1", "password1", authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);

        Mockito.when(userService.loadUser(Mockito.anyString())).thenReturn(null);

        // Check if user has the role
        Assertions.assertFalse(sessionInfoBean.hasRole(UserRole.USER));
    }

    @Test
    @DisplayName("Test hasRole with null role")
    void testHasRoleWithNullRole() {
        // Setup mock user
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.toString()));
        Authentication auth = new UsernamePasswordAuthenticationToken("user1", "password1", authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);

        Mockito.when(userService.loadUser(Mockito.anyString())).thenReturn(null);

        // Check if user has the role
        Assertions.assertFalse(sessionInfoBean.hasRole((String) null));
    }
}
