package at.qe.skeleton.api.controllers;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import at.qe.skeleton.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import at.qe.skeleton.api.exceptions.AccessPointNotFoundException;
import at.qe.skeleton.services.AccessPointService;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AccessPointControllerTest {

    @Mock
    private AccessPointService accessPointService;

    @InjectMocks
    private AccessPointController accessPointController;

    @Test
    @DisplayName("Test getting Access Point by Id")
    void testGetAccessPointById() throws AccessPointNotFoundException {
        AccessPoint accessPoint = new AccessPoint();
        accessPoint.setName("Test Access Point");

        when(accessPointService.loadAccessPoint(1L)).thenReturn(accessPoint);

        AccessPoint result = accessPointController.getAccessPoint(1L);

        assertEquals(accessPoint.getName(), result.getName());
    }

    @Test
    @DisplayName("Test getting all Access Points")
    void testGetAllAccessPoints() {
        Iterable<AccessPoint> accessPoints = accessPointController.getAllAccessPoints();

        assertNotNull(accessPoints);
    }

    @Test
    void testGetNewAccessPoint() {
        AccessPoint accessPoint = new AccessPoint();
        accessPoint.setName("new AccessPoint");
        when(accessPointService.saveAccessPoint(any(AccessPoint.class))).thenReturn(accessPoint);

        Long result = accessPointController.getNewAccessPoint();

        assertEquals(accessPoint.getAccessPointId(), result);
    }

    @Test
    void testGetNewAccessPointException() {
        when(accessPointService.saveAccessPoint(any(AccessPoint.class))).thenThrow(new RuntimeException());

        Assertions.assertThrows(RuntimeException.class, () -> {
            accessPointController.getNewAccessPoint();
        });
    }
}

