package comp354.concordia.endopro;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;

import comp354.concordia.endopro.Common.EndoProWorkout;
import comp354.concordia.endopro.Common.User;
import comp354.concordia.endopro.GroupF.MainPage;
import comp354.concordia.endopro.GroupF.Weather.WeatherDatabase;
import comp354.concordia.endopro.GroupF.Weather.WeatherEntity;

public class Task3 extends AppCompatActivity {

    private final String IP = "46.101.183.79";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task3);

        // Hide background view
        ConstraintLayout notEnoughData = findViewById(R.id.notEnoughData);
        notEnoughData.setVisibility(View.GONE);
        LinearLayout predictInfo = findViewById(R.id.predictInfo);
        predictInfo.setVisibility(View.GONE);

        // Retrieve user workouts
        User user = User.getInstance();
        ArrayList<EndoProWorkout> workouts = user.getWorkouts();

        // Do prediction
        if (workouts.size() > 0) {

            //        Create the link with local database
            WeatherDatabase db = Room.databaseBuilder(getApplicationContext(),
                    WeatherDatabase.class, "weather-db").allowMainThreadQueries().build();

            LocalDate now = LocalDate.now();
            WeatherEntity weatherEntity = db.weatherDAO().getWeatherEntity(now.toString());

            // Calculate rest days
            LocalDate lastWorkoutDate = LocalDate.parse(workouts.get(0).getStartTime().substring(0, 10));
            int rest_day = Period.between(lastWorkoutDate, LocalDate.now()).getDays();


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
                        String[] predictions = str.substring(2, str.length() - 2).split(",");
                        float avgSpeedPredict = Float.parseFloat(predictions[0]);
                        float distancePredict = Float.parseFloat(predictions[1]);

                        // Calculate moving average of speed and distance for last X workouts
                        float movingAvgSpeed = 0;
                        float movingAvgDist = 0;
                        int avgSize = 10;
                        int nbWorkouts = workouts.size();

                        if (nbWorkouts < avgSize) {

                            for (EndoProWorkout w : workouts) {
                                movingAvgSpeed += w.getSpeedAverage();
                                movingAvgDist += w.getDistance();
                            }
                        } else {

                            for (EndoProWorkout w : workouts.subList(nbWorkouts - avgSize, nbWorkouts)) {
                                movingAvgSpeed += w.getSpeedAverage();
                                movingAvgDist += w.getDistance();
                            }
                        }

                        movingAvgSpeed /= workouts.size();
                        movingAvgDist /= workouts.size();

                        final int performanceIndex;

                        if (avgSpeedPredict > movingAvgSpeed && distancePredict > movingAvgDist)
                            performanceIndex = 1;
                        else if (avgSpeedPredict < movingAvgSpeed && distancePredict < movingAvgDist)
                            performanceIndex = -1;
                        else
                            performanceIndex = 0;


                        // Update the UI with the UI thread
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                // Hide loading screen and show view
                                predictInfo.setVisibility(View.VISIBLE);
                                ConstraintLayout load = findViewById(R.id.load);
                                load.setVisibility(View.GONE);

                                // Display average speed
                                TextView avgSpeed = findViewById(R.id.EstmAvgSpeed);
                                avgSpeed.setText(String.format("%.2f Kph", avgSpeedPredict));

                                // Display average distance
                                TextView avgDist = findViewById(R.id.EstmDist);
                                avgDist.setText(String.format("%.2f Km", distancePredict));

                                // Change first line text to avoid typos with a/an

                                TextView perf = findViewById(R.id.perf);
                                // Set performance value

                                switch (performanceIndex) {
                                    // Bad day
                                    case -1:
                                        perf.setText("BAD");
                                        perf.setTextColor(0xffcc0000);
                                        break;

                                    // Average day
                                    case 0:
                                        perf.setText("AVERAGE");
                                        perf.setTextColor(0xffffbb33);
                                        break;

                                    // Good day
                                    case 1:
                                        perf.setText("GOOD");
                                        perf.setTextColor(0xff99cc00);
                                        break;
                                }
                            }
                        });


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            // Hide loading screen and show view
            notEnoughData.setVisibility(View.VISIBLE);
            ConstraintLayout load = findViewById(R.id.load);
            load.setVisibility(View.GONE);
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
