package com.example.boxingreflextrainer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import androidx.preference.PreferenceManager;


// This class manage the timer functions
public class TimerHandler {
    // Create timer object
    CountDownTimer clock;
    // TimerCallback object
    TimerCallbacks dataCallback;
    // Shared preferences object
    SharedPreferences sharedPreferences;
    // MainActivity context
    Context context;

    // Time variables
    long minutes, seconds;
    // Remaining time
    long milsToFinish;
    // Total time
    long totalTime;
    // Convert timer to or from milliseconds
    final int timeConversionValue = 60000;
    // Rounds iterator
    int roundIterator = 0;
    // Total rounds number
    int roundNumber = 0;
    // Timer is running
    boolean isActive = false;
    // Is rest time: bewteen two rounds
    boolean isRestTime = false;
    // Is preparation time: before start
    boolean isPreparationTime = true;

    // Constructor
    TimerHandler(long minutes, long seconds, Context context) {
        // Initialize Instance object
        dataCallback = (TimerCallbacks) context;
        // Initialize context
        this.context = context;
        // Set total time
        totalTime = convertTime(minutes, seconds);
        // Set milliseconds to finish
        milsToFinish = totalTime;
        // Set timer text
        updateTime(milsToFinish);
        // Create timer
        clock = createTimer();

    }


    // Update timer View
    public void updateTime(long milliseconds) {
        // Calculate minutes and seconds from milliseconds
        minutes = milliseconds / timeConversionValue;
        seconds = (milliseconds % timeConversionValue) / 1000;
        // Update view
        dataCallback.dataView(minutes, seconds, roundIterator, roundNumber);
    }


    // Set max time
    public void setTotalTime(long minutes, long seconds) {
        this.totalTime = convertTime(minutes, seconds);
        // Refresh the timer
        stopTimer();
    }


    // Convert time into milliseconds
    public long convertTime(long minutes, long seconds) {
        long time = minutes * 60000;
        time += seconds * 1000;
        return time;
    }


    // Function that create the timer
    protected CountDownTimer createTimer() {
        return new CountDownTimer(milsToFinish, 1000) {

            @Override
            // Every tick update milliseconds remaining and TextView's text
            public void onTick(long millisUntilFinished) {
                milsToFinish = millisUntilFinished;
                updateTime(milsToFinish);
            }

            // When timer reach to the end print on TextView
            public void onFinish() {
                // Ceck if iterator is in round's range
                if(roundIterator < roundNumber) {

                    // If is workout time increase the round iterator
                    if(!isRestTime && !isPreparationTime)
                        roundIterator++;

                    // If the roundIterator is higher than 0 update rest time
                    if(roundIterator > 0)
                        isRestTime = !isRestTime;
                    // If is the first round update preparation time
                    else {
                        isPreparationTime = !isPreparationTime;
                    }

                    // If it's the last round skip the rest time and stop the timer
                    if(roundIterator == roundNumber && isRestTime) {
                        // Reset variables
                        isRestTime = false;
                        isActive = false;
                        isPreparationTime = true;
                        roundIterator = 0;
                        getPreferences();
                        stopTimer();

                    // Start the timer with the new data
                    } else {
                        getPreferences();
                        isActive = false;
                        startTimer();
                    }

                // Stop the timer because it reached the end
                } else {
                    // Reset variables
                    isRestTime = false;
                    isActive = false;
                    isPreparationTime = true;
                    roundIterator = 0;
                    getPreferences();
                    stopTimer();
                }
            }
        };

    }


    // Get preferences form xml file
    public void getPreferences() {
        String temp;
        // Get shared preferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        if(!isRestTime && !isPreparationTime) {
            // Get timerTimer key's value from preferences
            temp = sharedPreferences.getString("trainingTime", "0:0");
            // Split minutes and seconds
            String[] splitString = temp.split(":");
            // Convert minutes and seconds into long and set the timer
            setTotalTime(Long.parseLong(splitString[0]), Long.parseLong(splitString[1]));
        }
        else if(isRestTime && !isPreparationTime) {
            // Get timerTimer key's value from preferences
            temp = sharedPreferences.getString("restTime", "0:0");
            // Split minutes and seconds
            String[] splitString = temp.split(":");
            // Convert minutes and seconds into long and set the timer
            setTotalTime(Long.parseLong(splitString[0]), Long.parseLong(splitString[1]));
        } else {
            // Get timerTimer key's value from preferences
            temp = sharedPreferences.getString("preparationTime", "0:0");
            // Split minutes and seconds
            String[] splitString = temp.split(":");
            // Convert minutes and seconds into long and set the timer
            setTotalTime(Long.parseLong(splitString[0]), Long.parseLong(splitString[1]));
        }

        roundNumber = Integer.parseInt(sharedPreferences.getString("roundsNumber", "-1"));

        // Update timer View
        updateTime(milsToFinish);

    }


    // Function that start the timer when it's not active
    protected void startTimer() {
        if(!isActive) {
            // Start the timer
            clock.start();
            // Change timer status
            isActive = true;
        }
    }


    // Function that pause the timer
    protected void pauseTimer() {
        if(isActive) {
            // Delete the actual timer
            clock.cancel();
            // Crete a new timer with remaining milliseconds
            clock = createTimer();
            // Change timer status
            isActive = false;
        }
    }


    // Function that stop the timer
    protected void stopTimer() {
        // Delete the actual timer
        clock.cancel();
        if(!isActive)
            dataCallback.resetButtons();
        // Reset the milliseconds remaining
        milsToFinish = totalTime;
        // Create a new timer with milliseconds remaining
        clock = createTimer();
        // Reset TextView's text
        updateTime(milsToFinish);
    }

}
