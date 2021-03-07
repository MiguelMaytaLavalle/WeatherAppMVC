package model.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import model.Parameter;

public class JsonParser {

    /*public static List<Forecast> getParsedParameters(JSONObject parsedObject){
        JSONObject root = parsedObject;


    }*/

    public static String getParsedApprovedTime(JSONObject response) {
        String oldApprovedTime = null;
        JSONObject root = response;
        String validDate = "", validHour = "";

        try {
            oldApprovedTime = root.get("approvedTime").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        int iendT = oldApprovedTime.indexOf("T"), iendZ = oldApprovedTime.indexOf("Z");

        if (iendT != -1) {
            validDate = oldApprovedTime.substring(0, iendT);
            //System.out.println("Valid date: " + validDate);
            validHour = oldApprovedTime.substring(iendT + 1, iendZ);
            //System.out.println("Valid hour: " + validHour);
        }
        return validDate + " " + validHour;
    }

    /**
     * Parse the response and return a list with the
     * relevant parameters when displaying the
     * weather forecast in the UI
     * @param response
     * @return
     * @throws JSONException
     */
    public static List<Parameter> getParameterList(JSONObject response) throws JSONException {
        JSONObject root = response;
        JSONArray timeSeries = root.getJSONArray("timeSeries");
        List<Parameter> parsedList = new ArrayList<>();

        for (int i = 0; i < timeSeries.length(); i++) {
            JSONObject parametersAtTime = timeSeries.getJSONObject(i);
            String validTime = parametersAtTime.getString("validTime");
            String parsedValidTime = getParsedValidTime(validTime);
            JSONArray parameters = parametersAtTime.getJSONArray("parameters");
            Parameter parameter = getParsedParameter(parameters, parsedValidTime);
            parsedList.add(parameter);
        }
        return parsedList;
    }

    /**
     * Removes the letter T and Z from the parsed ValidTime
     * and returns it back with date and time
     * @param validTime
     * @return
     */
    private static String getParsedValidTime(String validTime) {
        int iendT = validTime.indexOf("T"), iendZ = validTime.indexOf("Z");
        String validDate = "", validHour = "";

        if (iendT != -1) {
            validDate = validTime.substring(0, iendT);
            //System.out.println("Valid date: " + validDate);
            validHour = validTime.substring(iendT + 1, iendZ);
            //System.out.println("Valid hour: " + validHour);
        } else {
            return validTime;
        }
        String parsedValidTime = validDate + " " + validHour;
        return parsedValidTime;
    }

    /**
     * Fetches the relevant values (t, tcc_mean and Wsymb2) and returns
     * it in a Parameter object.
     * @param parameters
     * @param validTime
     * @return
     */
    private static Parameter getParsedParameter(JSONArray parameters, String validTime) {
        int t = 0, tcc_mean = 0, Wsymb2 = 0;
        Parameter parsedParameter = null;
        try {
            for (int j = 0; j < parameters.length(); j++) {
                JSONObject parameter = parameters.getJSONObject(j);
                String name = parameter.get("name").toString();
                switch (name) {
                    case "t":
                        t = parameter.getJSONArray("values").getInt(0);
                        break;
                    case "tcc_mean":
                        tcc_mean = parameter.getJSONArray("values").getInt(0);
                        break;
                    case "Wsymb2":
                        Wsymb2 = parameter.getJSONArray("values").getInt(0);
                        break;
                }
            }
            parsedParameter = new Parameter(validTime, t, tcc_mean, Wsymb2);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return parsedParameter;
    }

}
