package com.catalyst.android.birdapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

public class BirdLocationListener  implements LocationListener{
	
	private Context context;
	private LocationManager locationManager;
	private String provider;
	
	private double latitude;
	private double longitude;
	
	Location currentLocation;
	

	/**
	 * Constructor
	 */
	public BirdLocationListener(Context context) {
		this.context = context;
		
		locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		
		//Checks to see if the GPS is on and/or if the user wants it on
		checkForGPS();
		
		Criteria criteria = new Criteria();
		
		//checks to see if the GPS is active
		if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
			criteria.setAccuracy(Criteria.ACCURACY_FINE);
		}
		//Gets the best location provider
		provider = locationManager.getBestProvider(criteria, true);
		
		currentLocation = locationManager.getLastKnownLocation(provider);
		if(currentLocation != null){
			latitude = currentLocation.getLatitude();
			longitude = currentLocation.getLongitude();
		}
		
	}
	
	public Location getLocation(){
		return locationManager.getLastKnownLocation(provider);
	}
	
	/**
	 * Checks to see if the GPS is on.  If it is not and the user wants it on, it will take them
	 * to the GPS settings screen
	 */
	private void checkForGPS() {
		boolean activeGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		if(!activeGPS){
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
				public void onClick(DialogInterface dialogInterface, int notUsed) {
					//alerts the user that the GPS will not be used
					Toast.makeText(context, "GPS will not be used.", Toast.LENGTH_SHORT).show();
					dialogInterface.cancel();
				}
			});
			//shows the alertbox
			alert.show();
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		currentLocation = locationManager.getLastKnownLocation(provider);
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

}
