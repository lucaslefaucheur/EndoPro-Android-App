package comp354.concordia.endopro.Lucas;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.jjoe64.graphview.GraphView;

import comp354.concordia.endopro.Common.User;
import comp354.concordia.endopro.R;

public class Dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        User user = User.getInstance();
        final EndoProGraph endoProGraph = new EndoProGraph((GraphView) findViewById(R.id.graph), user);

        Button buttonHistorySpeed = (Button) findViewById(R.id.buttonHistorySpeed);
        buttonHistorySpeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endoProGraph.createHistorySpeedGraph();
            }
        });
        Button buttonYearlySpeed = (Button) findViewById(R.id.buttonYearlySpeed);
        buttonYearlySpeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endoProGraph.createYearlySpeedGraph();
            }
        });
        Button buttonMonthlySpeed = (Button) findViewById(R.id.buttonMonthlySpeed);
        buttonMonthlySpeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endoProGraph.createMonthlySpeedGraph();
            }
        });

        Button buttonHistoryDistance = (Button) findViewById(R.id.buttonHistoryDistance);
        buttonHistoryDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endoProGraph.createHistoryDistanceGraph();
            }
        });
        Button buttonYearlyDistance = (Button) findViewById(R.id.buttonYearlyDistance);
        buttonYearlyDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endoProGraph.createYearlyDistanceGraph();
            }
        });
        Button buttonMonthlyDistance = (Button) findViewById(R.id.buttonMonthlyDistance);
        buttonMonthlyDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endoProGraph.createMonthlyDistanceGraph();
            }
        });


    }
}
