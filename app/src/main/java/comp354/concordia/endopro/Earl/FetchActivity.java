package comp354.concordia.endopro.Earl;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import java.net.InetAddress;

import comp354.concordia.endopro.Common.User;
import comp354.concordia.endopro.Lucas.Dashboard;
import comp354.concordia.endopro.R;

public class FetchActivity extends AppCompatActivity {
    public static TextView statusTV ;

    public static String stat = "";
    public static boolean connected = false;
    public static boolean checking = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch);

        //testing
      // User.setInstance("app_u","app_p","paynedick1@gmail.com","endo354mondo");
        User test_user = User.getInstance();
        statusTV =  findViewById(R.id.currentStatus);
        final CyclingFetcher test  = new CyclingFetcher();

        while (!connected)
        {
            if(!checking)
                checkConnection();
        }
        if(connected)
         test.generateWorkoutList();
        if(CyclingFetcher.done)
            startActivity(new Intent(FetchActivity.this, Dashboard.class));
        final Button button = findViewById(R.id.refresh);

        button.setOnClickListener(new View.OnClickListener() {
                                      public void onClick(View v) {
                                          // Code here executes on main thread after user presses button
                                          checkConnection();
                                          while(checking);
                                          if(connected)
                                              test.generateWorkoutList();

                                            if(CyclingFetcher.done)
                                                startActivity(new Intent(FetchActivity.this, Dashboard.class));

                                      }
                                  }

        );
    }
    private  void checkConnection(){
        statusTV.setText("Trying to connect...");
        AsyncTask at = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                try {
                    checking = true;
                    InetAddress ipad = InetAddress.getByName("google.com");
                    connected = !ipad.equals("");
                }
                catch (Exception e)
                {
                    connected = false;
                }
                checking = false;
                return null;
            }
        }.execute();

    }

}
