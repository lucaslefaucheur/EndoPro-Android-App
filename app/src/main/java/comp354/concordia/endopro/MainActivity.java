package comp354.concordia.endopro;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

import comp354.concordia.endopro.Common.User;
import comp354.concordia.endopro.Earl.FetchActivity;
import comp354.concordia.endopro.Hong.App_Data;
import comp354.concordia.endopro.Hong.AuthenticationException;
import comp354.concordia.endopro.Hong.SignUp;
import comp354.concordia.endopro.Hong.StorageIntent;
import comp354.concordia.endopro.Hong.test;

public class MainActivity extends AppCompatActivity {

    private static final String TAG="endopro_logi_main";
    private EditText userNameTxt;
    private EditText passwordTxt;
    private CheckBox autoAuth;
    private TextView errorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Disable Screen Rotation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_main);

        FileInputStream file=null;
        TransitionToDashboard();
        try {
            file = openFileInput("endoData.txt");
            ObjectInputStream object = new ObjectInputStream(file);

            App_Data data =(App_Data)object.readObject();

            User.setApp_data(data);

            file.close();
            object.close();
            Log.i(TAG, "handleActionRead: File found");
        } catch (Exception e) {
            Log.i(TAG, "handleActionRead: "+e.getMessage());
            Log.i(TAG, "handleActionRead: File not found!");
            User.setApp_data(new App_Data());
        }

        Intent save = new Intent(getApplicationContext(),StorageIntent.class);
        startService(save);

        Log.i(TAG, "onCreate: Got here");

        //Get all UI elements
        errorText = findViewById(R.id.errorText_main);
        Button signinBtn = findViewById(R.id.signinBtn_main);
        Button signupBtn = findViewById(R.id.signupBtn_main);
        userNameTxt = findViewById(R.id.usernameText_main);
        passwordTxt = findViewById(R.id.passwordText_main);
        autoAuth = findViewById(R.id.autoAuth_main);
        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                handleSignin(autoAuth.isChecked());
            }
        });
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSignup();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        userNameTxt.getText().clear();
        passwordTxt.getText().clear();
        if(autoAuth.isChecked()) autoAuth.toggle();
        passwordTxt.clearFocus();
        clearError();
        checkSignedUser();
    }

    public void TransitionToDashboard(){
        Intent dashboard = new Intent(getApplicationContext(),FetchActivity.class);
        startActivity(dashboard);
    }

    private void checkSignedUser(){
        /*
            If no user is signed in or the previous user chose not to auto sign in,
            this step will be skipped.
         */

        if(User.getInstance()==null){
            if(User.getApp_data().getCached()==null)
                return;
            User.setInstance(User.getApp_data().getCached());
            Log.i(TAG, "checkSignedUser: Auto Signing in user");
        }
        TransitionToDashboard();
    }

    private void handleSignin(boolean autoAuth){
        /*
            Validate Credentials
            Cases:
                1) User Exists
                    1.a) Validation successful
                    1.b) Validation failed
                2) User Doesn't exist
        */
        try{
            String username=userNameTxt.getText().toString();
            String password=passwordTxt.getText().toString();

            if(verifyCredential(username,password)){
                /*
                    Transition to data fetcher activity that will handle collecting
                    data from Endomondo
                */
                if(autoAuth && User.getApp_data().getCached()==null){
                    User.rememberUser();
                    Intent save = new Intent(getApplicationContext(),StorageIntent.class);
                    startService(save);
                }
                TransitionToDashboard();
            }else{
                throw new AuthenticationException("Invalid Credentials");
            }
        }catch(AuthenticationException e){
            setError(e.getMessage());
        }catch (Exception general){
            setError(general.getMessage());
        }
    }

    private void handleSignup(){
        EditText userNameTxt = findViewById(R.id.usernameText_main);
        String userName=userNameTxt.getText().toString();

        /*
            User Creation Phase
            If user already entered some info before clicking "Sign Up", that info
            will be saved and passed on to the next phase.
        */

        Intent signup = new Intent(getApplicationContext(),SignUp.class);
        signup.putExtra("username",userName);
        startActivity(signup);
    }

    private boolean verifyCredential(String username, String password)
            throws AuthenticationException {

        User user = User.getApp_data().getUser(username);

        if(user==null) throw new AuthenticationException("User not found");

        User.setInstance(user);
        return user.Authenticate(username,password);
    }

    private void clearError(){
        errorText.setVisibility(View.INVISIBLE);
    }
    private void setError(String errText){
        errorText.setText(errText);
        errorText.setVisibility(View.VISIBLE);
    }
}
