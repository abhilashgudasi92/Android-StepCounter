package tumblrr.utd.com.stepcounter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Activity: StepDetectorActivity
 *
 * This handles the options of taking input for inch/step from user[default set to 10]
 * Start button to start the step detector service
 * Stop button to stop the step detector service
 * Displays number of steps taken ater starting the step detector service
 *
 * Created by Abhi on 4/8/2018.
 */

public class StepDetectorActivity extends AppCompatActivity {

    EditText inchStep;
    TextView counter;

    Button start;
    Button stop;
    Button back;

    long startTime;
    long stopTime;
    String countedStep;
    String DetectedStep;
    static final String State_Count = "Counter";
    static final String State_Detect = "Detector";

    boolean isServiceStopped;

    //Reading/Writing the steps related history on to a local storage file
    public String path  = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Step Counter";
    File myDirs = new File(path);
    File file =new File(path+"/stepCountHistory.txt");

    private Intent intent,intentMainactivity;
    private static final String TAG = "SensorEvent";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step_detector);
        intent = new Intent(this, StepCountingService.class);

        viewInit(); // Call view initialisation method.
    }

    /*string array to capture
    * a. start date/time,
    * b. step count
    * c. Distance walked
    * d. Total time taken*/
    String [] saveText = new String[4];

    // Initialise step_detector layout view
    //Author: Abhilash Gudasi
    public void viewInit() {

        isServiceStopped = true; //Current Service state

        start = (Button)findViewById(R.id.button_start);
        inchStep = (EditText) findViewById(R.id.edittext_inchstep);
        counter = (TextView) findViewById(R.id.counter);
        inchStep.setText("10");
        counter.setText("0");
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String msg = "Step Counter Started";
                Toast toast = Toast.makeText(StepDetectorActivity.this,msg,Toast.LENGTH_SHORT);
                toast.show();

                //Recording date and time
                saveText[0] =  DateFormat.getDateTimeInstance().format(new Date());

                startTime = Calendar.getInstance().getTimeInMillis();

                // start Service.
                startService(new Intent(getBaseContext(), StepCountingService.class));

                // register BroadcastReceiver
                registerReceiver(broadcastReceiver, new IntentFilter(StepCountingService.BROADCAST_ACTION));
                isServiceStopped = false;
            }
        });

        //To stop step detector service
        stop = (Button)findViewById(R.id.button_stop);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isServiceStopped) {
                    String msg = "Step counter stopped";
                    Toast toast = Toast.makeText(StepDetectorActivity.this,msg,Toast.LENGTH_SHORT);
                    toast.show();
                    //unregisterReceiver
                    unregisterReceiver(broadcastReceiver);
                    isServiceStopped = true;

                    stopTime = Calendar.getInstance().getTimeInMillis();
                    String abc = String.valueOf(stopTime);
                    //Steps walked
                    saveText[1] = counter.getText().toString();
                    //Distance Walked in feets
                    saveText[2] = String.valueOf(0.0833 * Double.parseDouble(counter.getText().toString()) * Double.parseDouble(inchStep.getText().toString()));
                    //Total time taken in min
                    saveText[3] = String.valueOf(((stopTime - startTime)/60000)+"."+((stopTime - startTime)%60000));
                    // stop Service.
                    stopService(new Intent(getBaseContext(), StepCountingService.class));

                    //To save the current step count results on to local file
                    try {
                        Save (file,saveText);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Intent intentReportactivity = new Intent(StepDetectorActivity.this, ReportActivity.class);
                    intentReportactivity.putExtra("report",saveText);
                    intentReportactivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivityForResult(intentReportactivity,1);
                }
            }
        });

        //Handling back button click
        //Going back from current StepDetector Activity to Main Activity
        back = (Button)findViewById(R.id.button_back);
        back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intentMainactivity = new Intent(StepDetectorActivity.this, MainActivity.class);
                    intentMainactivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intentMainactivity);
                }
            });

        counter = (TextView)findViewById(R.id.counter);
    }

    /*
    *Method to save the contact in to the local database(text file)
    *Author: Abhilash Gudasi
     */
    private void Save(File myfile,String[] data) throws IOException {
        if(!myDirs.exists()){
            myDirs.mkdirs();
        }
        if(!myfile.exists()){
            myfile.createNewFile();
        }
        FileOutputStream fos = null;
        OutputStreamWriter myOutWriter = null;
        try
        {
            fos = new FileOutputStream(file,true);
            myOutWriter = new OutputStreamWriter(fos);
        }
        catch (FileNotFoundException e)
        {e.printStackTrace();}
        try
        {
            for(int i=0;i<4;i++) {
                myOutWriter.append(data[i]);
                myOutWriter.append("\t");
            }
            myOutWriter.append("\n");

        }
        finally
        {
            myOutWriter.close();
            fos.close();
        }
    }

    protected void onPause() {
        super.onPause();
    }

    // OnResume resetting to previously left state of step_detector layout
    // Author: Paras Bansal
    protected void onResume() {
        super.onResume();
        intent = new Intent(this, StepCountingService.class);
        viewInit();
    }


    // BroadcastReceiver to receive the message from the Step Detector Service
    // Author: Paras Bansal
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateViews(intent);
        }
    };

    // Retrieve and set data of counter
    // Author: Paras Bansal
    private void updateViews(Intent intent) {
        countedStep = intent.getStringExtra("Counted_Step");
        DetectedStep = intent.getStringExtra("Detected_Step");
        Log.d(TAG, String.valueOf(countedStep));
        Log.d(TAG, String.valueOf(DetectedStep));

        counter.setText(String.valueOf(DetectedStep));
    }
}