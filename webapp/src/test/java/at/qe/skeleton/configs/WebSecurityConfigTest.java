package at.qe.skeleton.configs;


import at.qe.skeleton.spring.CustomizedLogoutSuccessHandler;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
class WebSecurityConfigTest {

    @InjectMocks
    private WebSecurityConfig webSecurityConfig;


    @Test
    void testPasswordEncoderStrength() {
        PasswordEncoder passwordEncoder = webSecurityConfig.passwordEncoder();
        String rawPassword = "password123";

        String encodedPasswordStrength10 = passwordEncoder.encode(rawPassword);
        String encodedPasswordStrength11 = new BCryptPasswordEncoder(11).encode(rawPassword);

        assertNotEquals(encodedPasswordStrength10, encodedPasswordStrength11);
    }

}
