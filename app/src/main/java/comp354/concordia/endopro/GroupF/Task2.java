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
import com.github.mikephil.charting.components.LegendEntry;
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
        lineChart = findViewById(R.id.chart);

        final ArrayList<String> xAxes = new ArrayList<>();
        ArrayList<Entry> yAxesTemp = new ArrayList<>();
        ArrayList<Entry> yAxesWindSpeed = new ArrayList<>();
        ArrayList<Entry> yAxesMovingAvgTemp = new ArrayList<>();
        ArrayList<Entry> yAxesMovingAvgWindSpeed = new ArrayList<>();

//        Set data points
        float movingAvgWindSpeed = 0;
        float movingAvgTemp = 0;
        for (int i = 0; i < allWeatherEntities.size(); i++) {
            // Get Entry values
            float avgTempC = allWeatherEntities.get(i).getAvgtemp_c();
            float avgWindSpeedKph = allWeatherEntities.get(i).getAvgwind_kph();

            // Adjust moving average
            movingAvgTemp += avgTempC;
            movingAvgWindSpeed += avgWindSpeedKph;

            // Format x labels
            String dateStr = allWeatherEntities.get(i).getDate();
            LocalDate date = LocalDate.parse(dateStr);
            String x = date.getMonth().name().substring(0,3) + " " +  dateStr.substring(8);

            // Add entry to graph
            yAxesTemp.add(new Entry(i, (int) avgTempC));
            yAxesWindSpeed.add(new Entry(i, (int) avgWindSpeedKph));
            yAxesMovingAvgTemp.add(new Entry(i, (int) movingAvgTemp / (i+1)));
            yAxesMovingAvgWindSpeed.add(new Entry(i, (int) movingAvgWindSpeed / (i+1)));
            xAxes.add(i, x);
        }

        ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();

//        Plot style average temperature
        LineDataSet lineDataSetTemp = new LineDataSet(yAxesTemp, "Average Temperature " + getString(R.string.degreeC));
        lineDataSetTemp.setCircleColor(Color.rgb(255, 176, 144));
        lineDataSetTemp.setColor(Color.rgb(255, 176, 144));
        lineDataSetTemp.setDrawValues(false);
        lineDataSetTemp.setLineWidth(3);

//        Plot style average wind speed
        LineDataSet lineDataSetWindSpeed = new LineDataSet(yAxesWindSpeed, "Average Wind Speed Kph");
        lineDataSetWindSpeed.setCircleColor(Color.rgb(190, 180, 232));
        lineDataSetWindSpeed.setColor(Color.rgb(190, 180, 232));
        lineDataSetWindSpeed.setDrawValues(false);
        lineDataSetWindSpeed.setLineWidth(3);


        float lineLength = 20;
        float spaceLength = 30;
        float phase = 0;

        //        Plot style average temperature
        LineDataSet lineDataSetMovingAvgTemp = new LineDataSet(yAxesMovingAvgTemp, "Average Temperature " + getString(R.string.degreeC));
        lineDataSetMovingAvgTemp.setDrawCircles(false);
        lineDataSetMovingAvgTemp.setColor(Color.argb(216,255, 176, 144));
        lineDataSetMovingAvgTemp.setDrawValues(false);
        lineDataSetMovingAvgTemp.setLineWidth(3);
        lineDataSetMovingAvgTemp.enableDashedLine(lineLength, spaceLength, phase);

//        Plot style average wind speed
        LineDataSet lineDataSetMovingAvgWindSpeed = new LineDataSet(yAxesMovingAvgWindSpeed, "Average Wind Speed Kph");
        lineDataSetMovingAvgWindSpeed.setDrawCircles(false);
        lineDataSetMovingAvgWindSpeed.setColor(Color.argb(216,190, 180, 232));
        lineDataSetMovingAvgWindSpeed.setDrawValues(false);
        lineDataSetMovingAvgWindSpeed.setLineWidth(3);
        lineDataSetMovingAvgWindSpeed.enableDashedLine(lineLength, spaceLength, phase);

//        Add line to graph
        lineDataSets.add(lineDataSetTemp);
        lineDataSets.add(lineDataSetWindSpeed);
        lineDataSets.add(lineDataSetMovingAvgTemp);
        lineDataSets.add(lineDataSetMovingAvgWindSpeed);

//        Styling
        lineChart.setData(new LineData(lineDataSets));
        lineChart.setViewPortOffsets(75, 25, 75, 75);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        // Legend
        Legend legend = lineChart.getLegend();
        legend.setPosition(Legend.LegendPosition.ABOVE_CHART_RIGHT);
        legend.setForm(Legend.LegendForm.LINE);
        
        int[] legendColor = { Color.rgb(255, 176, 144), Color.rgb(190, 180, 232)};
        String[] legendLabel ={"Temperature", "Wind speed"};
        List<LegendEntry> legendEntries = new ArrayList<>();
        for (int i = 0; i < legendColor.length; i++) {
            LegendEntry entry = new LegendEntry();
            entry.label = legendLabel[i];
            entry.form = Legend.LegendForm.LINE;
            entry.formColor = legendColor[i];
            legendEntries.add(entry);
        }
        legend.setCustom(legendEntries);


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
