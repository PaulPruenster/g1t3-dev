package at.qe.skeleton.services;

import at.qe.skeleton.api.exceptions.SensorNotFoundException;
import at.qe.skeleton.model.MeasurementData;
import at.qe.skeleton.model.MeasurementLabel;
import at.qe.skeleton.model.Sensor;
import at.qe.skeleton.repositories.MeasurementDataRepository;
import at.qe.skeleton.repositories.SensorRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.web.WebAppConfiguration;

import java.sql.Timestamp;
import java.util.*;

import static org.eclipse.persistence.internal.jpa.QueryHintsHandler.verify;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@WebAppConfiguration
class MeasurementDataServiceTest {


    @Mock
    private MeasurementDataRepository measurementDataRepository;

    @InjectMocks
    private MeasurementDataService measurementDataService;

    private static MeasurementData measurement1;

    private static MeasurementData measurement2;

    private static Sensor sensor;

    @Mock
    private SensorRepository sensorRepository;

    /**
     * Initializes the {@link MeasurementData} object.
     */
    @BeforeAll
    static void init(){
        sensor = new Sensor();
        sensor.setSensorId(1L);
        sensor.setName("Sensor");
        measurement1 = new MeasurementData();
        measurement1.setMeasurementLabel(MeasurementLabel.NORMAL);
        measurement1.setMeasurementId(1l);
        measurement1.setVal(1);
        measurement1.setSensor(sensor);

        measurement2 = new MeasurementData();
        measurement2.setMeasurementLabel(MeasurementLabel.LOWER_THRESHOLD_EXCEEDED);
        measurement2.setMeasurementId(2l);
        measurement2.setVal(2);
        measurement2.setSensor(sensor);
    }

    void addTestMocking(){
        // Mocking the repository to return the measurement1 object when the save method is called
        when(measurementDataRepository.save(measurement1)).thenReturn(measurement1);

        // Mocking the repository to return the measurement2 object when the save method is called
        when(measurementDataRepository.save(measurement2)).thenReturn(measurement2);

        // Mocking the repository to return the measurement1 object when the findById method is called
        when(measurementDataRepository.findFirstByMeasurementId(measurement1.getMeasurementId())).thenReturn(measurement1);

        // Mocking the repository to return the measurement2 object when the findById method is called
        when(measurementDataRepository.findFirstByMeasurementId(measurement2.getMeasurementId())).thenReturn(measurement2);

        // Mocking the repository to return both measurement objects when the findAll method is called
        when(measurementDataRepository.findAll()).thenReturn(java.util.Arrays.asList(measurement1, measurement2));
    }

    /**
     * Test for the method {@link MeasurementDataService#saveMeasurementData(MeasurementData)}.
     */
    @Test
    @DirtiesContext
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testInitialization(){
        addTestMocking();

        assertNotNull(measurement1, "Measurement1 is null");
        assertNotNull(measurement2, "Measurement2 is null");

        assertEquals(2, measurementDataService.getAllMeasurementDatas().size());
        assertDoesNotThrow(() ->{
            assertEquals(measurement1, measurementDataService.loadMeasurementData(measurement1.getMeasurementId()));
            assertEquals(measurement2, measurementDataService.loadMeasurementData(measurement2.getMeasurementId()));
        });
    }
    /**
     * Test for the method {@link MeasurementDataService#getMeasurementsBySensor(Long)}.
     */

    @Test
    void testGetMeasurementsBySensor(){
        when(measurementDataRepository.findMeasurementDataBySensorId(sensor.getSensorId())).thenReturn(Arrays.asList(measurement1, measurement2));
        Collection<MeasurementData> measurements = measurementDataService.getMeasurementsBySensor(sensor.getSensorId());
        assertNotNull(measurements);
        assertEquals(2, measurements.size());
        assertTrue(measurements.contains(measurement1));
        assertTrue(measurements.contains(measurement2));
    }

    /**
     * Test for the method {@link MeasurementDataService#saveMeasurementData(MeasurementData)}.
     */
    @Test
    @DirtiesContext
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetAllMeasurementDatas(){
        addTestMocking();

        assertEquals(2, measurementDataService.getAllMeasurementDatas().size(), "The size of the list is not 2");

        assertTrue(measurementDataService.getAllMeasurementDatas().contains(measurement1), "The list does not contain measurement1");
        assertTrue(measurementDataService.getAllMeasurementDatas().contains(measurement2), "The list does not contain measurement2");
    }

