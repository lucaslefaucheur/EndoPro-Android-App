package comp354.concordia.endopro.GroupF;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


import comp354.concordia.endopro.R;

public class MainPage extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.task2:
                intent = new Intent(this, Task2.class);
                startActivity(intent);
                break;

            case R.id.logout:
//                // Clear login information
//                FirebaseAuth.getInstance().signOut();
//                SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
//                SharedPreferences.Editor Ed = sp.edit();
//                Ed.clear();
//                Ed.apply();
//
//                // Redirect to login page
//                intent = new Intent(this, Login.class);
//                startActivity(intent);
//                finish();
                break;
        }

    }

}
