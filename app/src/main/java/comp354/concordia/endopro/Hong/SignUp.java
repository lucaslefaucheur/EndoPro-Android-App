package comp354.concordia.endopro.Hong;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import comp354.concordia.endopro.Common.User;
import comp354.concordia.endopro.MainActivity;
import comp354.concordia.endopro.R;

public class SignUp extends AppCompatActivity {
    private static final String TAG="endopro.logi_signup";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Disable Screen Rotation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_sign_up);

        Button signUpbtn = findViewById(R.id.signup_signup);
        signUpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        EditText app_usernameTxt = findViewById(R.id.username_app_signup);
        /*
            If these fields were already provided in the first window, use
            them to fill out the form.
         */
        if(getIntent().hasExtra("username")){
            String username;
            username=getIntent().getExtras().get("username").toString();
            app_usernameTxt.setText(username);
        }
    }

    public void createAccount(){
        EditText app_usernameTxt = findViewById(R.id.username_app_signup);
        EditText app_passwordTxt = findViewById(R.id.password_app_signup);
        EditText endo_usernameTxt = findViewById(R.id.username_endo_signup);
        EditText endo_passwordTxt = findViewById(R.id.password_endo_signup);

        final TextView errorText = findViewById(R.id.error_signup);

        String app_username=app_usernameTxt.getText().toString();
        String app_password=app_passwordTxt.getText().toString();
        String endo_username = endo_usernameTxt.getText().toString();
        String endo_password = endo_passwordTxt.getText().toString();

        try{
            validateInput(app_username,app_password);
            //Input is valid, proceed to account creation

            if(User.getApp_data().getUser(app_username)==null){
                //User doesn't exist, proceed to create

                User.setInstance(app_username,app_password,endo_username,endo_password);

                Intent DB = new Intent(getApplicationContext(),StorageIntent.class);
                startService(DB);
                finish();
            }else{
                throw new ValidationException("Account already created");
            }
        }catch (ValidationException e){
            errorText.setText(e.getMessage());
        }
    }

    private void validateInput(String username, String password) throws ValidationException{
        if(username.length()<4)
            throw new ValidationException("Username must be at least 4 characters");
        if(password.length()<6)
            throw new ValidationException("Password must be at least 6 characters");
    }
}
