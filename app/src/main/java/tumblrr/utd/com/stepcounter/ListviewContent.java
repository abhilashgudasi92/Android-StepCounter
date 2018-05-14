package tumblrr.utd.com.stepcounter;

/**
 * Created by Abhi on 4/7/2018.
 * This implements single element in the list view
 */

//Author: Paras Bansal
public class ListviewContent {

    String recordedTime;
    String stepsTaken;
    String distance;
    String timeTaken;

    public ListviewContent(String recordedTime, String stepsTaken, String distance, String feature, String timeTaken) {
        this.recordedTime = recordedTime;
        this.stepsTaken = stepsTaken;
        this.distance = distance;
        this.timeTaken = timeTaken;
    }

    public String getRecordedTime() {
        return recordedTime;
    }

    public String getStepsTaken() {
        return stepsTaken;
    }

    public String getDistance() {
        return distance;
    }

    public String getTimeTaken(){
        return timeTaken;
    }

}
