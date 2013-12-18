package com.catalyst.android.birdapp;

import static com.catalyst.android.birdapp.constants.ActivityIdentifyingConstants.BIRD_FORM_ACTIVITY;
import static com.catalyst.android.birdapp.constants.ActivityIdentifyingConstants.CALLING_ACTIVITY;
import static com.catalyst.android.birdapp.constants.ActivityIdentifyingConstants.SIGHTING_LIST_ACTIVITY;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableLayout;
import android.widget.TextView;

import com.catalyst.android.birdapp.database.DatabaseHandler;
import com.catalyst.android.birdapp.entities.BirdComparator;
import com.catalyst.android.birdapp.entities.BirdSighting;

public class RecordsActivity extends Activity {
	private static final String BIRD_SIGHTING = "BirdSighting";
	
	private DatabaseHandler dbHandler;
	private List<BirdSighting> allBirdSightings;
	SparseArray<BirdSighting> birdSightingSparseArray;
	
	private TableLayout birdSightingLayout;
	private TextView commonNameText;
	private TextView categoryText;
	private TextView dateRowText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_records);
		//Gets a handle to the database and the bird sighting table on the page
		dbHandler = DatabaseHandler.getInstance(this);
		birdSightingLayout = (TableLayout) findViewById(R.id.birdSightingTableLayout);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		birdSightingLayout.removeAllViews();
		retrieveAllBirdSightings();
        //Adds the bird sightings to the page
        updateBirdSightingList();
	}
	
	/**
	 * Retrieves all bird sighting from the database
	 */
	private void retrieveAllBirdSightings() {
    	//Retrieves all of the bird sightings from the DB
        allBirdSightings = dbHandler.getAllBirdSightings();
        //SparseArray is like a HashMap, but supposedly more efficient with ints
        birdSightingSparseArray = new SparseArray<BirdSighting>();
      
        for (BirdSighting birdSighting : allBirdSightings) {
            //Adds the BirdSighting to the HashMap so that the BirdSighting can be retrieved by passing in the id
            birdSightingSparseArray.put(birdSighting.getId(), birdSighting);
        }
	}
	
	/**
	 * Read the list of bird sightings and create the table of bird sightings
	 */
	private void updateBirdSightingList() {
		Collections.sort(allBirdSightings, new BirdComparator());
		int i = 0;
		for (BirdSighting sighting : allBirdSightings) {
			insertSightingInScrollView(sighting, i);
			i +=2;
		}
	}
	
	/**
	 * Populate the data in each row of the sightings table
	 */
	private void insertSightingInScrollView(BirdSighting sighting, int arrayIndex) {
		//Get the layout inflater service
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//Use the inflater to inflate a sighting row from activity_records_row
		View newSightingRow = inflater.inflate(R.layout.activity_records_row, null);
		//Create the text view fields for the scroll view row
		commonNameText = (TextView) newSightingRow.findViewById(R.id.commonNameText);
		categoryText = (TextView) newSightingRow.findViewById(R.id.categoryText);
		dateRowText = (TextView) newSightingRow.findViewById(R.id.dateRowText);
		//Add the sighting to the text view fields
		commonNameText.setText(sighting.getCommonName());
		categoryText.setText(sighting.getCategory());
		String sightingDate = formatDate(sighting.getDateTime());
		dateRowText.setText(sightingDate);
		//Store the list location in the tag
		newSightingRow.setTag(sighting.getId());
		//Add the new components for the sighting to the table layout
		birdSightingLayout.addView(newSightingRow, arrayIndex++);
		//Add a row separator line to the table layout
		View newLineRow = inflater.inflate(R.layout.activity_records_line_row, null);
		birdSightingLayout.addView(newLineRow, arrayIndex);
		
		newSightingRow.setClickable(true);
		newSightingRow.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				editBirdSightingSelected(arg0);
			}
        });
	}
	
	/**
	 * Formats the date for the sightings list
	 */
	private String formatDate(Date sightingDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy  HH:mm", Locale.US);
		return sdf.format(sightingDate); 
	}
	
	/**
	 * Goes to the edit page for the bird sighting clicked 
	 */
	private void editBirdSightingSelected(View view) {
		Integer holdIndex = (Integer) view.getTag();
		BirdSighting birdSighting = birdSightingSparseArray.get(holdIndex);
		//Checks to see if the bird sighting is null.
		if (birdSighting != null) {
			//Creates the  intent and stores the bird sighting for retrieval in the Edit Form Activity 
			Intent intent = new Intent();
			intent.setClass(RecordsActivity.this, BirdFormActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable(BIRD_SIGHTING, birdSighting);
			bundle.putInt(CALLING_ACTIVITY, SIGHTING_LIST_ACTIVITY);
			intent.putExtras(bundle);
		
			startActivityForResult(intent, BIRD_FORM_ACTIVITY);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.records, menu);
		return true;
	}
	
	/**
	 * Opens the application settings menu option
	 */
	public void openApplicationSettings(MenuItem menuItem) {
		Intent intent = new Intent(RecordsActivity.this, ApplicationSettingsActivity.class);
		startActivity(intent);
	}

}
