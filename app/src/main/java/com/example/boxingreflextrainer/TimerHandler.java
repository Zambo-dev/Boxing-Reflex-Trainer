package com.example.boxingreflextrainer;

import android.os.CountDownTimer;
import android.widget.TextView;

// This class manage the timer functions
public class TimerHandler {
    // Create TextView object
    public TextView timer;
    // Timer's remaining time
    long milsToFinish;
    // Total time got from settings file
    long totalTime;
    // Create timer object
    CountDownTimer clock;
    // Timer activty check
    boolean isActive = false;
    final int timeConversionValue = 60000;

    // Constructor
    TimerHandler(TextView template, float mils) {
        // Set total time
        totalTime = convertTime(mils);
        milsToFinish = totalTime;
        // Set timer TextView
        timer = template;
        // Set timer text
        updateTimerView(milsToFinish);
        // Create timer
        clock = createTimer();
    }

    // Update timer View
    public void updateTimerView(long time) {
        long minutes = time / timeConversionValue;
        long seconds = (time % timeConversionValue) / 1000;

        timer.setText(String.format("%d:%d", minutes, seconds));
    }

    // Set max time
    public void setTotalTime(float time) {
        this.totalTime = convertTime(time);
        stopTimer();
    }

    // Convert time into milliseconds
    public long convertTime(float time) {
        time *= 60000;
        return (long)time;
    }

    // Function that create the timer
    protected CountDownTimer createTimer() {
        return new CountDownTimer(milsToFinish, 1000) {

            @Override
            // Every tick update milliseconds remaining and TextView's text
            public void onTick(long millisUntilFinished) {
                milsToFinish = millisUntilFinished;
                updateTimerView(milsToFinish);
            }

            // When timer reach to the end print on TextView
            public void onFinish() {
                timer.setText("Done!");
            }
        };

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
        // Reset the milliseconds remaining
        milsToFinish = totalTime;
        // Create a new timer with milliseconds remaining
        clock = createTimer();
        // Change timer status
        isActive = false;
        // Reset TextView's text
        updateTimerView(milsToFinish);
    }

}
