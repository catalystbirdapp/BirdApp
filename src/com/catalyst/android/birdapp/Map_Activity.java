package com.catalyst.android.birdapp;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

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
import android.view.Menu;

public class Map_Activity extends Activity {
	
	private LocationManager locationManager;

	private GoogleMap map;
	
	private LatLng location;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		//Sets up the location manager
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		//gets the map fragment from the page to modify it
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		checkForGPS();
				
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
					updateMap();
				}
			});
			//Sets the alert box NO button and listener
			alert.setButton(DialogInterface.BUTTON_NEGATIVE, "NO", new OnClickListener(){
				@Override
				public void onClick(final DialogInterface dialogInterface, int notUsed) {
					dialogInterface.cancel();
					updateMap();
				}
			});
			//shows the alertbox
			alert.show();
		}
	}
	
	private void updateMap(){
		String provider = locationManager.getBestProvider(new Criteria(), true);
		//Gets the current location
		Location currentLocation = null;
		if(provider != null)currentLocation = locationManager.getLastKnownLocation(provider);
		
		try{
			//Updates the map to your location and zooms in.  The number is the amount of zoom
			location = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
			CameraUpdate update = CameraUpdateFactory.newLatLngZoom(location, 17);
			Marker myMarker = map.addMarker(new MarkerOptions().position(location).title("My Location"));
			map.animateCamera(update);
		} catch(NullPointerException e){
			
		}
	}
	
	@Override
	protected void onPause(){
		super.onPause();
	}

	@Override
	protected void onResume(){
		super.onResume();
		//Sets up the location manager
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		//gets the map fragment from the page to modify it
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		
		String provider = locationManager.getBestProvider(new Criteria(), true);
		//Gets the current location
		Location currentLocation = null;
		if(provider != null)currentLocation = locationManager.getLastKnownLocation(provider);
		try{
			location = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
			//Updates the map to your location and zooms in
			CameraUpdate update = CameraUpdateFactory.newLatLngZoom(location, 17);
			map.animateCamera(update);
		}catch(NullPointerException e){
			
		}
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map_, menu);
		return true;
	}

}
