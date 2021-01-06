package com.example.boxingreflextrainer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import androidx.preference.PreferenceManager;


// This class manage the timer functions
public class TimerHandler {
    long minutes, seconds;
    // Timer's remaining time
    long milsToFinish;
    // Total time got from settings file
    long totalTime;
    // Create timer object
    CountDownTimer clock;
    TimerCallbacks dataCallback;
    // Timer activty check
    boolean isActive = false;
    boolean isRestTime = false;
    boolean isPreparationTime = true;
    final int timeConversionValue = 60000;
    SharedPreferences sharedPreferences;
    Context context;
    int roundIterator = 0;
    int roundNumber = 0;

    // Constructor
    TimerHandler(long minutes, long seconds, Context context) {
        dataCallback = (TimerCallbacks) context;
        this.context = context;
        // Set total time
        totalTime = convertTime(minutes, seconds);
        milsToFinish = totalTime;
        // Set timer text
        updateTime(milsToFinish);
        // Create timer
        clock = createTimer();

    }


    // Update timer View
    public void updateTime(long time) {
        minutes = time / timeConversionValue;
        seconds = (time % timeConversionValue) / 1000;
    }


    // Set max time
    public void setTotalTime(long minutes, long seconds) {
        this.totalTime = convertTime(minutes, seconds);
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
                dataCallback.dataView(minutes, seconds, roundIterator, roundNumber);
            }

            // When timer reach to the end print on TextView
            public void onFinish() {

                if(roundIterator < roundNumber) {

                    if(!isRestTime && !isPreparationTime)
                        roundIterator++;

                    if(roundIterator > 0)
                        isRestTime = !isRestTime;
                    else {
                        isPreparationTime = !isPreparationTime;
                    }

                    if(roundIterator == roundNumber -1 && isRestTime) {
                        isRestTime = false;
                        isActive = false;
                        isPreparationTime = true;
                        getPreferences();
                        stopTimer();
                    } else {
                        getPreferences();
                        isActive = false;
                        startTimer();
                    }

                } else {
                    isRestTime = false;
                    isActive = false;
                    isPreparationTime = true;
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
        dataCallback.dataView(minutes, seconds, roundIterator, roundNumber);

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
        dataCallback.dataView(minutes, seconds, roundIterator, roundNumber);
    }

}
