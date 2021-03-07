package view;

import android.os.Parcelable;

import model.Forecast;

public interface MVCMainActivityView extends MVCView{
    public void bindDataToView();
    public void showForecastView(Forecast forecast);
    public void setViewInvisible();
    public void setViewVisible();
}
