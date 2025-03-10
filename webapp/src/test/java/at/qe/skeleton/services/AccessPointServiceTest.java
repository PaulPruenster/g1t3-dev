package at.qe.skeleton.services;
import at.qe.skeleton.model.AccessPoint;
import at.qe.skeleton.repositories.AccessPointRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.web.WebAppConfiguration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@WebAppConfiguration
class AccessPointServiceTest {

    @Mock
    private AccessPointRepository accessPointRepository;

    @InjectMocks
    private AccessPointService accessPointService;

    @Mock
    private BluetoothDeviceService  bluetoothDeviceService;

    private static AccessPoint accessPoint1;

    private static AccessPoint accessPoint2;


    /**
     * This method is called before all tests and initializes the test data
     */
    @BeforeAll
    static void init() {
        accessPoint1 = new AccessPoint();
        accessPoint1.setAccessPointId(1L);
        accessPoint1.setName("AccessPoint1");

        accessPoint2 = new AccessPoint();
        accessPoint2.setAccessPointId(2L);
        accessPoint2.setName("AccessPoint2");
    }

    /**
     * This method is called before each test and adds the test mocking
     */
    void addTestMocking(){
        // Mocking the repository to return the accessPoint1 and accessPoint2 when the getAllAccessPoints method is called
        when(accessPointRepository.findAll()).thenReturn(List.of(new AccessPoint[]{accessPoint1, accessPoint2}));

        // Mocking the repository to return the accessPoint1 when the loadAccessPoint method is called
        when(accessPointRepository.findFirstByAccessPointId(accessPoint1.getAccessPointId())).thenReturn(accessPoint1);

        // Mocking the repository to return the accessPoint2 when the loadAccessPoint method is called
        when(accessPointRepository.findFirstByAccessPointId(accessPoint2.getAccessPointId())).thenReturn(accessPoint2);

        // Mocking the repository to return the accessPoint1 when the saveAccessPoint method is called
        when(accessPointRepository.save(accessPoint1)).thenReturn(accessPoint1);

        // Mocking the repository to return the accessPoint2 when the saveAccessPoint method is called
        when(accessPointRepository.save(accessPoint2)).thenReturn(accessPoint2);



    }

    /**
     * Test for the method {@link AccessPointService#getAllAccessPoints()}.
     */
    @Test
    @DirtiesContext
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testInitialization(){
        addTestMocking();

        assertNotNull(accessPoint1, "AccessPoint1 could not be initialized");
        assertNotNull(accessPoint2, "AccessPoint2 could not be initialized");

        assertEquals(2, accessPointService.getAllAccessPoints().size(), "getAllAccessPoints() does not return the correct number of AccessPoints");
        assertDoesNotThrow(() ->{
            assertEquals(accessPoint1, accessPointService.loadAccessPoint(accessPoint1.getAccessPointId()), "loadAccessPoint() does not return the correct AccessPoint");
            assertEquals(accessPoint2, accessPointService.loadAccessPoint(accessPoint2.getAccessPointId()), "loadAccessPoint() does not return the correct AccessPoint");
        });

    }

    /**
     * Test for the method {@link AccessPointService#loadAccessPoint(Long)}.
     */
    @Test
    @DirtiesContext
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetAllAccessPoints() {
       addTestMocking();

        assertEquals(2, accessPointService.getAllAccessPoints().size(), "getAllAccessPoints() does not return the correct number of AccessPoints");
        assertTrue(accessPointService.getAllAccessPoints().contains(accessPoint1), "getAllAccessPoints() does not return the correct AccessPoints");
        assertTrue(accessPointService.getAllAccessPoints().contains(accessPoint2), "getAllAccessPoints() does not return the correct AccessPoints");
    }


