package com.example.weatherappmvc;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import model.Forecast;
import model.Parameter;

public class JsonRW {
    private static final String FILE_NAME = "forecast_report.json";

    public JsonRW() { }

    /**
     * Saves the forecast object in an serialized object
     * @param context
     * @param forecast
     */
    public static void writeToFile(Context context, Forecast forecast){
        JSONObject saveToFile = new JSONObject();
        FileOutputStream fos = null;
        JSONArray paramArray = new JSONArray();
        JSONArray coordinates = new JSONArray();
        JSONArray arrayCoordinates = new JSONArray();
        JSONObject geometry = new JSONObject();

        List<Parameter> parameterList = forecast.getParameters();
        try {
            saveToFile.put("approvedTime", forecast.getApprovedTime());
            arrayCoordinates.put(0, forecast.getCoordinates().getLongitude());
            arrayCoordinates.put(1, forecast.getCoordinates().getLatitude());
            coordinates.put(arrayCoordinates);
            geometry.put("coordinates", coordinates);
            saveToFile.put("geometry", geometry);
            for(int i = 0; i < parameterList.size(); i++){
                JSONObject parameter = new JSONObject();
                parameter.put("validTime", parameterList.get(i).getValidTime());
                parameter.put("t", parameterList.get(i).getT());
                parameter.put("tcc_mean", parameterList.get(i).getTcc_mean());
                parameter.put("Wsymb2", parameterList.get(i).getWsymb2());
                paramArray.put(i, parameter);
            }
            saveToFile.put("parameters", paramArray);

            fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            fos.write(saveToFile.toString().getBytes());
            fos.flush();
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if(fos != null){
                fos = null;
            }
        }
    }

    /**
     * When invoked it will read from the saved file
     * and return it back as an JSONObject.
     * @param context
     * @return
     */

    public static JSONObject readFromFile(Context context){
        String ret = "";
        JSONObject jsonObject = null;
        try {
            InputStream inputStream = context.openFileInput(FILE_NAME);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append("\n").append(receiveString);
                }
                inputStream.close();
                ret = stringBuilder.toString();
                jsonObject = new JSONObject(ret);
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
            Toast.makeText(context, "Could Not Find Saved File", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //System.out.println("READ FROM FILE: " + jsonObject.toString());
        return jsonObject;
    }

    /**
     * Just used to find the correct folder
     * when searching for the file.
     * Not used for anything else.
     * @param context
     * @param filename
     * @return
     */
    public static boolean isFilePresent(Context context, String filename){
        String path = context.getFilesDir().getAbsolutePath() + "/" + filename;
        File file = new File(path);
        System.out.println(path);
        return file.exists();
    }

}
