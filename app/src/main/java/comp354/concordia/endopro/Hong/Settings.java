package comp354.concordia.endopro.Hong;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import comp354.concordia.endopro.Common.User;
import comp354.concordia.endopro.R;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Button save = findViewById(R.id.saveBtn_settings);
        ImageButton close = findViewById(R.id.closeBtn_settings);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText oldPassTxt = findViewById(R.id.password_old_settings);
                EditText newPassTxt = findViewById(R.id.password_new_settings);

                EditText usernameEndo = findViewById(R.id.username_endo_settings);
                EditText passwordEndo = findViewById(R.id.password_endo_settings);

                final TextView errorText = findViewById(R.id.errorMessage_settings);

                if(newPassTxt.getText().length()<6){
                    errorText.setText("Password must be at least 6 characters");
                    return;
                }
                User current = User.getInstance();

                current.updateEndoInfo(usernameEndo.toString(),passwordEndo.toString());
                if(current.updatePassword(oldPassTxt.toString(),newPassTxt.toString())){
                    Intent saveIntent = new Intent(getApplicationContext(),StorageIntent.class);
                    startService(saveIntent);
                    finish();
                }else{
                    errorText.setText("Current password is incorrect");
                }
            }
        });
    }
}
