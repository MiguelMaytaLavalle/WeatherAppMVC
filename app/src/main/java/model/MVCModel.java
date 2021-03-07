package model;

import java.util.ArrayList;
import java.util.List;

import model.singleton.ForecastSingleton;

/**
 * This is the Model which contains the data.
 */

public class MVCModel {
    private List<Parameter> mParameterList;
    private Coordinates mCoordinates;
    private String mApprovedTime;
    private final Forecast mForecast;

    /**
     * When initialized, control if the models are null when fetching the instance of the ForecastSingleton
     *
     */
    public MVCModel() {
        this.mForecast = ForecastSingleton.getInstance();
        if(mForecast.getCoordinates() == null || mForecast.getApprovedTime() == null || mForecast.getParameters() == null){
            this.mParameterList = new ArrayList<>();
        }else{
            this.mCoordinates = mForecast.getCoordinates();
            this.mApprovedTime = mForecast.getApprovedTime();
            this.mParameterList = mForecast.getParameters();
        }
    }

    /**
     * Sets the coordinates that have been sent from the controller.
     * @return
     */
    public List<Parameter> getParameterList() {
        return mParameterList;
    }

    /**
     *
     * @param latitude
     * @param longitude
     */
    public void setCoordinates(float latitude, float longitude) {
        this.mCoordinates = new Coordinates(latitude, longitude);
        System.out.println(mCoordinates.toString());
    }

    /**
     * Sets the approved time from the response
     * @param approvedTime
     */
    public void setApprovedTime(String approvedTime) {
        this.mApprovedTime = approvedTime;
    }

    public String getApprovedTime(){
        return mApprovedTime;
    }

    /**
     * Returns an Forecast with all the parameters ready.
     * @return
     */
    public Forecast getForecast() {
        this.mForecast.setApprovedTime(mApprovedTime);
        this.mForecast.setCoordinates(mCoordinates);
        this.mForecast.setParameters(mParameterList);
        //this.mForecast = new Forecast(mApprovedTime,mCoordinates,mParameterList);
        return mForecast;
    }
}
