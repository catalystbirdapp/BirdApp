package com.catalyst.android.birdapp;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
		MediaPlayer mp = MediaPlayer.create(getApplicationContext(),
				R.raw.dovecooing4302008);
		mp.start();
	}

	public void navigateToSubmission(View v) {
		Intent i = new Intent(this, BirdFormActivity.class);
		startActivity(i);
	}

	public void navigateToRecordsReview(View v) {
		Intent i = new Intent(this, ViewPastSightingsActivity.class);
		startActivity(i);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	/**
	 * Opens the application settings
	 */
	public void openApplicationSettings(MenuItem menuItem) {
		Intent intent = new Intent(MainActivity.this, ApplicationSettingsActivity.class);
		startActivity(intent);
	}
	
	/**
	 * pulls up AddNewActivity view
	 */
	public void getAddNewActivity(MenuItem menuItem) {
		Intent intent = new Intent(getApplication(), AddNewActivity.class);
		startActivity(intent);
	}
	
	/**
	 * pulls up AboutUsActivity view
	 */
	public void openAboutUs(MenuItem menuItem) {
		Intent intent = new Intent(getApplication(), AboutUsActivity.class);
		startActivity(intent);
	}
	
}
