package comp354.concordia.endopro.GroupF.Weather;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Objects;

@Entity(tableName = "weather_table")
public class WeatherEntity {

    private String city;

    @PrimaryKey(autoGenerate=true)
    private int uid;

    private String date;
    private long date_epoch;

    private float maxtemp_c;
    private float maxtemp_f;
    private float mintemp_c;
    private float mintemp_f;
    private float avgtemp_c;
    private float avgtemp_f;
    private float maxwind_mph;
    private float maxwind_kph;
    private float avgwind_mph;
    private float avgwind_kph;
    private float totalprecip_mm;
    private float totalprecip_in;
    private float avgvis_km;
    private float avgvis_miles;
    private float avghumidity;
    private float uv;

    @Ignore
    public WeatherEntity() {
    }

    @Ignore
    public WeatherEntity(String date) {
        this.date = date;
    }

    public WeatherEntity(String city, String date, long date_epoch, float maxtemp_c, float maxtemp_f, float mintemp_c, float mintemp_f, float avgtemp_c, float avgtemp_f, float maxwind_mph, float maxwind_kph, float totalprecip_mm, float totalprecip_in, float avgvis_km, float avgvis_miles, float avghumidity, float uv) {
        this.city = city;
        this.date = date;
        this.date_epoch = date_epoch;
        this.maxtemp_c = maxtemp_c;
        this.maxtemp_f = maxtemp_f;
        this.mintemp_c = mintemp_c;
        this.mintemp_f = mintemp_f;
        this.avgtemp_c = avgtemp_c;
        this.avgtemp_f = avgtemp_f;
        this.maxwind_mph = maxwind_mph;
        this.maxwind_kph = maxwind_kph;
        this.totalprecip_mm = totalprecip_mm;
        this.totalprecip_in = totalprecip_in;
        this.avgvis_km = avgvis_km;
        this.avgvis_miles = avgvis_miles;
        this.avghumidity = avghumidity;
        this.uv = uv;
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

    public float getMaxwind_mph() {
        return maxwind_mph;
    }

    public void setMaxwind_mph(float maxwind_mph) {
        this.maxwind_mph = maxwind_mph;
    }

    public float getMaxwind_kph() {
        return maxwind_kph;
    }

    public void setMaxwind_kph(float maxwind_kph) {
        this.maxwind_kph = maxwind_kph;
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

    public float getTotalprecip_in() {
        return totalprecip_in;
    }

    public void setTotalprecip_in(float totalprecip_in) {
        this.totalprecip_in = totalprecip_in;
    }

    public float getAvgvis_km() {
        return avgvis_km;
    }

    public void setAvgvis_km(float avgvis_km) {
        this.avgvis_km = avgvis_km;
    }

    public float getAvgvis_miles() {
        return avgvis_miles;
    }

    public void setAvgvis_miles(float avgvis_miles) {
        this.avgvis_miles = avgvis_miles;
    }

    public float getAvghumidity() {
        return avghumidity;
    }

    public void setAvghumidity(float avghumidity) {
        this.avghumidity = avghumidity;
    }

    public float getUv() {
        return uv;
    }

    public void setUv(float uv) {
        this.uv = uv;
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
