package com.example.boxingreflextrainer;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements TimerCallbacks {
    // Global class variables
    private Button startButton;
    private Button pauseButton;
    private Button stopButton;
    private TextView timerView, roundView;
    private TimerHandler timerHandler;
    private View main;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize buttons and views
        startButton = findViewById(R.id.StartButton);
        pauseButton = findViewById(R.id.PauseButton);
        stopButton = findViewById(R.id.StopButton);
        timerView = findViewById(R.id.Timer);
        roundView = findViewById(R.id.RoundView);
        main = findViewById(R.id.Main);

        // Initialize timerHandler with constructor
        timerHandler = new TimerHandler(0, 0, this);
        // Get shared preferences
        timerHandler.getPreferences();

        // Set click listener on start button
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call StartTimer function from TimerHandler class
                timerHandler.startTimer();
                // Hide start button and show pause and stop buttons
                startButton.setVisibility(View.GONE);
                pauseButton.setVisibility(View.VISIBLE);
                stopButton.setVisibility(View.VISIBLE);
            }
        });

        // Set click listener on pause button
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call PauseTimer function from TimerHandler class
                timerHandler.pauseTimer();
                //Change start button text from "Start" to "Paused"
                startButton.setText("Paused");
                // Hide pause and stop button and show start button
                startButton.setVisibility(View.VISIBLE);
                pauseButton.setVisibility(View.GONE);
                stopButton.setVisibility(View.GONE);
                changeBackgroundColor(Color.WHITE);
            }
        });

        // Set click listener on pause button
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reset boolean variables
                timerHandler.isActive = false;
                timerHandler.isRestTime = false;
                timerHandler.isPreparationTime = true;
                timerHandler.roundIterator = 0;
                // Call stopTimer
                timerHandler.stopTimer();
                changeBackgroundColor(Color.WHITE);
            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();
        // Restore action bar title
        getSupportActionBar().setTitle("Boxing Reflex Trainer");
        // Get shared preference time
        timerHandler.getPreferences();
    }


    @Override
    // Create settings menu in the action bar
    public boolean onCreateOptionsMenu(Menu menu) {
        // Connect action bar xml file
        getMenuInflater().inflate(R.menu.settings_button, menu);
        // Set menu item from xml file
        MenuItem settingsButton = menu.findItem(R.id.settings_button);
        // Set click listener on action bar menu
        settingsButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Open Settings activity
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                return true;
            }
        });

        return true;
    }


    @Override
    // Update MainActivity Views
    public void dataView(long min, long sec, int iterator , int round) {
        timerView.setText(String.format("%d:%d", min, sec));
        roundView.setText(String.format("Round: %d/%d", iterator + 1, round));
    }

    @Override
    // Reset MainActivity buttons
    public void resetButtons() {
        startButton.setText("Start");
        startButton.setVisibility(View.VISIBLE);
        pauseButton.setVisibility(View.GONE);
        stopButton.setVisibility(View.GONE);
    }

    // Change background color to the given color
    public void changeBackgroundColor(int color) {
        main.setBackgroundColor(color);
    }


}