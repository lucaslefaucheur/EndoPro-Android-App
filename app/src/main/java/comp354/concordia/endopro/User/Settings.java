package comp354.concordia.endopro.User;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import comp354.concordia.endopro.Intents.StorageIntent;
import comp354.concordia.endopro.R;

public class Settings extends AppCompatActivity {
    private static final String TAG="endopro_logi_settings";
    private EditText usernameEndo;
    private EditText passwordEndo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        usernameEndo = findViewById(R.id.username_endo_settings);
        passwordEndo = findViewById(R.id.password_endo_settings);

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

                final TextView errorText = findViewById(R.id.errorMessage_settings);
                String oldPass = oldPassTxt.getText().toString();
                String newPass = newPassTxt.getText().toString();
                String endoUsername = usernameEndo.getText().toString();
                String endoPassword = passwordEndo.getText().toString();

                if(!oldPass.equals("") || !newPass.equals("")){
                    if(newPass.length()<6){
                        errorText.setText("Password must be at least 6 characters");
                        return;
                    }
                    if(UserController.getInstance().updateAppPassword(oldPass,newPass)){
                    Intent saveIntent = new Intent(getApplicationContext(),StorageIntent.class);
                    startService(saveIntent);
                    finish();
                }else{
                    errorText.setText("Current password is incorrect");
                    return;
                    }
                }

                if(!endoUsername.equals(UserController.getInstance().getCurrentUser().getApp_username()) ||
                        !endoPassword.equals("")){
                    UserController.getInstance().updateEndoInfo(endoUsername,endoPassword);
                    Intent save = new Intent(getApplicationContext(),StorageIntent.class);
                    startService(save);
                    finish();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        User current = UserController.getInstance().getCurrentUser();
        if(current!=null)
            usernameEndo.setText(current.getEndo_username());
    }
}
