package com.example.boxingreflextrainer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.preference.PreferenceManager;


// This class manage the timer functions
public class TimerHandler {
    // Create TextView object
    public TextView timer, roundView;
    public Button startButton, pauseButton, stopButton;
    // Timer's remaining time
    long milsToFinish;
    // Total time got from settings file
    long totalTime;
    // Create timer object
    CountDownTimer clock;
    // Timer activty check
    boolean isActive = false;
    boolean isRestTimer = false;
    final int timeConversionValue = 60000;
    SharedPreferences sharedPreferences;
    Context context;
    int roundIterator = 0;
    int roundNumber = 0;


    // Constructor
    TimerHandler(TextView template, TextView round, Button start, Button pause, Button stop, long minutes, long seconds, Context context) {
        this.context = context;
        startButton = start;
        pauseButton = pause;
        stopButton = stop;
        roundView = round;
        // Set total time
        totalTime = convertTime(minutes, seconds);
        milsToFinish = totalTime;
        // Set timer TextView
        timer = template;
        // Set timer text
        updateView(milsToFinish);
        // Create timer
        clock = createTimer();

    }


    // Update timer View
    public void updateView(long time) {
        long minutes = time / timeConversionValue;
        long seconds = (time % timeConversionValue) / 1000;

        roundView.setText(String.format("Round: %d/%d", roundIterator + 1, roundNumber));
        timer.setText(String.format("%d:%d", minutes, seconds));
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
                updateView(milsToFinish);
            }

            // When timer reach to the end print on TextView
            public void onFinish() {
                if(!isRestTimer)
                    roundIterator++;

                if(roundIterator <= roundNumber) {
                    isRestTimer = !isRestTimer;
                    getPreferences();
                    isActive = false;
                    startTimer();
                } else {
                    stopTimer();
                    isActive = false;
                }
            }
        };

    }


    // Get preferences form xml file
    public void getPreferences() {
        String temp;
        // Get shared preferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        if(!isRestTimer) {
            // Get timerTimer key's value from preferences
            temp = sharedPreferences.getString("trainingTime", "0:0");
            // Split minutes and seconds
            String[] splitString = temp.split(":");
            // Convert minutes and seconds into long and set the timer
            setTotalTime(Long.parseLong(splitString[0]), Long.parseLong(splitString[1]));
        }
        else {
            // Get timerTimer key's value from preferences
            temp = sharedPreferences.getString("restTime", "0:0");
            // Split minutes and seconds
            String[] splitString = temp.split(":");
            // Convert minutes and seconds into long and set the timer
            setTotalTime(Long.parseLong(splitString[0]), Long.parseLong(splitString[1]));
        }

        roundNumber = Integer.parseInt(sharedPreferences.getString("roundsNumber", "-1"));

        // Update timer View
        updateView(milsToFinish);

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
        //Change start button text from "Paused" to "Start"
        startButton.setText("Start");
        if(!isActive) {
            // Hide pause and stop button and show start button
            startButton.setVisibility(View.VISIBLE);
            pauseButton.setVisibility(View.GONE);
            stopButton.setVisibility(View.GONE);
        }
        // Delete the actual timer
        clock.cancel();
        // Reset the milliseconds remaining
        milsToFinish = totalTime;
        // Create a new timer with milliseconds remaining
        clock = createTimer();
        // Reset TextView's text
        updateView(milsToFinish);
    }

}
