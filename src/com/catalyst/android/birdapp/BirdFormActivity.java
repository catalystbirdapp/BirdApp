package com.catalyst.android.birdapp;

import android.os.Bundle;
import android.app.Activity;

import android.content.Intent;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;


public class BirdFormActivity extends Activity {
	
	Spinner categorySpinner;
	Spinner activitySpinner;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bird_form);
		categorySpinner = (Spinner) findViewById(R.id.category_drop_down);
		activitySpinner = (Spinner) findViewById(R.id.bird_acivity_dropdown);
		TextView textView = (TextView)findViewById(R.id.notes_edit_text);
		textView.setMovementMethod(ScrollingMovementMethod.getInstance());
		populateSpinners();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bird_form, menu);
		return true;
	}
	

	
	public void populateSpinners(){
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(BirdFormActivity.this,
		        R.array.category_drop_down_choices, android.R.layout.simple_spinner_item);
		
		ArrayAdapter<CharSequence> activityAdapter = ArrayAdapter.createFromResource(BirdFormActivity.this,
		        R.array.bird_acivity_dropdown_choices, android.R.layout.simple_spinner_item);
		
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		activityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		categorySpinner.setAdapter(adapter);
		activitySpinner.setAdapter(activityAdapter);
		
	}
	

	
	public void getMap(MenuItem item){
		Intent intent = new Intent(getApplication(), Map_Activity.class);
		startActivity(intent);
	}
}
