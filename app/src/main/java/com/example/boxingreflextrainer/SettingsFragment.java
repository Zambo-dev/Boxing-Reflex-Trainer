package com.example.boxingreflextrainer;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat {
    // Create preference change listeneer object
    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // Set preference form xml file
        setPreferencesFromResource(R.xml.settings_preferences, rootKey);

        /*
        preferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (key.equals("timerTime")) {
                    String s = sharedPreferences.getString(key, "0");
                    System.out.println("Questa è la stringa " + s);
                }
            }
        };
        */

    }

    @Override
    public void onResume() {
        super.onResume();
        // Update from xml when app in open
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(preferenceChangeListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Don't update when app is in background
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(preferenceChangeListener);
    }
}
