package com.catalyst.android.birdapp;

import android.app.Activity;
import android.os.Bundle;

public class BirdFormSettingsActivity extends Activity {
	
	public static final String KEY_PREF_GPS_PREFERENCE = "GPSPreference";

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new BirdFormSettingsFragment())
                .commit();
    }
	
}