    /**
     * Test for the method {@link MeasurementDataService#saveMeasurementData(MeasurementData)}.
     */
    @Test
    @DirtiesContext
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testSaveMeasurementData(){
        addTestMocking();

        MeasurementData measurement3 = new MeasurementData();
        measurement3.setMeasurementLabel(MeasurementLabel.NORMAL);
        measurement3.setMeasurementId(3l);
        measurement3.setVal(3);
        measurement3.setSensor(null);

        measurementDataService.saveMeasurementData(measurement3);

        // Mocking the repository to return the measurement3 object when the findById method is called
        when(measurementDataRepository.findFirstByMeasurementId(measurement3.getMeasurementId())).thenReturn(measurement3);

        // Mocking the repository to return all measurement objects when the findAll method is called
        when(measurementDataRepository.findAll()).thenReturn(java.util.Arrays.asList(measurement1, measurement2, measurement3));

        assertEquals(3, measurementDataService.getAllMeasurementDatas().size(), "The size of the list is not 3");

        assertTrue(measurementDataService.getAllMeasurementDatas().contains(measurement3), "The list does not contain measurement3");
    }

    /**
     * Test for the method {@link MeasurementDataService#loadMeasurementData(Long)}.
     */
    @Test
    @DirtiesContext
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testLoadMeasurementData(){
        addTestMocking();
        assertDoesNotThrow(() ->{
            assertEquals(measurement1, measurementDataService.loadMeasurementData(measurement1.getMeasurementId()), "The loaded measurement is not measurement1");
            assertEquals(measurement2, measurementDataService.loadMeasurementData(measurement2.getMeasurementId()), "The loaded measurement is not measurement2");
        });
    }

    /**
     * Test for the method {@link MeasurementDataService#loadMeasurementData(Long)}.
     */
    @Test
    void testLoadMeasurmentDataNotFound(){
        when(measurementDataRepository.findFirstByMeasurementId(measurement1.getMeasurementId())).thenReturn(null);
        assertThrows(Exception.class, () -> measurementDataService.loadMeasurementData(measurement1.getMeasurementId()));
    }

    /**
     * Test for the method {@link MeasurementDataService#loadMeasurementData(Long)}.
     */
    @Test
    void testLoadMeasurmentDataNotExist() {
        assertThrows(Exception.class, () -> measurementDataService.loadMeasurementData(10L));
    }

    /**
     * Test for the method {@link MeasurementDataService#loadMeasurementData(Long)}.
     */
    @Test
    @DirtiesContext
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testDeleteMeasurementData(){
        addTestMocking();

        measurementDataService.deleteMeasurementData(measurement1);

        // Mocking the repository to return null when the findById method is called
        when(measurementDataRepository.findFirstByMeasurementId(measurement1.getMeasurementId())).thenReturn(null);

        // Mocking the repository to return the measurement2 object when the findAll method is called
        when(measurementDataRepository.findAll()).thenReturn(java.util.Arrays.asList(measurement2));

        assertEquals(1, measurementDataService.getAllMeasurementDatas().size(), "The size of the list is not 1");

        assertFalse(measurementDataService.getAllMeasurementDatas().contains(measurement1), "The list contains measurement1");
        assertTrue(measurementDataService.getAllMeasurementDatas().contains(measurement2), "The list does not contain measurement2");
    }

    /**
     * Test for the method {@link MeasurementDataService#loadMeasurementData(Long)}.
     */
    @Test
    @DirtiesContext
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testUpdateMeasurementData() {
        addTestMocking();

        MeasurementData measurement3 = new MeasurementData();
        measurement3.setMeasurementLabel(MeasurementLabel.NORMAL);
        measurement3.setMeasurementId(3l);
        measurement3.setVal(3);
        measurement3.setSensor(null);

        measurementDataService.saveMeasurementData(measurement3);

        // Mocking the repository to return the measurement3 object when the findById method is called
        when(measurementDataRepository.findFirstByMeasurementId(measurement3.getMeasurementId())).thenReturn(measurement3);

        // Mocking the repository to return both measurement objects when the findAll method is called
        when(measurementDataRepository.findAll()).thenReturn(java.util.Arrays.asList(measurement1, measurement2, measurement3));

        assertEquals(3, measurementDataService.getAllMeasurementDatas().size(), "The size of the list is not 3");

        assertTrue(measurementDataService.getAllMeasurementDatas().contains(measurement3), "The list does not contain measurement3");

        measurement3.setVal(4);

        measurementDataService.saveMeasurementData(measurement3);
        assertDoesNotThrow(() -> {
            assertEquals(4, measurementDataService.loadMeasurementData(measurement3.getMeasurementId()).getVal(), "The value of the measurement is not 4");
        });
    }

    /**
     * Test for the method {@link MeasurementDataService#getLatestMeasurementDataBySensorId(long sensorId)}.
     */

