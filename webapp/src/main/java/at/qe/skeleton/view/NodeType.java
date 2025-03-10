package at.qe.skeleton.view;

public enum NodeType {
    // enum with string values

    ROOT("root"),
    USER("user"),
    SENSORSTATION("sensorStation"),
    SENSOR("sensor"),
    NOTASSIGNED("notAssigned");
    private String value;

    NodeType(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
    @Override
    public String toString() {
        return value;
    }

}
