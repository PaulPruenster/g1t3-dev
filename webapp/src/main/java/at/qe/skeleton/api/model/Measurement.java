package at.qe.skeleton.api.model;


public class Measurement {

    private Long id;
    private Integer plantID;
    private Double value;
    private String unit;
    private String type;

    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Integer getPlantID() {
        return plantID;
    }
    public void setPlantID(Integer plantID) {
        this.plantID = plantID;
    }
    public Double getValue() {
        return value;
    }
    public void setValue(Double value) {
        this.value = value;
    }
    public String getUnit() {
        return unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

}
