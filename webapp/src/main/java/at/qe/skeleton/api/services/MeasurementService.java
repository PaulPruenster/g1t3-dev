package at.qe.skeleton.api.services;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import at.qe.skeleton.api.exceptions.MeasurementNotFoundException;
import at.qe.skeleton.api.model.Measurement;

@Service
public class MeasurementService {

    private static final AtomicLong ID_COUNTER = new AtomicLong(1);
    private static final ConcurrentHashMap<Long, Measurement> measurements = new ConcurrentHashMap<>();

    public Measurement addMeasurement(Measurement measurement) {
        Measurement newMeasurement = new Measurement();
        newMeasurement.setId(ID_COUNTER.getAndIncrement());
        newMeasurement.setPlantID(measurement.getPlantID());
        newMeasurement.setUnit(measurement.getUnit());
        newMeasurement.setValue(measurement.getValue());
        newMeasurement.setType(measurement.getType());

        measurements.put(newMeasurement.getId(), newMeasurement);
        
        return newMeasurement;
    }

    public Measurement findOneMeasurement(Long id) throws MeasurementNotFoundException {
        Measurement measurement = measurements.get(id);
        if (measurement != null)
            return measurement;
        else
            throw new MeasurementNotFoundException();
    }

    public Measurement updateMeasurement(long id, Measurement measurement) {
        if (measurement.getPlantID() != null) {
            measurements.computeIfPresent(id, (key, value) -> {
                value.setPlantID(measurement.getPlantID());
                return value;
            });
        }

        if (measurement.getValue() != null) {
            measurements.computeIfPresent(id, (key, value) -> {
                value.setValue(measurement.getValue());
                return value;
            });
        }

        if (measurement.getUnit() != null) {
            measurements.computeIfPresent(id, (key, value) -> {
                value.setUnit(measurement.getUnit());
                return value;
            });
        }

        if (measurement.getType() != null) {
            measurements.computeIfPresent(id, (key, value) -> {
                value.setType(measurement.getType());
                return value;
            });
        }

    return measurements.get(id);

    //there are more elegant ways to do this: https://www.baeldung.com/spring-rest-json-patch
    }
}
