package com.catalyst.android.birdapp;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class CameraSettingsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera_settings);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bird_form, menu);
		return true;
	}

}
