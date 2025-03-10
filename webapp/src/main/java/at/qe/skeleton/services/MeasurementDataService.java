package at.qe.skeleton.services;

import at.qe.skeleton.api.exceptions.MeasurementNotFoundException;
import at.qe.skeleton.api.exceptions.SensorNotFoundException;
import at.qe.skeleton.model.MeasurementData;
import at.qe.skeleton.model.MeasurementLabel;
import at.qe.skeleton.model.Sensor;
import at.qe.skeleton.repositories.MeasurementDataRepository;
import at.qe.skeleton.repositories.SensorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;

/**
 * Service for accessing and manipulating measurement data.
 * Can be used for generic CRUD operations on measurement data.
 * @see MeasurementData
 * @see MeasurementDataRepository
 * @see MeasurementNotFoundException
 */
@Component
@Scope("application")
public class MeasurementDataService {

    @Autowired
    private MeasurementDataRepository measurementDataRepository;

    @Autowired
    private SensorRepository sensorRepository;

    /**
     * Returns a list of all measurement data.
     * @param sensorId the sensor id
     * @return a list of all measurement data
     */
    @PreAuthorize("permitAll()")
    public  Collection<MeasurementData> getMeasurementsBySensor(long sensorId) {
        return measurementDataRepository.findMeasurementDataBySensorId(sensorId);
    }

    /**
     * Returns a list of all measurement data.
     * @return a list of all measurement data
     */
    @PreAuthorize("permitAll()")
    public Collection<MeasurementData> getAllMeasurementDatas() {
        return measurementDataRepository.findAll();
    }

    /**
     * Loads a measurement data by its measurement ID.
     * @param id the measurement ID
     * @return the loaded measurement data
     * @throws MeasurementNotFoundException thrown if no measurement data with the given ID was found
     */
    @PreAuthorize("permitAll()")
    public MeasurementData loadMeasurementData(long id) throws MeasurementNotFoundException{

        MeasurementData measurementData = measurementDataRepository.findFirstByMeasurementId(id);
        if (measurementData == null) {
            throw new MeasurementNotFoundException();
        }
        return measurementData;
    }


    /**
     * Saves a measurement data.
     * @param saveMeasurement the measurement data to save
     * @return the updated measurement data
     */
    @PreAuthorize("permitAll()")
    public MeasurementData saveMeasurementData(MeasurementData saveMeasurement) {
        return measurementDataRepository.save(saveMeasurement);
    }

    /**
     * Adds a measurement data to a sensor.
     * @param sensorId the sensor id
     * @param saveMeasurement the measurement data to save
     * @return the updated measurement data
     * @throws SensorNotFoundException thrown if no sensor with the given ID was found
     */
    @PreAuthorize("permitAll()")
    public MeasurementData addMeasurementData(Long sensorId, MeasurementData saveMeasurement) throws SensorNotFoundException {
        Sensor sensor  = sensorRepository.findFirstBySensorId(sensorId);
        if(sensor == null)
            throw new SensorNotFoundException("Sensor with id " + sensorId + " not found");
        saveMeasurement.setSensor(sensor);
        saveMeasurement.setVal(Math.round(saveMeasurement.getVal() * 100.0) / 100.0);


        if(sensor.getLowerLimit() > saveMeasurement.getVal()){
            saveMeasurement.setMeasurementLabel(MeasurementLabel.LOWER_THRESHOLD_EXCEEDED);
            if(!sensor.isCurrentThresholdWarning()) {
                sensor.setCurrentThresholdWarning(true);
                sensorRepository.save(sensor);
            }

        } else if (sensor.getUpperLimit() < saveMeasurement.getVal()){
            saveMeasurement.setMeasurementLabel(MeasurementLabel.UPPER_THRESHOLD_EXCEEDED);
            if(!sensor.isCurrentThresholdWarning()) {
                sensor.setCurrentThresholdWarning(true);
                sensorRepository.save(sensor);
            }
        } else {
            saveMeasurement.setMeasurementLabel(MeasurementLabel.NORMAL);
            if(sensor.isCurrentThresholdWarning()) {
                sensor.setCurrentThresholdWarning(false);
                sensorRepository.save(sensor);
            }
        }
        return measurementDataRepository.save(saveMeasurement);
    }

    /**
     * 
     * @param deleteMeasurement
     */
    @PreAuthorize("permitAll()")
    public void deleteMeasurementData(MeasurementData deleteMeasurement) {
        measurementDataRepository.delete(deleteMeasurement);
    }

    public MeasurementData getLatestMeasurementDataBySensorId(long sensorId) {
        return measurementDataRepository.findFirstBySensorIdOrderByMeasurementIdDesc(sensorId);
    }

    public Collection<MeasurementData> findAllByMeasurementTimestampBetweenAndBySensorId(long sensorId, Date start, Date end) {
        return measurementDataRepository.findAllByMeasurementTimestampBetweenAndBySensorId(sensorId, start, end);
    }


}