    @Test
    void testGetLatestMeasurementDataBySensorId() {
        long sensorId = 1L;
        MeasurementData expectedMeasurementData = new MeasurementData();
        when(measurementDataRepository.findFirstBySensorIdOrderByMeasurementIdDesc(sensorId))
                .thenReturn(expectedMeasurementData);

        MeasurementData actualMeasurementData = measurementDataService.getLatestMeasurementDataBySensorId(sensorId);

        assertEquals(expectedMeasurementData, actualMeasurementData);
    }

    /**
     * Test for the method {@link MeasurementDataService#FindAllByMeasurementTimestampBetweenAndBySensorId(Long)}.
     */
    @Test
    void testFindAllByMeasurementTimestampBetweenAndBySensorId() {
        long sensorId = 1L;
        Timestamp from = new Timestamp(0);
        Timestamp to = new Timestamp(1000);
        List<MeasurementData> expectedMeasurementDataList = new ArrayList<>();
        when(measurementDataRepository.findAllByMeasurementTimestampBetweenAndBySensorId(sensorId, from, to))
                .thenReturn(expectedMeasurementDataList);

        List<MeasurementData> actualMeasurementDataList = (List<MeasurementData>) measurementDataService.findAllByMeasurementTimestampBetweenAndBySensorId(sensorId, from, to);

        assertEquals(expectedMeasurementDataList, actualMeasurementDataList);
    }

    @Test
    void testAddMeasurementData() {
        MeasurementData measurementData = new MeasurementData();
        measurementData.setMeasurementId(1L);
        measurementData.setMeasurementLabel(MeasurementLabel.NORMAL);
        measurementData.setVal(1);
        measurementData.setSensor(null);
        measurementData.setMeasurementTimestamp(new Timestamp(0));

        when(measurementDataRepository.save(measurementData)).thenReturn(measurementData);

        MeasurementData actualMeasurementData = measurementDataService.saveMeasurementData(measurementData);

        assertEquals(measurementData, actualMeasurementData);
    }

    @Test
    void testAddMeasurementDataSensorNotFound() {
        when(sensorRepository.findFirstBySensorId(1L)).thenReturn(null);

        MeasurementData measurementData = new MeasurementData();
        measurementData.setVal(50.0);

        assertThrows(SensorNotFoundException.class, () -> {
            measurementDataService.addMeasurementData(1L, measurementData);
        });
    }

    @Test
    void testAddMeasurementDataBelowLowerThreshold() {
        Sensor sensor = new Sensor();
        sensor.setSensorId(1L);
        sensor.setLowerLimit(20.0);
        sensor.setUpperLimit(80.0);
        sensor.setCurrentThresholdWarning(true);
        when(sensorRepository.findFirstBySensorId(1L)).thenReturn(sensor);

        MeasurementData measurementData = new MeasurementData();
        measurementData.setVal(10.0);

        doThrow(new RuntimeException("Failed to save measurement data")).when(measurementDataRepository).save(measurementData);

        assertThrows(RuntimeException.class, () -> {
            measurementDataService.addMeasurementData(1L, measurementData);
        });
    }

    @Test
    void testAddMeasurementUpperLimitBelowValue() {
        Sensor sensor = new Sensor();
        sensor.setSensorId(1L);
        sensor.setLowerLimit(20.0);
        sensor.setUpperLimit(80.0);
        sensor.setCurrentThresholdWarning(true);
        when(sensorRepository.findFirstBySensorId(1L)).thenReturn(sensor);

        MeasurementData measurementData = new MeasurementData();
        measurementData.setVal(90.0);

        doThrow(new RuntimeException("Failed to save measurement data")).when(measurementDataRepository).save(measurementData);

        assertThrows(RuntimeException.class, () -> {
            measurementDataService.addMeasurementData(1L, measurementData);
        });
    }

    @Test
    void testAddMeasurementNormalAndNoThresholdWarningYet() throws SensorNotFoundException {
        Sensor sensor = new Sensor();
        sensor.setSensorId(1L);
        sensor.setLowerLimit(20.0);
        sensor.setUpperLimit(80.0);
        sensor.setCurrentThresholdWarning(false);
        when(sensorRepository.findFirstBySensorId(1L)).thenReturn(sensor);

        MeasurementData measurementData = new MeasurementData();
        measurementData.setVal(50.0);
        measurementData.setMeasurementLabel(MeasurementLabel.NORMAL);

        when(measurementDataRepository.save(measurementData)).thenReturn(measurementData);

        MeasurementData actualMeasurementData = measurementDataService.addMeasurementData(1L, measurementData);

        assertEquals(measurementData, actualMeasurementData);
    }

}
