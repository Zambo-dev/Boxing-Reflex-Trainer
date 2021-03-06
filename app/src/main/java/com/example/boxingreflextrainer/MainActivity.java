package com.example.boxingreflextrainer;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements TimerCallbacks
{

    // Global class variables
    private Button startButton, pauseButton, stopButton, addProfileButton;
    private TextView timerView, roundView;
    private TimerHandler timerHandler;
    private FileHandler fileHandler;
    private View main;
    private Spinner dropdown;
    private boolean isPaused = false;
    private int profileArrayIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize buttons and views
        startButton = findViewById(R.id.StartButton);
        pauseButton = findViewById(R.id.PauseButton);
        stopButton = findViewById(R.id.StopButton);
        timerView = findViewById(R.id.Timer);
        roundView = findViewById(R.id.RoundView);
        addProfileButton = findViewById(R.id.profileButton);
        main = findViewById(R.id.Main);
        dropdown = findViewById(R.id.profileSelector);

        fileHandler = new FileHandler(this);
        // Initialize timerHandler with constructor
        timerHandler = new TimerHandler(0, 0, this);
        // Get shared preferences
        timerHandler.getPreferences();

        // Set click listener on start button
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
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
            public void onClick(View v)
            {
                if(!isPaused)
                {
                    // Call PauseTimer function from TimerHandler class
                    timerHandler.pauseTimer();
                    // Change pause button action
                    pauseButton.setText("Start");
                    changeBackgroundColor(Color.WHITE);
                }
                else
                {
                    // Call PauseTimer function from TimerHandler class
                    timerHandler.startTimer();
                    // Hide start button and show pause and stop buttons
                    startButton.setVisibility(View.GONE);
                    pauseButton.setVisibility(View.VISIBLE);
                    stopButton.setVisibility(View.VISIBLE);
                }
                isPaused = !isPaused;
            }
        });


        // Set click listener on pause button
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
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


        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                profileArrayIndex = position;
                timerHandler.activeProfile = parent.getItemAtPosition(profileArrayIndex).toString();
                fileHandler.activeProfile = parent.getItemAtPosition(profileArrayIndex).toString();
                timerHandler.getPreferences();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        addProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String profile = String.format("profile_%d",fileHandler.profileNumber);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, fileHandler.profilesArray);
                dropdown.setAdapter(adapter);
                fileHandler.insertDefaultData(fileHandler.filePath, profile);
                fileHandler.activeProfile = profile;
                timerHandler.activeProfile = fileHandler.activeProfile;
                timerHandler.getPreferences();
            }
        });

    }


    @Override
    public void onResume()
    {
        super.onResume();
        // Restore action bar title
        Objects.requireNonNull(getSupportActionBar()).setTitle("Boxing Reflex Trainer");
        fileHandler.updateJson(timerHandler.sharedPreferences);
        // Get shared preference time
        timerHandler.getPreferences();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, fileHandler.profilesArray);
        dropdown.setAdapter(adapter);
    }


    @Override
    // Create settings menu in the action bar
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Connect action bar xml file
        getMenuInflater().inflate(R.menu.action_bar, menu);
        // Set menu item from xml file
        MenuItem settingsButton = menu.findItem(R.id.settings_button);
        MenuItem backButton = menu.findItem(R.id.back_button);

        // Hide backButton while in MainActivity
        backButton.setVisible(false);

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
    public void dataView(long min, long sec, int iterator , int round)
    {
        timerView.setText(String.format("%2d:%2d", min, sec).replace(" ", "0"));
        roundView.setText(String.format("Round: %d/%d", iterator + 1, round));
    }


    @Override
    // Reset MainActivity buttons
    public void resetButtons()
    {
        startButton.setVisibility(View.VISIBLE);
        pauseButton.setVisibility(View.GONE);
        stopButton.setVisibility(View.GONE);
    }

    @Override
    // Change background color to the given color
    public void changeBackgroundColor(int color) {
        main.setBackgroundColor(color);
    }

}