package comp354.concordia.endopro.GroupF.Weather;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Objects;

@Entity(tableName = "weather_table")
public class WeatherEntity {

    private String city;

    @PrimaryKey(autoGenerate = true)
    private int uid;

    private String date;
    private long date_epoch;
    private float maxtemp_c;
    private float maxtemp_f;
    private float mintemp_c;
    private float mintemp_f;
    private float avgtemp_c;
    private float avgtemp_f;
    private float avgwind_mph;
    private float avgwind_kph;
    private float totalprecip_mm;
    private float avghumidity;

    @Ignore
    public WeatherEntity() {
    }

    public WeatherEntity(String city, String date, float maxtemp_c, float maxtemp_f, float mintemp_c, float mintemp_f, float avgtemp_c, float avgtemp_f, float totalprecip_mm, float avghumidity) {
        this.city = city;
        this.date = date;
        this.maxtemp_c = maxtemp_c;
        this.maxtemp_f = maxtemp_f;
        this.mintemp_c = mintemp_c;
        this.mintemp_f = mintemp_f;
        this.avgtemp_c = avgtemp_c;
        this.avgtemp_f = avgtemp_f;
        this.totalprecip_mm = totalprecip_mm;
        this.avghumidity = avghumidity;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getDate_epoch() {
        return date_epoch;
    }

    public void setDate_epoch(long date_epoch) {
        this.date_epoch = date_epoch;
    }

    public void setDate_epoch(String date) {
        LocalDate date_tmp = LocalDate.parse(date);
        ZoneId zoneId = ZoneId.systemDefault();
        this.date_epoch = date_tmp.atStartOfDay(zoneId).toEpochSecond();
    }

    public float getMaxtemp_c() {
        return maxtemp_c;
    }

    public void setMaxtemp_c(float maxtemp_c) {
        this.maxtemp_c = maxtemp_c;
    }

    public float getMaxtemp_f() {
        return maxtemp_f;
    }

    public void setMaxtemp_f(float maxtemp_f) {
        this.maxtemp_f = maxtemp_f;
    }

    public float getMintemp_c() {
        return mintemp_c;
    }

    public void setMintemp_c(float mintemp_c) {
        this.mintemp_c = mintemp_c;
    }

    public float getMintemp_f() {
        return mintemp_f;
    }

    public void setMintemp_f(float mintemp_f) {
        this.mintemp_f = mintemp_f;
    }

    public float getAvgtemp_c() {
        return avgtemp_c;
    }

    public void setAvgtemp_c(float avgtemp_c) {
        this.avgtemp_c = avgtemp_c;
    }

    public float getAvgtemp_f() {
        return avgtemp_f;
    }

    public void setAvgtemp_f(float avgtemp_f) {
        this.avgtemp_f = avgtemp_f;
    }

    public float getAvgwind_mph() {
        return avgwind_mph;
    }

    public void setAvgwind_mph(float avgwind_mph) {
        this.avgwind_mph = avgwind_mph;
    }

    public float getAvgwind_kph() {
        return avgwind_kph;
    }

    public void setAvgwind_kph(float avgwind_kph) {
        this.avgwind_kph = avgwind_kph;
    }

    public float getTotalprecip_mm() {
        return totalprecip_mm;
    }

    public void setTotalprecip_mm(float totalprecip_mm) {
        this.totalprecip_mm = totalprecip_mm;
    }

    public float getAvghumidity() {
        return avghumidity;
    }

    public void setAvghumidity(float avghumidity) {
        this.avghumidity = avghumidity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WeatherEntity weatherEntity = (WeatherEntity) o;
        return date.equals(weatherEntity.date) &&
                Objects.equals(city.toLowerCase(), weatherEntity.city.toLowerCase());
    }

    @Override
    public int hashCode() {

        return Objects.hash(city, date);
    }

    @NonNull
    @Override
    public String toString() {
        return date;
    }
}
