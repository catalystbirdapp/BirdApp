package com.catalyst.android.birdapp;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class EditFormActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_form);
		
		Bundle bundle = this.getIntent().getExtras();
		BirdSighting birdSighting = (BirdSighting) bundle.getSerializable("BirdSighting");
		
		TextView editFormTextView = (TextView) findViewById(R.id.editFormTextView);
		editFormTextView.setText(birdSighting.getCommonName());
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_form, menu);
		return true;
	}

}
