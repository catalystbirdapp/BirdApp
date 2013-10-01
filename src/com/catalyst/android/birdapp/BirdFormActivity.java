package com.catalyst.android.birdapp;

import com.catalyst.android.birdapp.utilities.Utilities;

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
	private EditText latitudeEditText, longitudeEditText;
	private Location autoLocation;
	private CheckBox autoGPS;
	private LocationManager locationManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bird_form);
		categorySpinner = (Spinner) findViewById(R.id.category_drop_down);
		activitySpinner = (Spinner) findViewById(R.id.bird_acivity_dropdown);
		TextView textView = (TextView)findViewById(R.id.notes_edit_text);
		textView.setMovementMethod(ScrollingMovementMethod.getInstance());
		populateSpinners();
		displayDateAndTime();
		intializeGPSfields();
		
	}
	
	@Override
	protected void onPause(){
		super.onPause();
		if(autoGPS.isChecked()){
			//Removes the locationListener if the Auto Fill check box is unchecked
			locationManager.removeUpdates(locationListener);
		}
	}
	
	@Override
	protected void onStop(){
		super.onStop();
		if(autoGPS.isChecked()){
			//Removes the locationListener if the Auto Fill check box is unchecked
			locationManager.removeUpdates(locationListener);
		}
	}
	
	@Override
	protected void onRestart(){
		super.onRestart();
		if(autoGPS.isChecked()){
			//Reinstates the location listener is the Auto Fill check box is checked
			String provider = locationManager.getBestProvider(new Criteria(), true);
			Location location = locationManager.getLastKnownLocation(provider);
			//Auto fills the form
			latitudeEditText.setText(Double.toString(location.getLatitude()));
			longitudeEditText.setText(Double.toString(location.getLongitude()));
			//Resets the Location Listener
			locationManager.requestLocationUpdates(provider, 0, 0, locationListener);
		}
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		if(autoGPS.isChecked()){
			//Reinstates the location listener is the Auto Fill check box is checked
			String provider = locationManager.getBestProvider(new Criteria(), true);
			Location location = locationManager.getLastKnownLocation(provider);
			//Auto fills the form
			latitudeEditText.setText(Double.toString(location.getLatitude()));
			longitudeEditText.setText(Double.toString(location.getLongitude()));
			//Resets the Location Listener
			locationManager.requestLocationUpdates(provider, 0, 0, locationListener);
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
		
		//amPmAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		activityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		categorySpinner.setAdapter(adapter);
		activitySpinner.setAdapter(activityAdapter);
		
	}
	

	/**
	 * Takes the user to the google map
	 */
	public void getMap(MenuItem item){
		//Takes the user to the Map Activity
		Intent intent = new Intent(getApplication(), Map_Activity.class);
		startActivity(intent);
	}
	
	/**
	 * Checks to see if the GPS is on.  If it is not and the user wants it on, it will take them
	 * to the GPS settings screen
	 */
	private void checkForGPS() {
		if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
			AlertDialog alert = new AlertDialog.Builder(this).create();
			
			//Sets alert box title and message
			alert.setTitle("GPS is turned OFF");
			alert.setMessage("Would you like to activate the GPS?");
			
			//Sets the alert box YES button and listener
			alert.setButton(DialogInterface.BUTTON_POSITIVE, "YES", new OnClickListener(){
				@Override
				public void onClick(DialogInterface dialogInterface, int notUsed) {
					//Sends the user to the GPS settings
					Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					startActivity(intent);
					dialogInterface.cancel();
				}
			});
			//Sets the alert box NO button and listener
			alert.setButton(DialogInterface.BUTTON_NEGATIVE, "NO", new OnClickListener(){
				@Override
				public void onClick(final DialogInterface dialogInterface, int notUsed) {
					dialogInterface.cancel();
				}
			});
			//shows the alertbox
			alert.show();
		}
	}
	
	//OnCheckedChangeListener for the Coordinate autofill checkbox. 
	private OnCheckedChangeListener autoGPSListener = new OnCheckedChangeListener(){

		@Override
		public void onCheckedChanged(CompoundButton buttonView,	boolean isChecked) {
			locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

			if(autoGPS.isChecked()){
				//Checks to see if the GPS is enabled
				checkForGPS();
				//Gets the best location provider
				String provider = locationManager.getBestProvider(new Criteria(), true);
				//Gets the user's location
				Location location = locationManager.getLastKnownLocation(provider);
				//Auto fills the form
				latitudeEditText.setText(Double.toString(location.getLatitude()));
				longitudeEditText.setText(Double.toString(location.getLongitude()));
				//Checks if there are any coordinates in the edit text boxes.  If they are empty then the coordinates are unavailable
				if(latitudeEditText.getText().length() == 0 || longitudeEditText.getText().length() == 0){
					latitudeEditText.setText("Coordinates not available");
					longitudeEditText.setText("Coordinates not available");
				} else {
					//Sets the listener for location changes.  The two 0's are for minimum time and distance to check for an update
					locationManager.requestLocationUpdates(provider, 0, 0, locationListener);
				}
			} else {
				//If the user is unchecking the box, the latitude and longitude boxes are cleared, and the location listener is removed.
				locationManager.removeUpdates(locationListener);
				latitudeEditText.setText("");
				longitudeEditText.setText("");
			}
			
		}
		
	};
	
	//Location Listener for the coordinate auto fill boxes
	private LocationListener locationListener = new LocationListener(){

		@Override
		public void onLocationChanged(Location location) {
			//Updates the auto filled GPS Coordinates on the form whenever your location is changed
			latitudeEditText.setText(Double.toString(location.getLatitude()));
			longitudeEditText.setText(Double.toString(location.getLongitude()));
			
		}

		@Override
		public void onProviderDisabled(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			// TODO Auto-generated method stub
			
		}
		
		
	};
	
	private void displayDateAndTime(){
		Utilities util = new Utilities();
		TextView date = (TextView) findViewById(R.id.date_time_edit_text);
		TextView time = (TextView) findViewById(R.id.hour_edit_text);
		date.setText(util.formatDate(util.currentMillis()));
		time.setText(util.formatTime(util.currentMillis()));
	}
}
