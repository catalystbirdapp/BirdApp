package com.catalyst.android.birdapp;

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
import android.widget.Toast;
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
		amPmSpinner = (Spinner) findViewById(R.id.am_pm_spinner);
		TextView textView = (TextView)findViewById(R.id.notes_edit_text);
		textView.setMovementMethod(ScrollingMovementMethod.getInstance());
		populateSpinners();
		intializeGPSfields();
		
	}

	private void intializeGPSfields() {
		latitudeEditText = (EditText) findViewById(R.id.latitude_edit_text);
		longitudeEditText = (EditText) findViewById(R.id.longitude_edit_text);
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
	

	
	public void getMap(MenuItem item){
		Intent intent = new Intent(getApplication(), Map_Activity.class);
		startActivity(intent);
	}
	
	private OnCheckedChangeListener autoGPSListener = new OnCheckedChangeListener(){

		@Override
		public void onCheckedChanged(CompoundButton buttonView,	boolean isChecked) {
			locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			if(autoGPS.isChecked()){
				String provider = locationManager.getBestProvider(new Criteria(), true);
				locationManager.requestLocationUpdates(provider, 0, 0, locationListener);
			} else {
				locationManager.removeUpdates(locationListener);
				latitudeEditText.setText("");
				longitudeEditText.setText("");
			}
			
		}
		
	};
	
	private LocationListener locationListener = new LocationListener(){

		@Override
		public void onLocationChanged(Location location) {
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
}
