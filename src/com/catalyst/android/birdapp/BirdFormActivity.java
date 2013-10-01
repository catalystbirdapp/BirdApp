package com.catalyst.android.birdapp;

import com.catalyst.android.birdapp.GPS_Utility.GPSUtility;
import com.google.android.gms.common.GooglePlayServicesUtil;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.app.Activity;
import android.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


public class BirdFormActivity extends Activity {
	
	private Spinner categorySpinner;
	private Spinner activitySpinner;
	private Spinner amPmSpinner;
	private CheckBox autoGPS;
	private GPSUtility gpsUtility;
	private EditText latitudeEditText, longitudeEditText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bird_form);
		//Checks to see if the device has google play services.
		GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
		//Sets up the GPS Utility class
		gpsUtility = new GPSUtility(this);
		categorySpinner = (Spinner) findViewById(R.id.category_drop_down);
		activitySpinner = (Spinner) findViewById(R.id.bird_acivity_dropdown);
		amPmSpinner = (Spinner) findViewById(R.id.am_pm_spinner);
		TextView textView = (TextView)findViewById(R.id.notes_edit_text);
		textView.setMovementMethod(ScrollingMovementMethod.getInstance());
		populateSpinners();
		//Grabs the fields needed for gps autofill
		intializeGPSfields();
		
	}
	
	@Override
	protected void onPause(){
		super.onPause();
		if(autoGPS.isChecked()){
			//Removes the locationListener if the Auto Fill check box is unchecked
			gpsUtility.removeFormLocationUpdates();
		}
	}
	
	@Override
	protected void onStop(){
		super.onStop();
		if(autoGPS.isChecked()){
			//Removes the locationListener if the Auto Fill check box is unchecked
			gpsUtility.removeFormLocationUpdates();
		}
	}
	
	@Override
	protected void onRestart(){
		super.onRestart();
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		if(autoGPS.isChecked()){
			gpsUtility.checkForGPS();
			Location location = gpsUtility.getCurrentLocation();
			//Auto fills the form
			latitudeEditText.setText(Double.toString(location.getLatitude()));
			longitudeEditText.setText(Double.toString(location.getLongitude()));
			//resets the location listener
			gpsUtility.setFormLocationListener();	
		}
	}

	private void intializeGPSfields() {
		//Grabs the edit texts fields from the page so that they can be edited
		latitudeEditText = (EditText) findViewById(R.id.latitude_edit_text);
		longitudeEditText = (EditText) findViewById(R.id.longitude_edit_text);
		//Grabs the coordinate autofill checkbox from the page and sets the OnCheckChangeListener
		autoGPS = (CheckBox) findViewById(R.id.autofill_checkbox);
		autoGPS.setOnCheckedChangeListener(autoGPSListener);
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
		
		ArrayAdapter<CharSequence> amPmAdapter = ArrayAdapter.createFromResource(BirdFormActivity.this,
		        R.array.am_pm_choices, android.R.layout.simple_spinner_item);
		
		amPmAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		activityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		amPmSpinner.setAdapter(amPmAdapter);
		categorySpinner.setAdapter(adapter);
		activitySpinner.setAdapter(activityAdapter);
		
	}
	

	/**
	 * Takes the user to the google map
	 */
	public void getMap(MenuItem item){
		//Takes the user to the Map Activity
		Intent intent = new Intent(getApplication(), MapActivity.class);
		startActivity(intent);
	}
	
	private void autoFillCoordinates(){
		Location location = gpsUtility.getCurrentLocation();
		//Auto fills the form
		if(location != null){
			latitudeEditText.setText(Double.toString(location.getLatitude()));
			longitudeEditText.setText(Double.toString(location.getLongitude()));
		}
		//Checks if there are any coordinates in the edit text boxes.  If they are empty then the coordinates are unavailable
		if(latitudeEditText.getText().length() == 0 || longitudeEditText.getText().length() == 0){
			gpsUtility.noLocationAvailable();
			latitudeEditText.setText("Coordinates not available");
			longitudeEditText.setText("Coordinates not available");
		} else {
			//If everything is good, this sets the location listener
			gpsUtility.setFormLocationListener();
		}
	}
	
	//OnCheckedChangeListener for the Coordinate autofill checkbox. 
	private OnCheckedChangeListener autoGPSListener = new OnCheckedChangeListener(){

		@Override
		public void onCheckedChanged(CompoundButton buttonView,	boolean isChecked) {
			if(autoGPS.isChecked()){
				gpsUtility.checkForGPS();
				autoFillCoordinates();
			} else {
				//If the user is unchecking the box, the latitude and longitude boxes are cleared, and the location listener is removed.
				gpsUtility.removeFormLocationUpdates();
				latitudeEditText.setText("");
				longitudeEditText.setText("");
			}
			
		}
		
	};
	
	
}
