package com.catalyst.android.birdapp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.catalyst.android.birdapp.GPS_Utility.GPSUtility;
import com.catalyst.android.birdapp.database.DatabaseHandler;
import com.catalyst.android.birdapp.utilities.AlertDialogFragment;
import com.catalyst.android.birdapp.utilities.OnDialogDoneListener;
import com.catalyst.android.birdapp.utilities.FormValidationUtilities;
import com.catalyst.android.birdapp.utilities.Utilities;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class BirdFormActivity extends Activity implements OnDialogDoneListener {

	public static final String LOGTAG = "DialogFrag";

	private Spinner categorySpinner;
	private Spinner activitySpinner;
	private CheckBox autoGPS;
	private GPSUtility gpsUtility;
	private EditText latitudeEditText, longitudeEditText;

	private EditText commonNameEditText;
	private EditText scientificNameEditText;
	private EditText notesEditText;
	private EditText dateEditText;
	private EditText timeEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.activity_bird_form);
		// Checks to see if the device has google play services.
		GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(getApplicationContext());
		// Sets up the GPS Utility class
		gpsUtility = new GPSUtility(this);
		categorySpinner = (Spinner) findViewById(R.id.category_drop_down);
		activitySpinner = (Spinner) findViewById(R.id.bird_acivity_dropdown);
		notesEditText = (EditText) findViewById(R.id.notes_edit_text);
		notesEditText.setMovementMethod(ScrollingMovementMethod.getInstance());
		displayDateAndTime();

		// Grabs the fields needed for gps autofill
		intializeGPSfields();

		commonNameEditText = (EditText) findViewById(R.id.common_name_edit_text);
		scientificNameEditText = (EditText) findViewById(R.id.scientific_name_edit_text);
		dateEditText = (EditText) findViewById(R.id.date_time_edit_text);
		timeEditText = (EditText) findViewById(R.id.hour_edit_text);
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (autoGPS.isChecked()) {
			// Removes the locationListener if the Auto Fill check box is
			// unchecked
			gpsUtility.removeFormLocationUpdates();
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (autoGPS.isChecked()) {
			// Removes the locationListener if the Auto Fill check box is
			// unchecked
			gpsUtility.removeFormLocationUpdates();
		}
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (autoGPS.isChecked()) {
			gpsUtility.checkForGPS();
			Location location = gpsUtility.getCurrentLocation();
			// Auto fills the form
			try {
				latitudeEditText
						.setText(Double.toString(location.getLatitude()));
				longitudeEditText.setText(Double.toString(location
						.getLongitude()));
			} catch (NullPointerException e) {

			}
			// resets the location listener
			gpsUtility.setFormLocationListener();
		}
		displayDateAndTime();
		fillActivitySpinner();
		fillCategorySpinner();
	}

	private void fillActivitySpinner() {
		DatabaseHandler dbHandler = DatabaseHandler.getInstance(this);
		ArrayList<String> activitiesFromDB = dbHandler.getAllActivities();
		ArrayAdapter adapter = new ArrayAdapter(this, R.layout.spinner_item,
				R.id.spinnertextview, activitiesFromDB);
		activitySpinner.setAdapter(adapter);

	}

	private void fillCategorySpinner() {
		DatabaseHandler dbHandler = DatabaseHandler.getInstance(this);
		ArrayList<String> categoriesFromDB = dbHandler.getAllCategories();
		ArrayAdapter adapter = new ArrayAdapter(this, R.layout.spinner_item,
				R.id.spinnertextview, categoriesFromDB);
		categorySpinner.setAdapter(adapter);

	}

	private void intializeGPSfields() {
		// Grabs the edit texts fields from the page so that they can be edited
		latitudeEditText = (EditText) findViewById(R.id.latitude_edit_text);
		longitudeEditText = (EditText) findViewById(R.id.longitude_edit_text);
		// Grabs the coordinate autofill checkbox from the page and sets the
		// OnCheckChangeListener
		autoGPS = (CheckBox) findViewById(R.id.autofill_checkbox);
		autoGPS.setOnCheckedChangeListener(autoGPSListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bird_form, menu);
		return true;
	}

	/**
	 * Takes the user to the google map
	 */
	public void getMap(MenuItem item) {
		// Takes the user to the Map Activity
		Intent intent = new Intent(getApplication(), MapActivity.class);
		startActivity(intent);
	}

	private void autoFillCoordinates() {
		Location location = gpsUtility.getCurrentLocation();
		// Auto fills the form
		if (location != null) {
			latitudeEditText.setText(Double.toString(location.getLatitude()));
			longitudeEditText.setText(Double.toString(location.getLongitude()));
		}
		// Checks if there are any coordinates in the edit text boxes. If they
		// are empty then the coordinates are unavailable
		if (latitudeEditText.getText().length() == 0
				|| longitudeEditText.getText().length() == 0) {
			gpsUtility.noLocationAvailable();
			latitudeEditText
					.setText(getString(R.string.coordinates_not_available));
			longitudeEditText
					.setText(getString(R.string.coordinates_not_available));
		} else {
			// If everything is good, this sets the location listener
			gpsUtility.setFormLocationListener();
		}
	}

	// OnCheckedChangeListener for the Coordinate autofill checkbox.
	private OnCheckedChangeListener autoGPSListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (autoGPS.isChecked()) {
				gpsUtility.checkForGPS();
				autoFillCoordinates();
			} else {
				// If the user is unchecking the box, the latitude and longitude
				// boxes are cleared, and the location listener is removed.
				gpsUtility.removeFormLocationUpdates();
				latitudeEditText.setText("");
				longitudeEditText.setText("");
			}

		}

	};

	/**
	 * Auto populates the date and time fields of the form
	 */
	private void displayDateAndTime() {
		Utilities util = new Utilities();
		TextView date = (TextView) findViewById(R.id.date_time_edit_text);
		TextView time = (TextView) findViewById(R.id.hour_edit_text);
		date.setText(util.formatDate(util.currentMillis()));
		time.setText(util.formatTime(util.currentMillis()));
	}

	public void submitBirdSighting() {
		int errors = 0;
		BirdSighting birdSighting = new BirdSighting();
		FormValidationUtilities fvd = new FormValidationUtilities();
		String commonNameField = commonNameEditText.getText().toString();
		// If user has provided input validate for proper content
		if (!commonNameField.isEmpty()) {
			if (!fvd.isFieldValueFormattedAlphaOnly(commonNameField)) {
				commonNameEditText
						.setError("Bird name must be alpha characters only!");
				errors++;
			}
		}
		String scientificNameField = scientificNameEditText.getText()
				.toString();
		// If user has provided input validate for proper content
		if (!scientificNameField.isEmpty()) {
			if (!fvd.isFieldValueFormattedAlphaOnly(scientificNameField)) {
				scientificNameEditText
						.setError("Scientific name must be alpha characters only!");
				errors++;
			}
		}
		if (errors == 0) {
			String longitudeField = longitudeEditText.getText().toString();
			String latitudeField = latitudeEditText.getText().toString();
			String notesField = notesEditText.getText().toString();
			String categoryField = categorySpinner.getSelectedItem().toString();
			String activityField = activitySpinner.getSelectedItem().toString();
			String dateField = dateEditText.getText().toString();
			String timeField = timeEditText.getText().toString();

			// create Date object from date/time fields
			String dateTimeString = dateField + " " + timeField;
			Utilities util = new Utilities();
			Date dateTime = util.getDateObject(dateTimeString);

			// Set values in BirdSighting object
			birdSighting.setCommonName(commonNameField);
			birdSighting.setScientificName(scientificNameField);
			birdSighting.setNotes(notesField);
			birdSighting.setActivity(activityField);
			birdSighting.setCategory(categoryField);
			birdSighting.setDateTime(dateTime);

			// Check formatting, set field to null if wrong format
			try {
				birdSighting.setLatitude(Double.parseDouble(latitudeField));
			} catch (NumberFormatException e) {
				birdSighting.setLatitude(null);
			}
			try {
				birdSighting.setLongitude(Double.parseDouble(longitudeField));
			} catch (NumberFormatException e) {
				birdSighting.setLongitude(null);
			}
			DatabaseHandler dbHandler = DatabaseHandler.getInstance(this);
			dbHandler.insertBirdSighting(birdSighting);
			// Presents a toast with the bird name, or a generic toast if no
			// bird name is input
			if (commonNameField.equals("")) {
				Toast.makeText(this,
						getString(R.string.sightingAddedBlankName),
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(
						this,
						getString(R.string.added_bird_sighting_toast1)
								+ " "
								+ commonNameField
								+ " "
								+ getString(R.string.added_bird_sighting_toast2),
						Toast.LENGTH_SHORT).show();
			}
			refreshActivity();
		}
	}

	/**
	 * pulls up AddNewActivity view
	 */
	public void getAddNewActivity(MenuItem menuItem) {
		Intent intent = new Intent(getApplication(), AddNewActivity.class);
		startActivity(intent);
	}

	/**
	 * Refreshes the current activity
	 */
	private void refreshActivity() {
		Intent i = getIntent();
		finish();
		startActivity(i);
	}

	/**
	 * Called when user taps 'Submit' button. Checks for missing user defined
	 * fields and alerts user if blank
	 * 
	 * @param view
	 */
	public void checkFieldsAndNotifyUserOfBlanksBeforeSubmission(View view) {
		List<String> userDefinedFields = new ArrayList<String>();
		List<String> missingFieldTitles = new ArrayList<String>();

		userDefinedFields.add(commonNameEditText.getText().toString());
		userDefinedFields.add(scientificNameEditText.getText().toString());
		userDefinedFields.add(notesEditText.getText().toString());
		FormValidationUtilities fvd = new FormValidationUtilities();
		missingFieldTitles = fvd.validateBirdFormFields(userDefinedFields);
		if (missingFieldTitles.size() > 0) {
			submitAlertDialog(missingFieldTitles);
		} else {
			submitBirdSighting();
		}
	}

	/**
	 * Launches the confirmation dialog
	 */
	@SuppressLint("CommitTransaction")
	public void submitAlertDialog(List<String> missingFieldTitles) {
		StringBuilder sb = new StringBuilder();
		String sep = ", ";
		for (String s : missingFieldTitles) {
			sb.append(sep).append(s);
		}
		String missFields = sb.toString();
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		AlertDialogFragment adf = AlertDialogFragment
				.newInstance(getString(R.string.emptyFieldsWarning)
						+ missFields + "?");
		adf.show(ft, missFields);
	}

	/**
	 * Listens for what the user chose and initiates callback method if the
	 * response was positive.
	 */
	public void onDialogDone(String tag, boolean cancelled, CharSequence message) {
		if (!cancelled) {
			submitBirdSighting();
		}
	}
}
