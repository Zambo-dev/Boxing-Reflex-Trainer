package com.example.boxingreflextrainer;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    // Global class variables
    private Button startButton;
    private Button pauseButton;
    private Button stopButton;
    private TimerHandler timerHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get button object
        startButton = findViewById(R.id.StartButton);
        pauseButton = findViewById(R.id.PauseButton);
        stopButton = findViewById(R.id.StopButton);

        // Pass timer view and time to TimerHandler class with constructor
        timerHandler = new TimerHandler(this.<TextView>findViewById(R.id.Timer), this.<Button>findViewById(R.id.StartButton), this.<Button>findViewById(R.id.PauseButton), this.<Button>findViewById(R.id.StopButton), 0, 0, this);
        // Get shared preference time
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
            }
        });

        // Set click listener on pause button
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Change timer status
                timerHandler.isActive = false;
                // Call StopTimer function from TimerHandler class
                timerHandler.stopTimer();
                timerHandler.isRestTimer = false;
                timerHandler.roundIterator = 0;
                timerHandler.getPreferences();
            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();

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

}