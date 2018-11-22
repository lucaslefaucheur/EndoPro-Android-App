package comp354.concordia.endopro.Graph;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

import comp354.concordia.endopro.User.EndoProWorkout;
import comp354.concordia.endopro.User.User;
import comp354.concordia.endopro.R;
import comp354.concordia.endopro.User.UserController;

public class Filtering extends AppCompatActivity {
    Button btn;
    ImageButton closeBtn;

    TextView textView1;
    SeekBar seekbar1;

    TextView textView2;
    SeekBar seekbar2;

    int max = 300;
    int max2 = 500;

    User user = UserController.getInstance().getCurrentUser();

    TextView starttime;
    TextView distance;
    TextView speed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Disable Screen Rotation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_filter);

        /*
        EndoProWorkout [] arr = new EndoProWorkout[50];
        for(int i = 0; i < 50; i ++){
            arr[i] = new EndoProWorkout(rand.nextInt(100),rand.nextInt(100), rand.nextInt(100),rand.nextInt(100), "11012018");
            user.addWorkout(arr[i]);
        }
        */

        btn = (Button)findViewById(R.id.button);
        closeBtn = findViewById(R.id.close_filter);
        textView1 = (TextView)findViewById(R.id.textView2);
        seekbar1 = (SeekBar)findViewById(R.id.seekBar);

        seekbar1.setMax(max);
        seekbar1.setProgress(max);

        textView1.setText(max + " km");

        textView2 = (TextView)findViewById(R.id.textView4);
        seekbar2 = (SeekBar)findViewById(R.id.seekBar2);

        seekbar2.setMax(max2);
        textView2.setText(max2 + " km/h");
        seekbar2.setProgress(max2);

        starttime = (TextView)findViewById(R.id.textView14);
        distance = (TextView)findViewById(R.id.textView13);
        speed = (TextView)findViewById(R.id.textView12);


        final ArrayList <EndoProWorkout> myworkouts = user.getWorkouts();
        for(int i = 0; i < myworkouts.size(); i++) {

            starttime.append(myworkouts.get(i).getStartTime() + "\n");
            distance.append(String.format("%.2f", myworkouts.get(i).getDistance()) + "\n\n");
            speed.append(String.format("%.2f", myworkouts.get(i).getSpeedAverage()) + "\n\n");
        }

        seekbar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, final int progress, boolean fromUser) {
                textView1.setText(progress + " km");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(final SeekBar seekBar) {
            }

        });
        seekbar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                textView2.setText(progress + " km/h");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        Button filterBtn = findViewById(R.id.clear_filter);
        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserController.getInstance().setFilteredWorkout(null);
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                try {
                    starttime.setText("");
                    speed.setText("");
                    distance.setText("");
                    ArrayList<EndoProWorkout> filter = new ArrayList<EndoProWorkout>();
                    UserController.getInstance().setFilteredWorkout(myworkouts);
                    for(int i = 0;i < myworkouts.size();i++){
                        if(seekbar2.getProgress() >= myworkouts.get(i).getSpeedAverage() && seekbar1.getProgress() >= myworkouts.get(i).getDistance() ) {
                            starttime.append(myworkouts.get(i).getStartTime() + "\n");
                            speed.append(String.format("%.2f", myworkouts.get(i).getSpeedAverage()) + "\n\n");
                            distance.append(String.format("%.2f", myworkouts.get(i).getDistance()) + "\n\n");
                            filter.add(myworkouts.get(i));
                        }
                    }
                    UserController.getInstance().setFilteredWorkout(filter);
                    finish();

                } catch (Exception e) {starttime.setText("Error"); }
                }
            }).run();
            }
        });
    }


}