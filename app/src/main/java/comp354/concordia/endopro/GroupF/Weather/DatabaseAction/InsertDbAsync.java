package comp354.concordia.endopro.GroupF.Weather.DatabaseAction;

import android.os.AsyncTask;


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

public final class InsertDbAsync extends AsyncTask<Void, Void, Void> {

    private WeatherEntity entity;
    private WeatherDatabase db;

    public InsertDbAsync(WeatherDatabase db, WeatherEntity entity) {
        this.db = db;
        this.entity = entity;
    }

    @Override
    protected Void doInBackground(final Void... params) {
        WeatherDAO weatherDAO = db.weatherDAO();
        weatherDAO.insert(entity);
        return null;
    }
}
