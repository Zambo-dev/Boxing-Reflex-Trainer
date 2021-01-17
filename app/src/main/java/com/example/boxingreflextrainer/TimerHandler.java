package com.example.boxingreflextrainer;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import androidx.preference.PreferenceManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;


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
    MediaPlayer bellSound,beforeFinish, beforeStart;

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
    long preparationTimeEnd, restTimeEnd, roundTimeEnd;
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

        beforeStart = MediaPlayer.create(context, R.raw.beforestart);
        bellSound = MediaPlayer.create(context, R.raw.thebell);
        beforeFinish = MediaPlayer.create(context, R.raw.beforefinish);

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

                if(isActive) {

                    // Check if round is ending
                    if (milsToFinish >= roundTimeEnd && milsToFinish < roundTimeEnd + 1000 && !isPreparationTime && !isRestTime)
                        beforeFinish.start();

                    else if (milsToFinish >= preparationTimeEnd && milsToFinish < preparationTimeEnd + 1000 && isPreparationTime && !isRestTime)
                        beforeStart.start();

                    else if (milsToFinish >= restTimeEnd && milsToFinish < restTimeEnd + 1000 && !isPreparationTime && isRestTime)
                        beforeStart.start();
                }
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
                        dataCallback.changeBackgroundColor(Color.WHITE);

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
                    dataCallback.changeBackgroundColor(Color.WHITE);
                }
            }
        };

    }


    // Get preferences form xml file
    public void getPreferences() {
        String temp;
        String[] splitString;
        // Get shared preferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        updatePreferences(parseJson("profile_1"));

        if(!isRestTime && !isPreparationTime) {
            // Get timerTimer key's value from preferences
            temp = sharedPreferences.getString("trainingTime", "0:0");
            // Split minutes and seconds
            splitString = temp.split(":");
            // Convert minutes and seconds into long and set the timer
            setTotalTime(Long.parseLong(splitString[0]), Long.parseLong(splitString[1]));
        }
        else if(isRestTime && !isPreparationTime) {
            // Get timerTimer key's value from preferences
            temp = sharedPreferences.getString("restTime", "0:0");
            // Split minutes and seconds
            splitString = temp.split(":");
            // Convert minutes and seconds into long and set the timer
            setTotalTime(Long.parseLong(splitString[0]), Long.parseLong(splitString[1]));
        } else {
            // Get timerTimer key's value from preferences
            temp = sharedPreferences.getString("preparationTime", "0:0");
            // Split minutes and seconds
            splitString = temp.split(":");
            // Convert minutes and seconds into long and set the timer
            setTotalTime(Long.parseLong(splitString[0]), Long.parseLong(splitString[1]));
        }

        roundNumber = Integer.parseInt(sharedPreferences.getString("roundsNumber", "-1"));

        // Get timerTimer key's value from preferences
        temp = sharedPreferences.getString("preparationAlert", "0:0");
        // Split minutes and seconds
        splitString = temp.split(":");
        preparationTimeEnd = convertTime(Long.parseLong(splitString[0]), Long.parseLong(splitString[1]));

        // Get timerTimer key's value from preferences
        temp = sharedPreferences.getString("restAlent", "0:0");
        // Split minutes and seconds
        splitString = temp.split(":");
        restTimeEnd = convertTime(Long.parseLong(splitString[0]), Long.parseLong(splitString[1]));

        // Get timerTimer key's value from preferences
        temp = sharedPreferences.getString("roundAlert", "0:0");
        // Split minutes and seconds
        splitString = temp.split(":");
        roundTimeEnd = convertTime(Long.parseLong(splitString[0]), Long.parseLong(splitString[1]));

        // Update timer View
        updateTime(milsToFinish);

    }

    //Parse json data
    protected JSONObject parseJson(String profileName) {
        String jsonString = null;
        JSONObject jsonObject = null;

        try {
            InputStream inputStream = context.getAssets().open("preferences.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];

            inputStream.read(buffer);
            inputStream.close();
            jsonString = new String(buffer, StandardCharsets.UTF_8);

            jsonObject = new JSONObject(jsonString);
            return jsonObject.getJSONObject(profileName);

        } catch (IOException ex) {
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void updatePreferences(JSONObject profileJson) {
        try {
            SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();

            preferencesEditor.putString("trainingTime", profileJson.getString("training_time"));
            preferencesEditor.putString("restTime", profileJson.getString("rest_time"));
            preferencesEditor.putString("preparationTime", profileJson.getString("preparation_time"));
            preferencesEditor.putString("roundsNumber", profileJson.getString("round_number"));
            preferencesEditor.putString("preparationAlert", profileJson.getString("end_preparation"));
            preferencesEditor.putString("restAlent", profileJson.getString("end_rest"));
            preferencesEditor.putString("roundAlert", profileJson.getString("end_round"));

            preferencesEditor.apply();
        }
        catch (JSONException e) {
            System.out.println(e);
        }
    }

    // Function that start the timer when it's not active
    protected void startTimer() {
        if(!isActive) {
            // Start the timer
            clock.start();
            // Change timer status
            isActive = true;

            if(isPreparationTime)
                dataCallback.changeBackgroundColor(Color.YELLOW);
            else if(isRestTime)
                dataCallback.changeBackgroundColor(Color.RED);
            else {
                dataCallback.changeBackgroundColor(Color.GREEN);
                // Play start sound
                bellSound.start();
            }
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
