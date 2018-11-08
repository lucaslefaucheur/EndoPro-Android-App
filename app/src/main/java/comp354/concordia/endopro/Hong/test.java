package comp354.concordia.endopro.Hong;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import comp354.concordia.endopro.Common.User;
import comp354.concordia.endopro.R;

public class test extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Button signout = findViewById(R.id.signout_test);

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User.signOut();
                Intent save = new Intent(getApplicationContext(),StorageIntent.class);
                startService(save);
                finish();
            }
        });
    }
}
