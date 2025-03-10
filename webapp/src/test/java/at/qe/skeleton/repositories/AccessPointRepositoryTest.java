package at.qe.skeleton.repositories;

import at.qe.skeleton.model.AccessPoint;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@WebAppConfiguration
@Sql("classpath:sql/AccessPointTest.sql")
class AccessPointRepositoryTest {

    @Autowired
    private AccessPointRepository accessPointRepository;

    private AccessPoint accessPoint;

    private Boolean makeConnection;


    /**
     * Initializes the {@link AccessPoint} object.
     */
    @BeforeEach
    public void setUp() {
        accessPoint = new AccessPoint();
        accessPoint.setAccessPointId(1L);
        accessPoint.setName("AP1");
        accessPoint.setMakeConnection(false); // Set a non-null value here
    }

    /**
     * Test for the method {@link AccessPointRepository#save(AccessPoint)}.
     */
    /*
    @Test
    @DirtiesContext
    void testSave() {
        // Save the access point
        AccessPoint savedAccessPoint = accessPointRepository.save(accessPoint);

        // Verify that the access point is saved correctly
        assertNotNull(savedAccessPoint.getId());
        assertEquals(accessPoint.getAccessPointId(), savedAccessPoint.getAccessPointId());
        assertEquals(accessPoint.getName(), savedAccessPoint.getName());
    }
    */
}
