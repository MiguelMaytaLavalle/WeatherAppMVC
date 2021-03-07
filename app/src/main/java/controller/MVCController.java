package controller;

import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.ClientError;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.weatherappmvc.JsonRW;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import model.Forecast;
import model.Parameter;
import model.MVCModel;
import com.example.weatherappmvc.RequestQueueManager;
import model.parser.JsonParser;
import view.MainActivityViewImplementor;

/**
 * This is the controller class for the application
 */
public class MVCController extends AppCompatActivity {
    private static final String WEATHER_10_DAYS_URL
            ="https://opendata-download-metfcst.smhi.se/api/category/pmp3g/version/2/geotype/point/";

    MainActivityViewImplementor mvcView;
    MVCModel mvcModel;
    RequestQueueManager requestQueueManager;

    public MVCController(MVCModel mvcModel, MainActivityViewImplementor mvcView, RequestQueueManager requestQueueManager) {
        this.mvcModel = mvcModel;
        this.mvcView = mvcView;
        this.requestQueueManager = requestQueueManager;
    }

    public List<Parameter> getForecastList() {
        return mvcModel.getParameterList();
    }

    /**
     * Invoked when the data has been updated and needs to be refreshed with
     * wither new or old values. If failure then set the UI to INVISIBLE instead.
     */
    public void onViewLoaded(){
        try{
            Forecast forecast = mvcModel.getForecast();
            mvcView.showForecastView(forecast);
        }catch (NullPointerException e){
            mvcView.setViewInvisible();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onSubmitButtonClicked(float latitude, float longitude){
        String url = WEATHER_10_DAYS_URL + "lon/"+longitude+"/lat/"+latitude+"/data.json";
        mvcModel.setCoordinates(latitude,longitude);
        getRequestForecast(url);
    }

    /**
     * Checks if the data is 10 minutes old when invoked.
     * Sets the oldApprovedTime with 10 minutes ahead for when comparing with the current time
     * when data was fetched.
     * @param oldApprovedTime
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean isDataOld(String oldApprovedTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar oldCal, curCal;
        try {
            Date oldDate = sdf.parse(oldApprovedTime);
            curCal = Calendar.getInstance();
            oldCal = Calendar.getInstance();
            oldCal.setTime(oldDate);
            oldCal.add(Calendar.MINUTE, 10);

            if(oldCal.get(Calendar.YEAR) == curCal.get(Calendar.YEAR) && oldCal.get(Calendar.MONTH)
                    == curCal.get(Calendar.MONTH) && oldCal.get(Calendar.DATE) == curCal.get(Calendar.DATE)){

                if(oldCal.get(Calendar.HOUR) == curCal.get(Calendar.HOUR)){
                    if(oldCal.get(Calendar.MINUTE) < curCal.get(Calendar.MINUTE)){
                        System.out.println("TRUE");
                        return true;
                    }
                }
            }
            System.out.println("False");
            return false;

        } catch (ParseException e) {
            e.printStackTrace();
        }catch (NullPointerException e){
            return true;
        }
        return true;
    }

    public void getRequestForecast(String url){
        JsonObjectRequest forecastRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                responseListener,
                errorListener);
        forecastRequest.setTag(mvcView.getRootView().getTag());
        requestQueueManager.getRequestQueue().add(forecastRequest);
    }

    private Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onResponse(JSONObject response) {
            Log.i("Volley", "got response");
            try {
                Log.i("response.listener", "onResponse: " + response);
                List<Parameter> newParameterList = JsonParser.getParameterList(response);
                String newApprovedTime = JsonParser.getParsedApprovedTime(response);
                if(isDataOld(mvcModel.getApprovedTime())){
                    mvcModel.setApprovedTime(newApprovedTime);
                    mvcModel.getParameterList().clear();
                    mvcModel.getParameterList().addAll(newParameterList);
                    mvcView.bindDataToView();
                    requestQueueManager.getRequestQueue().cancelAll(mvcView.getRootView());
                    mvcView.messageToast("Fetched Updated Data");
                    JsonRW.writeToFile(mvcView.getRootView().getContext(), mvcModel.getForecast());
                }else{
                    JsonRW.readFromFile(mvcView.getRootView().getContext());
                }

            } catch (JSONException e){
                Log.i("Volley", "Error");
                mvcView.messageToast("Something Went Wrong. Try Again");
            }
        }
    };

    private Response.ErrorListener errorListener = new Response.ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Log.i("Volley error", volleyError.toString());
            String message = null;
            if (volleyError instanceof NetworkError) {
                message = "No Internet Connection. Fetching Old Stored Data";
                getOldForecastData(JsonRW.readFromFile(mvcView.getRootView().getContext()));
            }
            if(volleyError instanceof ClientError){
                message = "Out of Bounds. Fetching Old Stored Data";
                getOldForecastData(JsonRW.readFromFile(mvcView.getRootView().getContext()));
            }
            Toast.makeText(mvcView.getRootView().getContext(), message, Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * Invoked when we need to fetch the old data from the saved file.
     * @param jsonObject
     */
    private void getOldForecastData(JSONObject jsonObject){
        JSONObject geometry, parameter;
        JSONArray coordinates, inputCoordinates, parameters;
        float latitude = 0, longitude = 0;
        String approvedTime = null;
        List<Parameter> oldParameters = new ArrayList<>();
        try {
            approvedTime = jsonObject.get("approvedTime").toString();
            geometry = jsonObject.getJSONObject("geometry");
            coordinates = geometry.getJSONArray("coordinates");
            inputCoordinates = coordinates.getJSONArray(0);
            longitude = Float.parseFloat(inputCoordinates.get(0).toString());
            latitude = Float.parseFloat(inputCoordinates.get(1).toString());
            parameters = jsonObject.getJSONArray("parameters");
            for(int i = 0; i < parameters.length(); i++){
                parameter = parameters.getJSONObject(i);
                String oldValidTime = parameter.getString("validTime");
                float oldT = Float.parseFloat(parameter.getString("t"));
                int oldTcc_Mean = parameter.getInt("tcc_mean");
                int oldWymb2 = parameter.getInt("Wsymb2");
                Parameter oldParameter = new Parameter(oldValidTime, oldT, oldTcc_Mean, oldWymb2);
                oldParameters.add(oldParameter);
            }
            mvcModel.setApprovedTime(approvedTime);
            mvcModel.setCoordinates(latitude, longitude);
            System.out.println(oldParameters.get(0).getValidTime());
            mvcModel.getParameterList().addAll(oldParameters);
            System.out.println(mvcModel.getParameterList().get(0).getValidTime());
            mvcView.getForecastListAdapter().notifyDataSetChanged();
            mvcView.bindDataToView();
        } catch (JSONException e) {
            mvcView.messageToast("No Saved Data");
        } catch (IndexOutOfBoundsException e){
            mvcView.messageToast("No Saved Data");
        }

    }

}
