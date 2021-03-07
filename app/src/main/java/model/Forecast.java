package model;

import java.util.List;

/**
 * Forecast is used for storing all the parameters, coordinates and approved time.
 */
public class Forecast {
    private String approvedTime;
    private Coordinates coordinates;
    private List<Parameter> parameters;

    public Forecast() { }

    public void setApprovedTime(String approvedTime) {
        this.approvedTime = approvedTime;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    public String getApprovedTime() { return approvedTime; }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }
}
