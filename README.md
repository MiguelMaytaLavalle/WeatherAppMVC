# General
Develop a mobile application that uses this data to present weather forecast data for a 10 day period. In the simplest form of the application the user enters longitude and latitude and the application, in a list, displays time, temperature and an image representing the cloud coverage. Display an error message if the coordinates are out of range or some similar error occurs.

Application in portrait mode
<img src="https://github.com/MiguelMaytaLavalle/WeatherAppMVC/blob/main/app/src/main/java/preview/Portrait.jpg" width="200" height="300">

# Requierements
* The mobile application is built following the MVC design pattern.
* The mobile application uses the Swedish Meteorological and Hydrological Institute, SMHI, API which publishes hourly updated weather forecaast in JSON-format at https://opendata-download-metfcst.smhi.se/.
* Uses the Volley framework for making queries to download data over the network. It stores it as a backup in case of there's no network.
* When in "offline" mode, use the stored data, but informs the user that the data is out of date.
* Forecast data is presented in a scrollable list (RecyclerView), which each list element contains time, temperature and information representing the cloud cover.
* The UI can be presented in both landscape or portrait mode.
* Handles exceptions to prevent the application from crashing in case:
  - the network fails
  - user input is errorneous or missing

# Thoughts
There are many more features that I wished I had time to implement, such as:
* automaticlly find your position when first opening the app
* enter name of location instead of coordinates
* able to store favorite locations 
* being able to adjust preferences regarding the UI, e.g. the time interval between updates.

In due time I will probably return back and finish it up