    /**
     * Test for the method {@link AccessPointService#loadAccessPoint(Long)}.
     */
    /*
    @Test
    @DirtiesContext
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testDeleteAccessPoint() {
        addTestMocking();

        Long accessPoint1_id = accessPoint1.getAccessPointId();
        Long accessPoint2_id = accessPoint2.getAccessPointId();

        accessPointService.deleteAccessPoint(accessPoint1);
        accessPointService.deleteAccessPoint(accessPoint2);

        assertEquals(0, accessPointService.getAllAccessPoints().size(), "getAllAccessPoints() does not return the correct number of AccessPoints");
        assertThrows(AccessPointNotFoundException.class, () -> accessPointService.loadAccessPoint(accessPoint1_id), "loadAccessPoint() does not throw an AccessPointNotFoundException");
        assertThrows(AccessPointNotFoundException.class, () -> accessPointService.loadAccessPoint(accessPoint2_id), "loadAccessPoint() does not throw an AccessPointNotFoundException");
    }

    */

    /**
     * Test for the method {@link AccessPointService#saveAccessPoint(AccessPoint)}.
     */
    @Test
    @DirtiesContext
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testSaveAccessPoint() {
        addTestMocking();

        Long accessPoint1_id = accessPoint1.getAccessPointId();
        AccessPoint toBeSavedAccessPoint = new AccessPoint();
        toBeSavedAccessPoint.setAccessPointId(3L);
        toBeSavedAccessPoint.setName("AccessPoint3");

        accessPointService.saveAccessPoint(toBeSavedAccessPoint);

        Long accessPoint3_id = toBeSavedAccessPoint.getAccessPointId();

        // Mocking the repository to return null when the loadAccessPoint method is called
        when(accessPointRepository.findFirstByAccessPointId(accessPoint3_id)).thenReturn(toBeSavedAccessPoint);
        when(accessPointRepository.findAll()).thenReturn(List.of(new AccessPoint[]{accessPoint1, accessPoint2, toBeSavedAccessPoint}));


        assertEquals(3, accessPointService.getAllAccessPoints().size(), "No AccessPoint has been saved after calling AceessPointService.saveAccessPoint");
        assertDoesNotThrow(() -> {
            AccessPoint savedAccessPoint = accessPointService.loadAccessPoint(accessPoint3_id);
            assertNotNull(savedAccessPoint, "Saved AccessPoint \"" + accessPoint3_id + "\" could not be loaded from test data source via UserService.loadUser");
        });
    }

    /**
     * Test for the method {@link AccessPointService#saveAccessPoint(AccessPoint)}.
     */
    @Test
    @DirtiesContext
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testUpdateAccessPoint() {
        addTestMocking();
        Long accessPoint1_id = accessPoint1.getAccessPointId();

        accessPoint1.setName("AccessPoint1Updated");
        accessPointService.saveAccessPoint(accessPoint1);

        // Mocking the repository to return null when the loadAccessPoint method is called
        when(accessPointRepository.findFirstByAccessPointId(accessPoint1_id)).thenReturn(accessPoint1);
        when(accessPointRepository.findAll()).thenReturn(List.of(new AccessPoint[]{accessPoint1}));

        assertEquals(1, accessPointService.getAllAccessPoints().size(), "No AccessPoint has been updated after calling AceessPointService.saveAccessPoint");
        assertDoesNotThrow(() -> {
            AccessPoint updatedAccessPoint = accessPointService.loadAccessPoint(accessPoint1_id);
            assertNotNull(updatedAccessPoint, "Updated AccessPoint \"" + accessPoint1_id + "\" could not be loaded from test data source via UserService.loadUser");
            assertEquals("AccessPoint1Updated", updatedAccessPoint.getName(), "Updated AccessPoint \"" + accessPoint1_id + "\" has not been updated correctly");
        });
    }

    /**
     * Test for the method {@link AccessPointService#saveAccessPoint(AccessPoint)}.
     */
    @Test
    @DirtiesContext
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testLoadAccessPoint() {
        addTestMocking();
        Long accessPoint1_id = accessPoint1.getAccessPointId();
        assertDoesNotThrow(() -> {
            AccessPoint loadedAccessPoint = accessPointService.loadAccessPoint(accessPoint1_id);
            assertNotNull(loadedAccessPoint, "AccessPoint \"" + accessPoint1_id + "\" could not be loaded from test data source via UserService.loadUser");
            assertEquals(accessPoint1_id, loadedAccessPoint.getAccessPointId(), "AccessPoint \"" + accessPoint1_id + "\" has not been loaded correctly");
        });
    }
}
