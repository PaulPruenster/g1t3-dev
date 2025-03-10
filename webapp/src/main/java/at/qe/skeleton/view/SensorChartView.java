package at.qe.skeleton.view;

import at.qe.skeleton.model.MeasurementData;
import at.qe.skeleton.model.Sensor;
import at.qe.skeleton.services.MeasurementDataService;
import at.qe.skeleton.ui.controllers.SensorDetailController;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Named;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.line.LineChartDataSet;
import org.primefaces.model.charts.line.LineChartModel;
import org.primefaces.model.charts.line.LineChartOptions;
import org.primefaces.model.charts.optionconfig.title.Title;
import org.primefaces.model.charts.optionconfig.animation.Animation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Named
@Scope("view")
public class SensorChartView implements Serializable {

    private LineChartModel lineModel;

    @Autowired
    private SensorDetailController sensorDetailController;

    @Autowired
    private  transient MeasurementDataService measurementDataService;

    private Date minDate;

    private Date maxDate;

    private String label = "";



    @PostConstruct
    public void init() {
        createLineModel();
    }

    public void saveSensor() {
        sensorDetailController.doSaveSensor();
        createLineModel();
    }

    public void createLineModel() {
        lineModel = new LineChartModel();
        ChartData data = new ChartData();

        List<String> labels = new ArrayList<>();



        LineChartDataSet upperLimit = new LineChartDataSet();
        List<Object> valuesUpperLimit = new ArrayList<>();

        upperLimit.setData(valuesUpperLimit);
        upperLimit.setFill(false);
        upperLimit.setLabel("Upper Limit");
        upperLimit.setBorderColor("rgb(255, 0, 0)");
        upperLimit.setTension(0.1);
        upperLimit.setPointRadius(0);
        upperLimit.setPointHitRadius(10);
        data.addChartDataSet(upperLimit);


        LineChartDataSet lowerLimit = new LineChartDataSet();
        List<Object> valuesLowerLimit = new ArrayList<>();

        lowerLimit.setData(valuesLowerLimit);
        lowerLimit.setFill(false);
        lowerLimit.setLabel("Lower Limit");
        lowerLimit.setBorderColor("rgb(0, 0, 255)");
        lowerLimit.setTension(0.1);
        lowerLimit.setPointRadius(0);
        lowerLimit.setPointHitRadius(10);
        data.addChartDataSet(lowerLimit);


        LineChartDataSet dataSet = new LineChartDataSet();
        List<Object> values = new ArrayList<>();

        dataSet.setData(values);
        dataSet.setFill(false);
        dataSet.setLabel("Measuring Data");
        dataSet.setBorderColor("rgb(75, 192, 192)");
        dataSet.setTension(0.1);
        dataSet.setPointRadius(0);
        dataSet.setPointHitRadius(10);
        data.addChartDataSet(dataSet);


        Sensor sensor = this.sensorDetailController.getSensor();
        if(sensor == null){
            return;
        }
        Long id = sensor.getSensorId();
        Collection<MeasurementData> measurementDataCollection = measurementDataService.findAllByMeasurementTimestampBetweenAndBySensorId(
                id,
                (minDate ==null) ? new Date(0L) : minDate,
                (maxDate ==null) ? new Date() : maxDate
        );
        this.label = "Es wurden " + measurementDataCollection.size() + " Messwerte gefunden.";
        ArrayList<MeasurementData> measurementData = new ArrayList<>(measurementDataCollection);

        for (MeasurementData measurementDatum : measurementData) {
            valuesUpperLimit.add(sensor.getUpperLimit());
            valuesLowerLimit.add(sensor.getLowerLimit());
            values.add(measurementDatum.getVal());
            labels.add(measurementDatum.getMeasurementTimestamp().toString());

        }

        data.setLabels(labels);

        //Options
        LineChartOptions options = new LineChartOptions();
        Title title = new Title();
        title.setDisplay(true);
        title.setText(this.label);
        options.setTitle(title);
        options.setAnimation(null);
        Animation animation = new Animation();
        animation.setDuration(0);
        options.setAnimation(animation);

        lineModel.setOptions(options);
        lineModel.setData(data);
    }

    public LineChartModel getLineModel() {
        return lineModel;
    }

    public void setLineModel(LineChartModel lineModel) {
        this.lineModel = lineModel;
    }


    public Date getMinDate() {
        return minDate;
    }

    public void setMinDate(Date minDate) {
        this.minDate = minDate;
    }

    public Date getMaxDate() {
        return maxDate;
    }

    public void setMaxDate(Date maxDate) {
        this.maxDate = maxDate;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
