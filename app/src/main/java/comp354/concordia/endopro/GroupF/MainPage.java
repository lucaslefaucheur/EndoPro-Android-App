package comp354.concordia.endopro.GroupF;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


import java.util.Collections;

import comp354.concordia.endopro.Common.User;
import comp354.concordia.endopro.Hong.StorageIntent;
import comp354.concordia.endopro.Lucas.Dashboard;
import comp354.concordia.endopro.R;
import comp354.concordia.endopro.Task3;

public class MainPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main_page);
    }

    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.task1:
                intent = new Intent(this, Dashboard.class);
                startActivity(intent);
                Collections.reverse(User.getInstance().getWorkouts());
                intent = new Intent(getApplicationContext(), StorageIntent.class);
                startService(intent);
                break;

            case R.id.task2:
                intent = new Intent(this, Task2.class);
                startActivity(intent);
                break;

            case R.id.task3:
                intent = new Intent(this, Task3.class);
                startActivity(intent);
                break;

            case R.id.logout:
                User.signOut();
                intent = new Intent(this,StorageIntent.class);
                startService(intent);
                finish();
                break;
        }

    }

}
