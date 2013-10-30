package com.catalyst.android.birdapp;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.catalyst.android.birdapp.database.DatabaseHandler;
import com.catalyst.android.birdapp.GPS_Utility.GPSUtility;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends Activity {

	private static final String MAP_TYPE_PREFERENCE_KEY = "com.catalyst.birdapp.mapType";

	private static final int ZERO = 0;

	private static final int PADDING_BETWEEN_TITLE_AND_INFO = 50;

	private static final int INFO_TEXT_VIEW_WIDTH = 300;

	private LocationManager locationManager;

	private GoogleMap map;

	private LatLng location;

	private GPSUtility gpsUtility;

	private DatabaseHandler dbHandler;

	private HashMap<Marker, BirdSighting> markerSightingsMap;

	private TableLayout mapInfoWindow;

	private ImageButton mapSettingsButton;

	private boolean settingsOnScreen = false;

	private RelativeLayout mapLayout;

	private View mapSettingsView;
	private Spinner mapTypeSpinner;
	private Button mapSettingsSaveButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		// Sets up the GPS Utility class
		gpsUtility = new GPSUtility(this);
		// Sets up the location manager
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		// gets the map fragment from the page to modify it
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		dbHandler = DatabaseHandler.getInstance(this);
		markerSightingsMap = new HashMap<Marker, BirdSighting>();
		mapSettingsButton = (ImageButton) findViewById(R.id.map_settings_button);
		mapLayout = (RelativeLayout) findViewById(R.id.mapLayout);
		mapSettingsView = getLayoutInflater().inflate(R.layout.map_settings,
				mapLayout, false);
		setMapSettingsButtonOnClickListener();
		// Sets the custom pop up windows for the map
		setMapMarkerInfoWindowAdapter();

		// Gets the saved preferences for map type
		SharedPreferences preferenses = getPreferences(MODE_PRIVATE);
		String savedMapType = preferenses.getString(MAP_TYPE_PREFERENCE_KEY,
				getString(R.string.normal));
		setMapType(savedMapType);

	}

	/**
	 * sets the map type to the one that the user saved
	 */
	private void setMapType(String savedMapType) {
		if (savedMapType.equals(getString(R.string.normal))) {
			map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		} else if (savedMapType.equals(getString(R.string.satellite))) {
			map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
		} else if (savedMapType.equals(getString(R.string.terrain))) {
			map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
		} else if (savedMapType.equals(getString(R.string.hybrid))) {
			map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		}

	}

	/**
	 * sets up the custom window adapter
	 */
	private void setMapMarkerInfoWindowAdapter() {
		map.setInfoWindowAdapter(new InfoWindowAdapter() {

			@Override
			public View getInfoContents(Marker marker) {
				// Creates the view to put all of the information into
				View view = getLayoutInflater().inflate(
						R.layout.map_window_adapter, null);

				// Pulls the table layout out of the view so that table rows
				// could be added to it.
				mapInfoWindow = (TableLayout) view
						.findViewById(R.id.map_info_window);

				try {
					// Retrieves the bird sighting from the hashmap
					BirdSighting birdSighting = markerSightingsMap.get(marker);

					// Gets the date from the bird sighting and formats the date
					// to the date format that the person has selected for their
					// phone
					Date birdSightingDate = birdSighting.getDateTime();
					java.text.DateFormat dateFormat = DateFormat
							.getDateFormat(getApplicationContext());
					String formattedDate = dateFormat.format(birdSightingDate);

					// formats the time to the time format that the person has
					// selected for their phone
					java.text.DateFormat timeFormat = DateFormat
							.getTimeFormat(getApplicationContext());
					String formattedTime = timeFormat.format(birdSightingDate);

					// calls the method that constructs the table rows and
					// inserts the information into them. The if statements
					// keeps out rows if the information is empty.
					if (birdSighting.getCommonName().length() > 0) {
						addBirdInfoToMapInfoWindow(
								getString(R.string.birdName),
								birdSighting.getCommonName());
					}
					if (birdSighting.getScientificName().length() > 0) {
						addBirdInfoToMapInfoWindow(
								getString(R.string.scientificName),
								birdSighting.getScientificName());
					}
					addBirdInfoToMapInfoWindow(getString(R.string.dateText),
							formattedDate);
					addBirdInfoToMapInfoWindow(getString(R.string.timeText),
							formattedTime);
					addBirdInfoToMapInfoWindow(
							getString(R.string.activityText),
							birdSighting.getActivity());
					if (birdSighting.getNotes().length() > 0) {
						addBirdInfoToMapInfoWindow(
								getString(R.string.noteText),
								birdSighting.getNotes());
					}
				} catch (NullPointerException e) {
					// This is thrown when clicking on the default marker for
					// current location
					TextView currentLocationTextView = new TextView(
							getApplicationContext());
					currentLocationTextView
							.setText(getString(R.string.current_location));
					mapInfoWindow.addView(currentLocationTextView);
				}
				return view;
			}

			/**
			 * Adds info to a table row and puts it in the pop up window for the
			 * map.
			 */
			private void addBirdInfoToMapInfoWindow(String rowTitle, String info) {
				TableRow tableRow = new TableRow(getApplicationContext());

				// Creates the text view for the title of the row
				TextView titleTextView = new TextView(getApplicationContext());
				titleTextView.setText(rowTitle);

				// Creates the text view for the row's information
				TextView infoTextView = new TextView(getApplicationContext());
				infoTextView.setText(info);

				// Sets the padding and width, adds them to the table row, then
				// adds the table row to the view
				infoTextView.setWidth(INFO_TEXT_VIEW_WIDTH);
				titleTextView.setPadding(ZERO, ZERO,
						PADDING_BETWEEN_TITLE_AND_INFO, ZERO);
				tableRow.addView(titleTextView);
				tableRow.addView(infoTextView);
				mapInfoWindow.addView(tableRow);
			}

			@Override
			public View getInfoWindow(Marker marker) {
				return null;
			}

		});

	}

	/**
	 * Adds a marker for the person's current location and then adds markers for
	 * the past sightings. Then it zooms the camera in on the person's current
	 * location
	 */
	private void updateMap() {
		Location currentLocation = gpsUtility.getCurrentLocation();
		try {
			// Clears the map for updating
			map.clear();
			// Updates the map to your location and zooms in. The number is the
			// amount of zoom
			location = new LatLng(currentLocation.getLatitude(),
					currentLocation.getLongitude());
			CameraUpdate update = CameraUpdateFactory.newLatLngZoom(location,
					17);
			map.addMarker(new MarkerOptions().position(location).title(
					"My Location"));
			map.animateCamera(update);
		} catch (NullPointerException e) {
			gpsUtility.noLocationAvailable();
		}
	}

	/**
	 * Adds the markers to the map for the sightings that have been stored in
	 * the DB
	 */
	private void addMarkersForPreviousSightings() {
		// Retrieves all of the bird sightings from the DB
		List<BirdSighting> allBirdSightings = dbHandler.getAllBirdSightings();

		for (int index = 0; index < allBirdSightings.size(); index++) {
			BirdSighting birdSighting = allBirdSightings.get(index);
			// Gets the LatLng location from the sighting for placement on the
			// map
			LatLng birdSightingLocation = new LatLng(
					birdSighting.getLatitude(), birdSighting.getLongitude());
			Marker mapMarker = map.addMarker(new MarkerOptions().position(
					birdSightingLocation).icon(getMapIcon(birdSighting)));
			// Adds the birdsighting to the hasmap so that the birdsighting can
			// be retrieved by passing in the marker
			markerSightingsMap.put(mapMarker, birdSighting);
		}

	}

	/**
	 * Returns the proper icon according to the category of the sighting.
	 */
	private BitmapDescriptor getMapIcon(BirdSighting birdSighting) {
		BitmapDescriptor bitmap = null;
		// Checks the category of the sighting and returns the proper image for
		// the map marker
		if (birdSighting.getCategory().equals(getString(R.string.sighting))) {
			bitmap = BitmapDescriptorFactory
					.fromResource(R.drawable.bird_map_icon);
		} else if (birdSighting.getCategory().equals(getString(R.string.nest))) {
			bitmap = BitmapDescriptorFactory
					.fromResource(R.drawable.nest_map_icon);
		} else {
			bitmap = BitmapDescriptorFactory
					.fromResource(R.drawable.misc_map_icon);
		}
		return bitmap;
	}

	/**
	 * Sets the on click listener for the settings button
	 */
	private void setMapSettingsButtonOnClickListener() {
		mapSettingsButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				if (!settingsOnScreen) {
					mapLayout.addView(mapSettingsView);
					setSaveButtonOnClickListener();
					mapTypeSpinner = (Spinner) findViewById(R.id.map_type_spinner);
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(
							getApplicationContext(), R.layout.spinner_item,
							R.id.spinnertextview, getResources()
									.getStringArray(R.array.map_types_array));
					mapTypeSpinner.setAdapter(adapter);
					settingsOnScreen = true;
				} else {
					mapLayout.removeView(mapSettingsView);
					settingsOnScreen = false;
				}

			}

		});

	}

	/**
	 * Sets the on click listener for the save button
	 */
	private void setSaveButtonOnClickListener() {
		mapSettingsSaveButton = (Button) findViewById(R.id.map_settings_save_button);

		mapSettingsSaveButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				if (settingsOnScreen) {
					String mapTypeSelected = mapTypeSpinner.getSelectedItem()
							.toString();

					if (mapTypeSelected.equals(getString(R.string.normal))) {
						map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
					} else if (mapTypeSelected
							.equals(getString(R.string.satellite))) {
						map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
					} else if (mapTypeSelected
							.equals(getString(R.string.terrain))) {
						map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
					} else if (mapTypeSelected
							.equals(getString(R.string.hybrid))) {
						map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
					}
					Editor preferencesEditor = getPreferences(MODE_PRIVATE)
							.edit();
					preferencesEditor.putString(MAP_TYPE_PREFERENCE_KEY,
							mapTypeSelected).commit();
					settingsOnScreen = false;
					mapLayout.removeView(mapSettingsView);
				}

			}

		});

	}

	@Override
	protected void onPause() {
		super.onPause();

	}

	@Override
	protected void onStop() {
		super.onStop();

	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			updateMap();
		}
		addMarkersForPreviousSightings();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map_, menu);
		return true;
	}

}
