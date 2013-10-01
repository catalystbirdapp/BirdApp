package com.catalyst.android.birdapp.GPS_Utility;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.EditText;

public class GPSUtility {
	
	private Context context;
	private LocationManager locationManager;
	private EditText latitudeEditText, longitudeEditText;
	
	//Location Listener for the coordinate auto fill boxes
	private LocationListener formLocationListener = new LocationListener(){
		@Override
		public void onLocationChanged(Location location) {
			//Updates the auto filled GPS Coordinates on the form whenever your location is changed
			try{
				latitudeEditText.setText(Double.toString(location.getLatitude()));
				longitudeEditText.setText(Double.toString(location.getLongitude()));
			}catch(NullPointerException e){
				
			}
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
	

	public GPSUtility(Context context) {
		super();
		this.context = context;
		locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
	}
	
	public void removeFormLocationUpdates(){
		//Removes the locationListener if the Auto Fill check box is unchecked
		locationManager.removeUpdates(formLocationListener);
	}
	
	public Location getCurrentLocation(){
		Location currentLocation = null;
		String provider = locationManager.getBestProvider(new Criteria(), true);
		//Gets the current location
		if(provider != null)currentLocation = locationManager.getLastKnownLocation(provider);
		return currentLocation;
	}
	
	public void setFormLocationListener(){
		String provider = locationManager.getBestProvider(new Criteria(), true);
		if(provider != null)locationManager.requestLocationUpdates(provider, 0, 0, formLocationListener);
	}
	
	/**
	 * Checks to see if the GPS is on.  If it is not and the user wants it on, it will take them
	 * to the GPS settings screen
	 */
	public void checkForGPS() {
		if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
			AlertDialog alert = new AlertDialog.Builder(context).create();
			
			//Sets alert box title and message
			alert.setTitle("GPS is turned OFF");
			alert.setMessage("Would you like to activate the GPS?");
			
			//Sets the alert box YES button and listener
			alert.setButton(DialogInterface.BUTTON_POSITIVE, "YES", new OnClickListener(){
				@Override
				public void onClick(DialogInterface dialogInterface, int notUsed) {
					//Sends the user to the GPS settings
					Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					context.startActivity(intent);
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
	
	public void noLocationAvailable(){
		AlertDialog alert = new AlertDialog.Builder(context).create();
		
		//Sets alert box title and message
		alert.setTitle("Location Unavailable");
		alert.setMessage("Your Location is unavailable.");
		
		//Sets the alert box YES button and listener
		alert.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new OnClickListener(){
			@Override
			public void onClick(DialogInterface dialogInterface, int notUsed) {
				dialogInterface.cancel();
			}
		});
		//shows the alertbox
		alert.show();
	}

}
