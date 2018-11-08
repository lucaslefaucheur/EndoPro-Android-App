package comp354.concordia.endopro.Lucas;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Arrays;

import comp354.concordia.endopro.Common.EndoProWorkout;
import comp354.concordia.endopro.Common.User;

public class EndoProGraph {

    ArrayList<EndoProWorkout> workouts;
    GraphView graph;

    public EndoProGraph(GraphView graph, User user) {
        this.graph = graph;
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setYAxisBoundsManual(true);

        workouts = user.getWorkouts(); // user.getWorkouts_Filtered();

        organizeWorkouts();
    }

    public void organizeWorkouts() {
        // organize in chronological order

    }

    public void createHistorySpeedGraph() {
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>();
        series.setDrawDataPoints(true);

        double x = 0, y;

        for (int i = 0; i < workouts.size(); i++) {
            x++;
            y = workouts.get(i).getSpeedAverage();
            series.appendData(new DataPoint(x, y), true, workouts.size());
        }

        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    return null;
                } else {
                    return super.formatLabel(value, isValueX) + " km/h";
                }
            }
        });

        graph.removeAllSeries();
        graph.setTitle("History of all average speeds");
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(x+1);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(40);
        graph.addSeries(series);
    }

    public void createYearlySpeedGraph() {
        BarGraphSeries<DataPoint> series = new BarGraphSeries<DataPoint>();

        double [] sum = new double [5];
        Arrays.fill(sum, 0.0);
        int [] quantity = new int [5];
        Arrays.fill(quantity, 0);

        for (int i = 0; i < workouts.size(); i++) {
            int year = Integer.parseInt(workouts.get(i).getStartTime().substring(0, 4));
            if (year <= 2018 && year >= 2014) {
                sum[year-2014] += workouts.get(i).getSpeedAverage();
                quantity[year-2014] ++;
            }
        }

        for (int i = 0; i < 5; i++) {
            series.appendData(new DataPoint((i+2014), (sum[i]/quantity[i])), true, 5);
        }

        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    return super.formatLabel(value, isValueX);
                } else {
                    return super.formatLabel(value, isValueX) + " km/h";
                }
            }
        });

        graph.removeAllSeries();
        graph.setTitle("Yearly averages of average speeds");
        graph.getViewport().setMinX(2013);
        graph.getViewport().setMaxX(2019);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(40);
        graph.addSeries(series);
    }

    public void createMonthlySpeedGraph() {
        BarGraphSeries<DataPoint> series = new BarGraphSeries<DataPoint>();

        double [] sum = new double [12];
        Arrays.fill(sum, 0.0);
        int [] quantity = new int [12];
        Arrays.fill(quantity, 0);

        for (int i = 0; i < workouts.size(); i++) {
            int month = Integer.parseInt(workouts.get(i).getStartTime().substring(5, 7));
            int year = Integer.parseInt(workouts.get(i).getStartTime().substring(0, 4));

            if (year == 2018 && month >= 1 && month <=12) {
                sum[month-1] += workouts.get(i).getSpeedAverage();
                quantity[month-1] ++;
            }
        }

        for (int i = 0; i < 12; i++) {
            series.appendData(new DataPoint((i+1), (sum[i]/quantity[i])), true, 12);
        }

        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    if (value == 1) return "JAN";
                    else if (value == 2) return "FEB";
                    else if (value == 3) return "MAR";
                    else if (value == 4) return "APR";
                    else if (value == 5) return "MAY";
                    else if (value == 6) return "JUN";
                    else if (value == 7) return "JUL";
                    else if (value == 8) return "AUG";
                    else if (value == 9) return "SEP";
                    else if (value == 10) return "OCT";
                    else if (value == 11) return "NOV";
                    else if (value == 12) return "DEC";
                    else return null;

                } else {
                    return super.formatLabel(value, isValueX) + " km/h";
                }
            }
        });

        graph.removeAllSeries();
        graph.setTitle("Monthly averages of average speeds");
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(13);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(40);
        graph.addSeries(series);
    }

    public void createHistoryDistanceGraph() {
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>();
        series.setDrawDataPoints(true);

        double x = 0, y;

        for (int i = 0; i < workouts.size(); i++) {
            x++;
            y = workouts.get(i).getDistance();
            series.appendData(new DataPoint(x, y), true, workouts.size());
        }

        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    return null;
                } else {
                    return super.formatLabel(value, isValueX) + " km/h";
                }
            }
        });

        graph.removeAllSeries();
        graph.setTitle("History of all distances");
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(x+1);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(200);
        graph.addSeries(series);
    }

    public void createYearlyDistanceGraph() {
        BarGraphSeries<DataPoint> series = new BarGraphSeries<DataPoint>();

        double [] sum = new double [5];
        Arrays.fill(sum, 0.0);
        int [] quantity = new int [5];
        Arrays.fill(quantity, 0);

        for (int i = 0; i < workouts.size(); i++) {
            int year = Integer.parseInt(workouts.get(i).getStartTime().substring(0, 4));
            if (year <= 2018 && year >= 2014) {
                sum[year-2014] += workouts.get(i).getDistance();
                quantity[year-2014] ++;
            }
        }

        for (int i = 0; i < 5; i++) {
            series.appendData(new DataPoint((i+2014), (sum[i]/quantity[i])), true, 5);
        }

        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    return super.formatLabel(value, isValueX);
                } else {
                    return super.formatLabel(value, isValueX) + " km/h";
                }
            }
        });

        graph.removeAllSeries();
        graph.setTitle("Yearly averages of distances");
        graph.getViewport().setMinX(2013);
        graph.getViewport().setMaxX(2019);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(200);
        graph.addSeries(series);
    }

    public void createMonthlyDistanceGraph() {
        BarGraphSeries<DataPoint> series = new BarGraphSeries<DataPoint>();

        double [] sum = new double [12];
        Arrays.fill(sum, 0.0);
        int [] quantity = new int [12];
        Arrays.fill(quantity, 0);

        for (int i = 0; i < workouts.size(); i++) {
            int month = Integer.parseInt(workouts.get(i).getStartTime().substring(5, 7));
            int year = Integer.parseInt(workouts.get(i).getStartTime().substring(0, 4));

            if (year == 2018 && month >= 1 && month <=12) {
                sum[month-1] += workouts.get(i).getDistance();
                quantity[month-1] ++;
            }
        }

        for (int i = 0; i < 12; i++) {
            series.appendData(new DataPoint((i+1), (sum[i]/quantity[i])), true, 12);
        }

        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    if (value == 1) return "JAN";
                    else if (value == 2) return "FEB";
                    else if (value == 3) return "MAR";
                    else if (value == 4) return "APR";
                    else if (value == 5) return "MAY";
                    else if (value == 6) return "JUN";
                    else if (value == 7) return "JUL";
                    else if (value == 8) return "AUG";
                    else if (value == 9) return "SEP";
                    else if (value == 10) return "OCT";
                    else if (value == 11) return "NOV";
                    else if (value == 12) return "DEC";
                    else return null;

                } else {
                    return super.formatLabel(value, isValueX) + " km/h";
                }
            }
        });

        graph.removeAllSeries();
        graph.setTitle("Monthly averages of distances");
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(13);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(200);
        graph.addSeries(series);
    }

}
