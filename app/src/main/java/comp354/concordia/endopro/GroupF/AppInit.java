package comp354.concordia.endopro.GroupF;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.time.LocalDate;
import java.time.ZoneId;

import comp354.concordia.endopro.GroupF.Weather.DatabaseAction.CleanDBAsync;
import comp354.concordia.endopro.GroupF.Weather.DatabaseAction.QueryDbAsync;
import comp354.concordia.endopro.GroupF.Weather.WeatherDatabase;
import comp354.concordia.endopro.GroupF.Weather.WeatherEntity;
import comp354.concordia.endopro.R;

public class AppInit extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_app_init);

//        Change thread policy to allow https request
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

//        Check if the user is already logged in
        Boolean Registered;
        SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
        Registered = sp.getBoolean("Registered", false);

//        Create the link with local database
        WeatherDatabase db = Room.databaseBuilder(getApplicationContext(),
                WeatherDatabase.class, "weather-db").build();

//        Get today's and the past X's days data.
        LocalDate curr = LocalDate.now();

        int nbWeatherEntries = 7;
        WeatherEntity[] dataEntries = new WeatherEntity[nbWeatherEntries];
        for (int i = 0; i < nbWeatherEntries; i++) {

            String day = curr.minusDays(i).toString();
            new QueryDbAsync(db, day).execute();
        }

//        Clean the database from unused entries
        new CleanDBAsync(db, curr.minusDays(nbWeatherEntries).atStartOfDay(ZoneId.systemDefault()).toEpochSecond()).execute();



//        Redirect to correct view
        if (!Registered) {
//            startActivity(new Intent(this, Login.class));
        } else {
            startActivity(new Intent(this, MainPage.class));
        }

    }
}
