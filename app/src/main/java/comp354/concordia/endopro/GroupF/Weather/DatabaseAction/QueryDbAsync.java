package comp354.concordia.endopro.GroupF.Weather.DatabaseAction;

import android.os.AsyncTask;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import comp354.concordia.endopro.GroupF.Weather.WeatherDAO;
import comp354.concordia.endopro.GroupF.Weather.WeatherDatabase;
import comp354.concordia.endopro.GroupF.Weather.WeatherEntity;

public final class QueryDbAsync extends AsyncTask<Void, Void, WeatherEntity> {

    private final String API_URL = "https://api.worldweatheronline.com/premium/v1/past-weather.ashx";
    private final String API_KEY = "5cc8bb3a2dbd47bb942200929182911";

    private final String CITY = "Montreal";

    private String date;
    private WeatherDatabase db;

    public QueryDbAsync(WeatherDatabase db, String date) {
        this.db = db;
        this.date = date;
    }

    @Override
    protected WeatherEntity doInBackground(final Void... params) {
        WeatherDAO weatherDAO = db.weatherDAO();
        return weatherDAO.getWeatherEntity(date);
    }

    @Override
    protected void onPostExecute(WeatherEntity entity) {
        if (entity == null) {


            try {
                JSONObject response = getJSONObjectFromURL(
                        String.format("%s" +
                                        "?key=%s" +
                                        "&q=%s" + // city
                                        "&format=json" +
                                        "&extra=utcDateTime" +
                                        "&date=%s" +
                                        "&tp=1", // tp=1 for hourly data
                                API_URL,
                                API_KEY,
                                CITY,
                                date));

//                Parse the JSON response
                WeatherEntity newEntry = new WeatherEntity();

                // JSON crawler
                JSONObject data = response.getJSONObject("data");
                JSONObject weather = data.getJSONArray("weather").getJSONObject(0);
                JSONArray hourly = weather.getJSONArray("hourly");

                // City
                newEntry.setCity(data.getJSONArray("request").getJSONObject(0).getString("query"));

                // Date
                newEntry.setDate(date);
                newEntry.setDate_epoch(date);

                // Temperature
                newEntry.setMaxtemp_c(Float.parseFloat(weather.getString("maxtempC")));
                newEntry.setMaxtemp_f(Float.parseFloat(weather.getString("maxtempF")));
                newEntry.setMintemp_c(Float.parseFloat(weather.getString("mintempC")));
                newEntry.setMintemp_f(Float.parseFloat(weather.getString("mintempF")));

                float avgTempC = 0;
                float avgTempF = 0;
                for (int i = 0; i < hourly.length(); i++) {
                    avgTempC += Float.parseFloat(hourly.getJSONObject(i).getString("tempC"));
                    avgTempF += Float.parseFloat(hourly.getJSONObject(i).getString("tempF"));
                }
                newEntry.setAvgtemp_c(avgTempC / hourly.length());
                newEntry.setAvgtemp_f(avgTempF / hourly.length());

                // Wind speed
                float avgWindSpeedKph = 0;
                float avgWindSpeedMph = 0;
                for (int i = 0; i < hourly.length(); i++) {
                    avgWindSpeedKph += Float.parseFloat(hourly.getJSONObject(i).getString("windspeedKmph"));
                    avgWindSpeedMph += Float.parseFloat(hourly.getJSONObject(i).getString("windspeedMiles"));
                }
                newEntry.setAvgwind_kph(avgWindSpeedKph / hourly.length());
                newEntry.setAvgwind_mph(avgWindSpeedMph / hourly.length());

                // Precipitation
                float precipMM = 0;
                for (int i = 0; i < hourly.length(); i++) {
                    precipMM += Float.parseFloat(hourly.getJSONObject(i).getString("precipMM"));
                }
                newEntry.setAvgprecip_mm(precipMM / hourly.length());

                // Humidity
                float avg_humidity = 0;
                for (int i = 0; i < hourly.length(); i++) {
                    avg_humidity += Float.parseFloat(hourly.getJSONObject(i).getString("humidity"));

                }
                newEntry.setAvghumidity(avg_humidity / hourly.length());

//                Add the Entry to local database
                new InsertDbAsync(db, newEntry).execute();

            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
        }

    }

    /*
    Wrapper to do get JSON object from an HTTP request
     */
    private JSONObject getJSONObjectFromURL(String urlString) throws IOException, JSONException {
        HttpURLConnection urlConnection = null;
        URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setReadTimeout(10000 /* milliseconds */);
        urlConnection.setConnectTimeout(15000 /* milliseconds */);
        urlConnection.setDoOutput(true);
        urlConnection.connect();

        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }
        br.close();

        String jsonString = sb.toString();

        urlConnection.disconnect();

        return new JSONObject(jsonString);
    }
}
