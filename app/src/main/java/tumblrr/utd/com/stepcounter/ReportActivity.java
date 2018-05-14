package tumblrr.utd.com.stepcounter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;

/**
 * Activity: ReportActivity
 * This reports the results of current walk trip when step detector service is stopped
 * Displays:
 *          a. Calories burnt(in Cal)
 *          b. Avg speed(feets/min)
 *          c. Steps walked
 *          d. Time taken(hours:min:sec accordingly
 * Created by Abhi on 4/10/2018.
 */

public class ReportActivity extends AppCompatActivity {

    TextView time,avgSpeed,caloriesBurnt,stepWalked;
    Button back;
    //Author: Abhilash Gudasi
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report);

        time = (TextView) findViewById(R.id.time);
        caloriesBurnt = (TextView) findViewById(R.id.calories);
        avgSpeed = (TextView) findViewById(R.id.avgspeed);
        stepWalked = (TextView) findViewById(R.id.stepCount);

        Bundle extras = getIntent().getExtras();
        String[] value = extras.getStringArray("report");

        DecimalFormat decim = new DecimalFormat("0.00");

        stepWalked.setText(value[1]+"steps");
        time.setText(value[3]+"min");
        avgSpeed.setText(String.format("%.2f",(Double.parseDouble(value[2])/Double.parseDouble(value[3]))* 0.00508 )+"m/sec");

        //Calculating calories burnt
        double conversationFactor = Double.parseDouble(value[2])/63;           //(5280/10) ==> (1 mile in feet / 0.8333 feet one step
        double CaloriesBurned = Double.parseDouble(value[1]) * conversationFactor;
        caloriesBurnt.setText(String.format("%.2f",CaloriesBurned)+"cal");

        back = (Button)findViewById(R.id.button_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentStepDetectoractivity = new Intent(ReportActivity.this, StepDetectorActivity.class);
                intentStepDetectoractivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentStepDetectoractivity);
            }
        });
    }

    // OnResume resetting to previously left state of report layout
    // Author: Abhilash Gudasi
    @Override
    protected void onResume() {
        super.onResume();
        time = (TextView) findViewById(R.id.time);
        caloriesBurnt = (TextView) findViewById(R.id.calories);
        avgSpeed = (TextView) findViewById(R.id.avgspeed);
        stepWalked = (TextView) findViewById(R.id.stepCount);

        Bundle extras = getIntent().getExtras();
        String[] value = extras.getStringArray("report");

        stepWalked.setText(value[1]+"steps");
        time.setText(value[3]+"min");
        avgSpeed.setText(String.format("%.2f",(Double.parseDouble(value[2])/Double.parseDouble(value[3]))* 0.00508 )+"m/sec");

        //Calculating calories burnt
        double conversationFactor = Double.parseDouble(value[2])/63;           //(5280/10) ==> (1 mile in feet / 0.8333 feet one step
        double CaloriesBurned = Double.parseDouble(value[1]) * conversationFactor;
        caloriesBurnt.setText(String.format("%.2f",CaloriesBurned)+"cal");

        back = (Button)findViewById(R.id.button_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentStepDetectoractivity = new Intent(ReportActivity.this, StepDetectorActivity.class);
                intentStepDetectoractivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentStepDetectoractivity);
            }
        });
    }
}
