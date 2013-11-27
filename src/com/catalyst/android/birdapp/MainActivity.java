package com.catalyst.android.birdapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {
	private static final String GPS_PREFERENCE = "GPSPreference";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		MediaPlayer mp = MediaPlayer.create(getApplicationContext(),
				R.raw.dovecooing4302008);
		mp.start();
	}

	public void navigateToSubmission(View v) {
		Intent i = new Intent(this, BirdFormActivity.class);
		startActivity(i);
	}

	public void navigateToRecordsReview(View v) {
		Intent i = new Intent(this, RecordsActivity.class);
		startActivity(i);
	}
	
	/*
	 * 
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		//delete GPS preferences for this session of the app
		String preferencesFileLocation = getString(R.string.preference_file_key);
		SharedPreferences preferences = getSharedPreferences(preferencesFileLocation, Context.MODE_PRIVATE);
    	SharedPreferences.Editor editor = preferences.edit();
    	editor.remove(GPS_PREFERENCE);
    	editor.commit();
	}
	
}
