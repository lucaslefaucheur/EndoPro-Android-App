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
                JSONObject response = getJSONObjectFromURL("https://api.apixu.com/v1/history.json?key=0f3452d18d3043f4a9560342180711&q=Montreal&dt=" + date);

//                Parse the JSON response
                WeatherEntity newEntry = new WeatherEntity();

                newEntry.setCity(response.getJSONObject("location").getString("name"));

                JSONObject forecastday = response.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(0);

                newEntry.setDate(forecastday.getString("date"));
                newEntry.setDate_epoch(forecastday.getLong("date_epoch"));

                JSONObject day = forecastday.getJSONObject("day");

                newEntry.setMaxtemp_c(Float.parseFloat(day.getString("maxtemp_c")));
                newEntry.setMaxtemp_f(Float.parseFloat(day.getString("maxtemp_f")));
                newEntry.setMintemp_c(Float.parseFloat(day.getString("mintemp_c")));
                newEntry.setMintemp_f(Float.parseFloat(day.getString("mintemp_f")));
                newEntry.setAvgtemp_c(Float.parseFloat(day.getString("avgtemp_c")));
                newEntry.setAvgtemp_f(Float.parseFloat(day.getString("avgtemp_f")));


                float avgWindSpeedKph = 0;
                float avgWindSpeedMph = 0;
                JSONArray hour = forecastday.getJSONArray("hour");
                for (int i = 0; i < hour.length(); i++) {
                    avgWindSpeedKph += Float.parseFloat(hour.getJSONObject(i).getString("wind_kph"));
                    avgWindSpeedMph += Float.parseFloat(hour.getJSONObject(i).getString("wind_mph"));
                }
                newEntry.setAvgwind_kph(avgWindSpeedKph / hour.length());
                newEntry.setAvgwind_mph(avgWindSpeedMph / hour.length());

                newEntry.setMaxwind_mph(Float.parseFloat(day.getString("maxwind_mph")));
                newEntry.setMaxwind_kph(Float.parseFloat(day.getString("maxwind_kph")));

                newEntry.setTotalprecip_mm(Float.parseFloat(day.getString("totalprecip_mm")));
                newEntry.setTotalprecip_in(Float.parseFloat(day.getString("totalprecip_in")));
                newEntry.setAvgvis_km(Float.parseFloat(day.getString("avgvis_km")));
                newEntry.setAvgvis_miles(Float.parseFloat(day.getString("avgvis_miles")));
                newEntry.setAvghumidity(Float.parseFloat(day.getString("avghumidity")));
                newEntry.setUv(Float.parseFloat(day.getString("uv")));

//                Add the Entry to local database
                new InsertDbAsync(db, newEntry).execute();

            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }


//            weatherDAO.insert(new_entity);
        }

    }

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
        System.out.println("JSON: " + jsonString);

        urlConnection.disconnect();

        return new JSONObject(jsonString);
    }
}
