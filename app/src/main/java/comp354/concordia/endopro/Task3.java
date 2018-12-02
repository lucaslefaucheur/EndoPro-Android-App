package comp354.concordia.endopro;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;

import comp354.concordia.endopro.GroupF.Weather.WeatherDatabase;
import comp354.concordia.endopro.GroupF.Weather.WeatherEntity;

public class Task3 extends AppCompatActivity {

    private final String IP = "46.101.183.79";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task3);


        //        Create the link with local database
        WeatherDatabase db = Room.databaseBuilder(getApplicationContext(),
                WeatherDatabase.class, "weather-db").allowMainThreadQueries().build();

        LocalDate now = LocalDate.now();
        WeatherEntity weatherEntity = db.weatherDAO().getWeatherEntity(now.toString());

        //TODO Add method to find the number of rest days
        int rest_day = 2;

//        Query data on new thread
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    // Get predictions from server
                    @SuppressLint("DefaultLocale") JSONObject response = getJSONObjectFromURL(String.format("http://" + IP + "/?data=[%d,%f,%f,%f,%f]",
                            rest_day,
                            weatherEntity.getAvgtemp_c(),
                            weatherEntity.getAvgwind_kph(),
                            weatherEntity.getAvgprecip_mm(),
                            weatherEntity.getAvghumidity()));

                    // Extract prediction result
                    String str = response.getString("predictions");
                    String[] a = str.substring(2, str.length() - 2).split(",");

                    // Update the UI with the UI thread
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            ConstraintLayout load = findViewById(R.id.load);
                            load.setVisibility(View.GONE);

                            // Display average speed
                            TextView avgSpeed = findViewById(R.id.EstmAvgSpeed);
                            avgSpeed.setText(String.format("%.2f Kph", Float.parseFloat(a[0])));

                            // Display average distance
                            TextView avgDist = findViewById(R.id.EstmDist);
                            avgDist.setText(String.format("%.2f Km", Float.parseFloat(a[1])));

                            //TODO get moving average of past 10 days and use it to compare with the predictive result
                            //if both predictions better then moving average -->  GOOD Day (Green)
                            //if one prediction better --> AVERAGE Day (Yellow / Orange)
                            //else BAD day (Red)
                            /*
                            if it's an average day then "today" label is: "Today is an"
                            else "Today is a"
                             */

                        }
                    });


                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });


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
