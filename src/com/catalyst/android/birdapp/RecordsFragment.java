package com.catalyst.android.birdapp;

import static com.catalyst.android.birdapp.constants.ActivityIdentifyingConstants.BIRD_FORM_ACTIVITY;
import static com.catalyst.android.birdapp.constants.ActivityIdentifyingConstants.CALLING_ACTIVITY;
import static com.catalyst.android.birdapp.constants.ActivityIdentifyingConstants.SIGHTING_LIST_ACTIVITY;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.catalyst.android.birdapp.database.DatabaseHandler;
import com.catalyst.android.birdapp.entities.BirdComparator;
import com.catalyst.android.birdapp.entities.BirdSighting;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TableLayout;
import android.widget.TextView;

public class RecordsFragment extends Fragment {
	
private static final String BIRD_SIGHTING = "BirdSighting";
private static final String ID = "ID";
	
	private DatabaseHandler dbHandler;
	private List<BirdSighting> allBirdSightings;
	private HashMap <Integer, BirdSighting> birdSightingsMap;
	
	private TableLayout birdSightingLayout;
	private TextView commonNameText;
	private TextView categoryText;
	private TextView dateRowText;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.activity_records, parent, false);
		//Gets a handle to the database and the bird sighting table on the page
		dbHandler = DatabaseHandler.getInstance(getActivity());
		birdSightingLayout = (TableLayout) view.findViewById(R.id.birdSightingTableLayout);
		return view;
	}
	
	public static RecordsFragment newInstance(long id){
		Bundle args = new Bundle();
		args.putLong(ID, id);
		RecordsFragment recordsFragment = new RecordsFragment();
		recordsFragment.setArguments(args);
		return recordsFragment;
	}
	
	@Override
	public void onResume() {
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
        birdSightingsMap = new HashMap <Integer, BirdSighting>();
      
        for (BirdSighting birdSighting : allBirdSightings) {
            //Adds the birdsighting to the hashmap so that the birdsighting can be retrieved by passing in the id
            birdSightingsMap.put(birdSighting.getId(), birdSighting);
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
		LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy  hh:mm a", Locale.US);
		return sdf.format(sightingDate); 
	}
	
	/**
	 * Goes to the edit page for the bird sighting clicked 
	 */
	private void editBirdSightingSelected(View view) {
		Integer holdIndex = (Integer) view.getTag();
		BirdSighting birdSighting = birdSightingsMap.get(holdIndex);
		
		//Checks to see if the bird sighting is null.
		if(birdSighting != null){
			//Creates the  intent and stores the bird sighting for retrieval in the Edit Form Activity 
			Intent intent = new Intent();
			intent.setClass(getActivity(), BirdFormActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable(BIRD_SIGHTING, birdSighting);
			bundle.putInt(CALLING_ACTIVITY, SIGHTING_LIST_ACTIVITY);
			intent.putExtras(bundle);
		
			startActivityForResult(intent, BIRD_FORM_ACTIVITY);
		}
	}
}
