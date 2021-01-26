package com.example.boxingreflextrainer;

import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.preference.PreferenceManager;


public class SettingsActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Change action bar title
        getSupportActionBar().setTitle("Settings");

        // Insert activity_settings.xml into SettingsActivity
        setContentView(R.layout.activity_settings);
        PreferenceManager.setDefaultValues(this, R.xml.settings_preferences, false);

        // Connect fragment to activity_settings.xml
        getSupportFragmentManager().beginTransaction().replace(R.id.SettingsContainer, new SettingsFragment()).commit();
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

        // Hide settings button while in SettingsActivity
        settingsButton.setVisible(false);

        // Set click listener on action bar menu
        backButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Open Settings activity
                SettingsActivity.this.finish();
                return true;
            }
        });

        return true;
    }

}