package at.qe.skeleton.services;

import at.qe.skeleton.model.Userx;
import at.qe.skeleton.repositories.UserxRepository;
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
import static org.mockito.Mockito.when;

@WebAppConfiguration
@SpringBootTest
class UserxServiceTest {

    @Mock
    private UserxRepository userxRepository;

    @Mock
    private AccessPointService accessPointService;

    @Mock
    private SensorStationService sensorStationService;

    @InjectMocks
    private UserService userService;

    private static Userx user1;

    private static Userx user2;

    /**
     * This method is called before all tests and initializes the test data
     */
    @BeforeAll
    static void init() {
        user1 = new Userx();
        user1.setUsername("User1");
        user1.setFirstName("User");
        user1.setLastName("One");


        user2 = new Userx();
        user2.setUsername("User2");
        user2.setFirstName("User");
        user2.setLastName("Two");
    }

    /**
     * Test for method {@link UserService#getAllUsers()}
     */
    void addTestMocking(){
        // Mocking the repository to return the user1 and user2 when the getAllUsers method is called
        when(userxRepository.findAll()).thenReturn(List.of(new Userx[]{user1, user2}));

        // Mocking the repository to return the user1 when the loadUser method is called
        when(userxRepository.findFirstByUsername(user1.getUsername())).thenReturn(user1);

        // Mocking the repository to return the user2 when the loadUser method is called
        when(userxRepository.findFirstByUsername(user2.getUsername())).thenReturn(user2);
    }

    /**
     * Test for method {@link UserService#getAllUsers()}
     */
    @Test
    @DirtiesContext
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testInitialization(){
        addTestMocking();

        assertNotNull(user1, "User1 is null");
        assertNotNull(user2, "User2 is null");

        assertEquals(2, userService.getAllUsers().size(), "There are not 2 users in the database");

        assertEquals(user1, userService.loadUser(user1.getUsername()), "User1 is not loaded correctly");
        assertEquals(user2, userService.loadUser(user2.getUsername()), "User2 is not loaded correctly");

    }

    /**
     * Test for method {@link UserService#getAllUsers()}
     */
    @Test
    @DirtiesContext
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetAllUsers(){
        addTestMocking();
        assertEquals(2, userService.getAllUsers().size(), "There are not 2 users in userService.getAllUsers()");
        assertTrue(userService.getAllUsers().contains(user1), "User1 is not in userService.getAllUsers()");
        assertTrue(userService.getAllUsers().contains(user2), "User2 is not in userService.getAllUsers()");
    }

    /**
     * Test for method {@link UserService#loadUser(String)}
     */
    @Test
    @DirtiesContext
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testDeleteUser(){
        addTestMocking();

        userService.deleteUser(user1);

        // Mocking the repository to return the user2 when the getAllUsers method is called
        when(userxRepository.findAll()).thenReturn(List.of(new Userx[]{user2}));

        // Mocking the repository to return the user2 when the loadUser method is called
        when(userxRepository.findFirstByUsername(user2.getUsername())).thenReturn(user2);

        assertEquals(1, userService.getAllUsers().size(), "There is a wrong amount of user in  usersService.getAllUsers()");
        assertFalse(userService.getAllUsers().contains(user1), "User1 is still in userService.getAllUsers()");
        assertTrue(userService.getAllUsers().contains(user2), "User2 is not in userService.getAllUsers()");
    }

    /**
     * Test for method {@link UserService#loadUser(String)}
     */
    @Test
    @DirtiesContext
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testSaveUser(){
        addTestMocking();

        Userx user3 = new Userx();
        user3.setUsername("User3");
        user3.setFirstName("User");
        user3.setLastName("Three");

        userService.saveUser(user3);

        // Mocking the repository to return the user1, user2 and user3 when the getAllUsers method is called
        when(userxRepository.findAll()).thenReturn(List.of(new Userx[]{user1, user2, user3}));

        // Mocking the repository to return the user3 when the loadUser method is called
        when(userxRepository.findFirstByUsername(user3.getUsername())).thenReturn(user3);

        assertEquals(3, userService.getAllUsers().size(), "There is a wrong amount of user in  usersService.getAllUsers()");

        assertNotNull(userService.loadUser(user3.getUsername()), "User3 is not saved correctly");
        assertNotNull(userService.loadUser(user1.getUsername()), "User1 was not suppose to be deleted");
        assertNotNull(userService.loadUser(user2.getUsername()), "User2 was not suppose to be deleted");
    }

    /**
     * Test for method {@link UserService#loadUser(String)}
     */
    @Test
    @DirtiesContext
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testUpdateUser(){
        addTestMocking();

        Userx toBeUpdatedUser = userService.loadUser(user1.getUsername());
        toBeUpdatedUser.setFirstName("Updated");
        userService.saveUser(user1);

        // Mocking the repository to return the user1 when the loadUser method is called
        when(userxRepository.findFirstByUsername(user1.getUsername())).thenReturn(toBeUpdatedUser);

        // Mocking the repository to return the user1 when the getAllUsers method is called
        when(userxRepository.findAll()).thenReturn(List.of(new Userx[]{toBeUpdatedUser, user2}));
        assertEquals(toBeUpdatedUser, userService.loadUser(user1.getUsername()), "User1 is not updated correctly");
    }


    /**
     * Test for method {@link UserService#loadUser(String)}
     */
    @Test
    @DirtiesContext
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testLoadUser(){
        addTestMocking();

        assertEquals(user1, userService.loadUser(user1.getUsername()), "User1 is not loaded correctly");
        assertEquals(user2, userService.loadUser(user2.getUsername()), "User2 is not loaded correctly");
    }


    /*
    @Test
    @DirtiesContext
    void testSaveSensorNotAdmin() {
        addTestMocking();
        assertThrows(org.springframework.security.access.AccessDeniedException.class, () -> {
            Userx loadUser = userService.loadUser(user1.getUsername());
            loadUser.setLastName("User1Updated");
            userService.saveUser(loadUser);
        }, "saveUserx() does not throw an AccessDeniedException when a non admin user tries to save a userx");
    }

    @Test
    @DirtiesContext
    void testDeleteSensorNotAdmin() {
        addTestMocking();
        assertThrows(org.springframework.security.access.AccessDeniedException.class, () -> {
            userService.deleteUser(user1);
        }, "deleteUser() does not throw an AccessDeniedException when a non admin user tries to delete a userx");
    }

     */
}
