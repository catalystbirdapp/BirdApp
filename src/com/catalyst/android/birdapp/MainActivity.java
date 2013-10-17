package com.catalyst.android.birdapp;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

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
}
