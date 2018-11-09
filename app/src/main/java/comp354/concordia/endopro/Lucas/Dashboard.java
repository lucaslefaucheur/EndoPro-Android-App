package comp354.concordia.endopro.Lucas;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.jjoe64.graphview.GraphView;

import comp354.concordia.endopro.Common.User;
import comp354.concordia.endopro.DanielT.Filtering;
import comp354.concordia.endopro.Earl.FetchActivity;
import comp354.concordia.endopro.Hong.Settings;
import comp354.concordia.endopro.Hong.StorageIntent;
import comp354.concordia.endopro.R;

public class Dashboard extends AppCompatActivity {
    private static final String TAG="endopro_logi_dashboard";
    EndoProGraph endoProGraph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Disable Screen Rotation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_dashboard);

        Log.i(TAG, "onCreate: Transitioned to Dashboard");

        Button buttonHistorySpeed = findViewById(R.id.buttonHistorySpeed);
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

        Button signout = findViewById(R.id.signout_dashboard);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User.signOut();
                Intent save = new Intent(getApplicationContext(),StorageIntent.class);
                startService(save);
                finish();
            }
        });

        Button settings = findViewById(R.id.settings_dashboard);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settings = new Intent(getApplicationContext(),Settings.class);
                startActivity(settings);
            }
        });

        final ImageButton filter = findViewById(R.id.filter_dashboard);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent filter = new Intent(getApplicationContext(),Filtering.class);
                startActivity(filter);
            }
        });

        ImageButton refresh = findViewById(R.id.refresh_dashboard);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fetch = new Intent(getApplicationContext(),FetchActivity.class);
                startActivity(fetch);
                finish();
            }
        });

        Log.i(TAG, "onCreate: Page setting up completed");
    }

    @Override
    protected void onResume() {
        super.onResume();
        User user = User.getInstance();
        endoProGraph = new EndoProGraph((GraphView) findViewById(R.id.graph), user);
    }
}
