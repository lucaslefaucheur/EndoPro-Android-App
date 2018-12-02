package comp354.concordia.endopro.GroupF.Weather.DatabaseAction;

import android.os.AsyncTask;

import comp354.concordia.endopro.GroupF.Weather.WeatherDAO;
import comp354.concordia.endopro.GroupF.Weather.WeatherDatabase;


public final class CleanDBAsync extends AsyncTask<Void, Void, Void> {

    private long date_epoch;
    private WeatherDatabase db;

    public CleanDBAsync(WeatherDatabase db, long date_epoch) {
        this.db = db;
        this.date_epoch = date_epoch;
    }

    @Override
    protected Void doInBackground(final Void... params) {
        WeatherDAO weatherDAO = db.weatherDAO();
        weatherDAO.cleanDB(date_epoch);
        return null;
    }
}
