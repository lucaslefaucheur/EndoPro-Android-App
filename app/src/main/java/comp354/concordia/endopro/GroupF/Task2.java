package comp354.concordia.endopro.GroupF;

import android.arch.persistence.room.Room;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import comp354.concordia.endopro.GroupF.Weather.WeatherDatabase;
import comp354.concordia.endopro.GroupF.Weather.WeatherEntity;
import comp354.concordia.endopro.R;

public class Task2 extends AppCompatActivity {

    LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_task2);

//        Create the link with local database
        WeatherDatabase db = Room.databaseBuilder(getApplicationContext(),
                WeatherDatabase.class, "weather-db").allowMainThreadQueries().build();

        List<WeatherEntity> allWeatherEntities = db.weatherDAO().getAllWeatherEntities();


//        Display daily statistics
        WeatherEntity today = allWeatherEntities.get(allWeatherEntities.size() - 1);

        TextView avgTemp = findViewById(R.id.avgTempValue);
        avgTemp.setText(String.format("%.0f%s", today.getAvgtemp_c(), getString(R.string.degreeC)));

        TextView highTemp = findViewById(R.id.highTempValue);
        highTemp.setText(String.format("%.0f%s", today.getMaxtemp_c(), getString(R.string.degreeC)));

        TextView minTemp = findViewById(R.id.lowTempValue);
        minTemp.setText(String.format("%.0f%s", today.getMintemp_c(), getString(R.string.degreeC)));

        TextView avgWindSpeed = findViewById(R.id.avgWindSpeedValue);
        avgWindSpeed.setText(String.format("%.0f Kph", today.getAvgwind_kph()));


//        Display graph
        lineChart = (LineChart) findViewById(R.id.chart);

        final ArrayList<String> xAxes = new ArrayList<>();
        ArrayList<Entry> yAxesTemp = new ArrayList<>();
        ArrayList<Entry> yAxesWindSpeed = new ArrayList<>();

        for (int i = 0; i < allWeatherEntities.size(); i++) {
            float avgTempC = allWeatherEntities.get(i).getAvgtemp_c();
            float avgWindSpeedKph = allWeatherEntities.get(i).getAvgwind_kph();
            String dateStr = allWeatherEntities.get(i).getDate();

            LocalDate date = LocalDate.parse(dateStr);
            String x = date.getDayOfWeek().name().substring(0, 3);

            int reverseI = allWeatherEntities.size() - (i + 1);

            yAxesTemp.add(new Entry(i, (int) avgTempC));
            yAxesWindSpeed.add(new Entry(i, (int) avgWindSpeedKph));
            xAxes.add(i, x);
        }

        ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();

        LineDataSet lineDataSetTemp = new LineDataSet(yAxesTemp, "Average Temperature " + getString(R.string.degreeC));
        lineDataSetTemp.setDrawCircles(false);
        lineDataSetTemp.setColor(Color.rgb(52, 202, 255));
        lineDataSetTemp.setDrawValues(false);
        lineDataSetTemp.setLineWidth(3);

        LineDataSet lineDataSetWindSpeed = new LineDataSet(yAxesWindSpeed, "Average Wind Speed Kph");
        lineDataSetWindSpeed.setDrawCircles(false);
        lineDataSetWindSpeed.setColor(Color.rgb(232, 35, 33));
        lineDataSetWindSpeed.setDrawValues(false);
        lineDataSetWindSpeed.setLineWidth(3);

        lineDataSets.add(lineDataSetTemp);
        lineDataSets.add(lineDataSetWindSpeed);

//        Styling
        lineChart.setData(new LineData(lineDataSets));
        lineChart.setViewPortOffsets(50, 25, 25, 75);

        lineChart.getAxisRight().setEnabled(false);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.getLegend().setPosition(Legend.LegendPosition.RIGHT_OF_CHART_INSIDE);
        lineChart.getDescription().setEnabled(false);
        lineChart.setScaleEnabled(false);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xAxes.get((int) value);
            }
        });

    }
}
