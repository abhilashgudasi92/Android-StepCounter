# StepCounter
**1. Main Menu:**

This will be the Main activity which user will see after launching the application.

- It has 5 components:

    - Application name/Image: This is a static either App name/ Image

    - Start Step Count: This button will call Step Detector Activity where user can start step detection service and see the step count. 

    - Track Steps: This Button will call Track Steps Activity which contains graphical view and list view of all past step counts taken    till date.

    - Help: This button will call Help Activity which will give general description of the Application.

    - Exit: To exit the application.

**2. Step Detector Activity:**

When user clicks Start Step Count button in Main Activity, the Step Detector Activity shows up.

This Activity contains:
  - Inch/Step: To right of this text view display is a blank edit view where user needs to enter how much inch does a step counts as. Ex: 1 step = 10 inches. 
  
  - Step Count: To the right of this text view is another text view which displays the count of steps taken after starting the step count service.
  
  - Start: This button will start the Step count service in background which reports (Broadcast Receiver) and updates the step count present in this activity.
  
  - Stop: This button will stop the Step count service running in background.
   
  - Back: Back button to go to previous Activity I.e. Main Activity.
  
**3. Track Steps Activity:**

When user clicks Track Steps button in Main Activity, the Track Steps Activity shows up.

This Activity keeps track all the previous step counts taken which are stored and updated locally in a text file.

This Activity contains:

  - Graphical View: Graphical representation of all the previous step counts because pictorial representation is easier to analyses than text.
  
  - List view of all the previous step counts taken.
  
  - Back: Back button to go to previous Activity I.e. Main Activity.
  
**4. Report Activity:**

This screen will be called from “Stop” button on Step Detector page.

This contains some useful results that user would like to know based on the number of steps they have taken like Calories burnt, Avg Speed while taking steps and time taken between the step counts.

Back button to go to previous Activity I.e. Step Detector Activity.

**5. Help Activity:**

This screen will be called from “Help” button on Main Activity page.

This page contains the general guild lines about the application and how to use it.

