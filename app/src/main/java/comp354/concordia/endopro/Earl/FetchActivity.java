package comp354.concordia.endopro.Earl;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.UUID;

import comp354.concordia.endopro.Common.EndoProWorkout;
import comp354.concordia.endopro.Common.User;
import comp354.concordia.endopro.GroupF.MainPage;
import comp354.concordia.endopro.Hong.StorageIntent;
import comp354.concordia.endopro.Lucas.Dashboard;
import comp354.concordia.endopro.R;

public class FetchActivity extends AppCompatActivity {
    private static final String TAG="endopro_logi_data_fetcher";
    private static final String AuthPrefix = "https://api.mobile.endomondo.com/mobile/auth?";
    private static final String FetchPrefix = "https://api.mobile.endomondo.com/mobile/api/workouts?";
    private RequestQueue mQ;
    TextView errorTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch);
        Log.i(TAG, "onCreate: Transitioned to data_fetcher");

        errorTxt= findViewById(R.id.errorMessage_fetcher);

        mQ=Volley.newRequestQueue(this);
        getToken();

        Button skipBtn=findViewById(R.id.skip_fetcher);
        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toDashboard();
            }
        });
    }

    private void toDashboard(){
        Intent mainPage = new Intent(getApplicationContext(), MainPage.class);
        startActivity(mainPage);
        finish();
    }
    private void getToken(){
        String request_url =
            AuthPrefix
            +"deviceId="+UUID.randomUUID().toString()+"&"
            +"country=pl&"
            +"action=pair&"
            +"email="+User.getInstance().getEndo_username()+"&"
            +"password="+User.getInstance().getEndo_password();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, request_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG, "onResponse: "+response);
                        int index=response.indexOf("authToken");
                        if(index!=-1){
                            String token = response.substring(index+10,index+33);
                            Log.i(TAG, "onResponse: Token: "+token);
                            fetchFromEndo(token);
                        }else{
                            errorTxt.setText("Failed to sign in to Endomondo, please verify your credentials");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                errorTxt.setText("Failed to sign in to Endomondo, please verify your credentials");
            }
        });
        mQ.add(stringRequest);
    }

    private void fetchFromEndo(String token){
        String url = FetchPrefix
            +"authToken="+token+"&"
            +"fields=simple,basic&maxResults=100";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "onResponse: Success");
                        Log.i(TAG, "onResponse: "+response.toString());
                        try{
                            JSONArray workouts = response.getJSONArray("data");
                            storeWorkouts(workouts);
                            Log.i(TAG, "onResponse: Added workouts to user");
                            Intent save = new Intent(getApplicationContext(),StorageIntent.class);
                            startService(save);
                            toDashboard();
                        }catch(Exception e){
                            e.printStackTrace();
                            Log.e(TAG, "onResponse: ",e );
                            errorTxt.setText("Failed to obtain data from Endomondo");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                errorTxt.setText("Failed to obtain data from Endomondo");
            }
        });
        mQ.add(request);
    }

    private void storeWorkouts(JSONArray workouts) throws JSONException {
        User user=User.getInstance();
        user.clearWorkouts();
        for (int i = 0; i < workouts.length(); ++i) {
            JSONObject currentWorkout = workouts.getJSONObject(i);
            if (currentWorkout.getInt("sport") == 2) //2 stands for cycling sport
            {
                double avg = -1;
                double max = -1;
                double dist = -1;
                double dur = -1;
                String start = "";
                if (currentWorkout.has("speed_avg"))
                    avg = currentWorkout.getDouble("speed_avg");
                if (currentWorkout.has("speed_max"))
                    max = currentWorkout.getDouble("speed_max");
                if (currentWorkout.has("distance")) dist = currentWorkout.getDouble("distance");
                if (currentWorkout.has("duration")) dur = currentWorkout.getDouble("duration");
                if (currentWorkout.has("start_time"))
                    start = currentWorkout.getString("start_time");

                user.addWorkout(new EndoProWorkout(avg, max, dist, dur, start));
            }
        }
    }
}
