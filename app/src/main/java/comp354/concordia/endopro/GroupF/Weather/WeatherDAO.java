package comp354.concordia.endopro.GroupF.Weather;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface WeatherDAO {

    @Insert
    void insert(WeatherEntity entity);

    @Query("SELECT * from weather_table ORDER BY date_epoch ASC")
    List<WeatherEntity> getAllWeatherEntities();

    @Query("SELECT * from weather_table WHERE date LIKE :date")
    WeatherEntity getWeatherEntity(String date);

    @Query("DELETE FROM weather_table WHERE date_epoch < :date_epoch")
    void cleanDB(long date_epoch);

}
