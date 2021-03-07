package model.singleton;

import java.util.ArrayList;
import java.util.List;

import model.Forecast;

public class ForecastSingleton {
    private static Forecast theForecast;

    private ForecastSingleton() {
    }

    public static Forecast getInstance()
    {
        if (theForecast == null)
            theForecast = new Forecast();
        return theForecast;
    }
}
