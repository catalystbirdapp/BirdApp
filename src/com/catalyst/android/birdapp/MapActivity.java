package com.catalyst.android.birdapp;

import java.util.List;

import com.catalyst.android.birdapp.GPS_Utility.GPSUtility;
import com.catalyst.android.birdapp.database.DatabaseHandler;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;

public class MapActivity extends Activity {
	
	private LocationManager locationManager;

	private GoogleMap map;
	
	private LatLng location;
	
	private GPSUtility gpsUtility;
	
	private DatabaseHandler dbHandler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		//Sets up the GPS Utility class
		gpsUtility = new GPSUtility(this);
		//Sets up the location manager
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		//gets the map fragment from the page to modify it
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		dbHandler = DatabaseHandler.getInstance(this);		
	}
	
	private void updateMap(){
		Location currentLocation = gpsUtility.getCurrentLocation();
		try{
			//Updates the map to your location and zooms in.  The number is the amount of zoom
			location = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
			CameraUpdate update = CameraUpdateFactory.newLatLngZoom(location, 17);
//			map.addMarker(new MarkerOptions().position(location).title("My Location"));
			addMarkersForPreviousSightings();
			map.animateCamera(update);
		} catch(NullPointerException e){
			gpsUtility.noLocationAvailable();
		}
	}
	
    private void addMarkersForPreviousSightings() {
        List<BirdSighting> allBirdSightings = dbHandler.getAllBirdSightings();
      
        for(int index = 0; index < allBirdSightings.size(); index++){
        	
                        BirdSighting birdSighting = allBirdSightings.get(index);
                        LatLng birdSightingLocation = new LatLng(birdSighting.getLatitude(), birdSighting.getLongitude());
                        map.addMarker(new MarkerOptions().position(birdSightingLocation));
        }
        
}

	
	@Override
	protected void onPause(){
		super.onPause();
		
	}
	
	@Override
	protected void onStop(){
		super.onStop();
		
	}
	
	@Override
	protected void onRestart(){
		super.onRestart();
	}

	@Override
	protected void onResume(){
		super.onResume();
		gpsUtility.checkForGPS();
		if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) updateMap();
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map_, menu);
		return true;
	}
	
}
