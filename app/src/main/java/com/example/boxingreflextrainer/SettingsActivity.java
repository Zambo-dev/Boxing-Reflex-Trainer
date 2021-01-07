package com.example.boxingreflextrainer;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.preference.PreferenceManager;


public class SettingsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setTitle("Settings");

        // Insert activity_settings.xml into SettingsActivity
        setContentView(R.layout.activity_settings);
        PreferenceManager.setDefaultValues(this, R.xml.settings_preferences, false);

        // Connect fragment to activity_settings.xml
        getSupportFragmentManager().beginTransaction().replace(R.id.SettingsContainer, new SettingsFragment()).commit();

    }
}