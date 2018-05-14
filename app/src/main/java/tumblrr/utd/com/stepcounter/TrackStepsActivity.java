package tumblrr.utd.com.stepcounter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

/**
 * Activity: TrackStepsActivity
 *
 * This displays graphical and list view of your steps taken trip in past
 * No data is lost unless user Opts to clear the data
 * Created by Abhi on 4/6/2018.
 */

public class TrackStepsActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<ListviewContent> listviewContents;
    private static ListviewAdap adapter;

    //Reading/Writing the steps related history on to/from a local storage file
    public String path  = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Step Counter";
    File myDirs = new File(path);
    File file =new File(path+"/stepCountHistory.txt");

    String[] lineDetail = new String[4];
    ArrayList<String> lines = new ArrayList<String>(); //Array list to store each line from the file
    ArrayList<Date> dates = new ArrayList<Date>();

    //Author: Abhilash Gudasi, Paras Bansal
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.track_steps);
        GraphView graph = (GraphView) findViewById(R.id.graph);
        GridLabelRenderer glr = graph.getGridLabelRenderer();
        glr.setPadding(60);
        graph.getViewport().setScrollable(true);
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "Cp1252"), 100); //Open the file to read
            String line;

            LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>();
            while ((line = br.readLine()) != null) { //Read each Line from the file
                lineDetail = line.split("\t"); //Split the line by tab and store in a string array
                lines.add(lineDetail[0] + "\t" + lineDetail[1] + "\t" + lineDetail[2] + "\t" + lineDetail[3]);
                //String interm = lineDetail[0].split(" ")[0];
                String interm1 = lineDetail[0];
                dates.add(new Date(interm1));
            }

            Iterator it1 = lines.iterator();
            Iterator it2 = dates.iterator();
            DataPoint[] dp = new DataPoint[25];
            while (it1.hasNext() && it2.hasNext()) {
                Date a = (Date)it2.next();
                Integer b = Integer.parseInt(it1.next().toString().split("\t")[1]);
                series.appendData(//new DataPoint(4, 6)
                        new DataPoint(a, b),true,100 //new DataPoint(new Date(2018, 04, 16), 40),
                         );
            }
            //series.appendData(new DataPoint(new Date(2018,04,16),20),true,100);
            graph.addSeries(series);

            // set date label formatter
            glr.setLabelFormatter(new DateAsXAxisLabelFormatter(graph.getContext()));
            glr.setNumHorizontalLabels(3);

            graph.getViewport().setXAxisBoundsManual(true);

            graph.getGridLabelRenderer().setHumanRounding(false);


            br.close();//Close the Buffer reader
        } catch (Exception e) {
            e.printStackTrace();
        }

        listView=(ListView)findViewById(R.id.list);

        listviewContents = new ArrayList<>();
        Iterator iter = lines.iterator();
        int i =0;

        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        while(iter.hasNext()){
            String str = iter.next().toString();
            double distance = Double.parseDouble(str.split("\t")[2]);
            listviewContents.add(new ListviewContent(str.split("\t")[0], str.split("\t")[1]+" steps", decimalFormat.format(distance).toString()+" feets",str.split("\t")[3],str.split("\t")[3]+ " mins"));
        }

        adapter= new ListviewAdap(listviewContents,getApplicationContext());
        listView.setAdapter(adapter);
    }
}
