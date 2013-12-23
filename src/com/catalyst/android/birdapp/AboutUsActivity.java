package com.catalyst.android.birdapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

public class AboutUsActivity extends Activity {
	
	private TableLayout developerLayout;
	private TextView developerNameText;
	private Button contactButton;
	private ImageView logo;
	
	private String[] developerArray;
	private String[] testerArray;
	private String[] scrumArray;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_us);
		contactButton = (Button) findViewById(R.id.contactUsButton);
		logo = (ImageView) findViewById(R.id.companyLogo);
		
		developerLayout = (TableLayout) findViewById(R.id.devListTable);
		developerArray = getResources().getStringArray(R.array.aboutUsDevelopers);
		testerArray = getResources().getStringArray(R.array.aboutUsTesters);
		scrumArray = getResources().getStringArray(R.array.aboutUsScrumMasters);
		insertDataInTable();
		
		contactButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				contactButtonClick();
			}
		});
		
		logo.setOnClickListener(new View.OnClickListener(){
		    public void onClick(View v){
		    	logoClick();
		    }
		});
	}
	
	/**
	 * When the contact us button is clicked, open the email program,
	 * filling in the to and subject fields
	 */
	private void contactButtonClick() {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("plain/text");
		intent.putExtra(Intent.EXTRA_EMAIL,   new String[] { getString(R.string.aboutUsContactEmail) });
		intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.aboutUsContactSubject) );
		startActivity(Intent.createChooser(intent, ""));
	}
	
	/**
	 * When the logo is clicked, go to the company website
	 */
	private void logoClick() {
		Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse(getString(R.string.aboutUsAddressUrl)));
        startActivity(intent);
	}
	
	/**
	 * Read the list of names and create the table of names
	 */
	private void insertDataInTable() {
		int i = 0;
		//insert company blurbs
		insertDataInScrollView(getString(R.string.aboutUsBlurb1), i++);
		insertDataInScrollView("", i++);
		insertDataInScrollView(getString(R.string.aboutUsBlurb2), i++);
		//insert developers
		insertHeadingInScrollView(getString(R.string.aboutUsDevelopersHeading), i++);
		for (String developer : developerArray) {
			insertDataInScrollView(developer, i++);
		}
		//insert testers
		insertHeadingInScrollView(getString(R.string.aboutUsTestersHeading), i++);
		for (String developer : testerArray) {
			insertDataInScrollView(developer, i++);
		}
		//insert scrum masters
		insertHeadingInScrollView(getString(R.string.aboutUsScrumMastersHeading), i++);
		for (String developer : scrumArray) {
			insertDataInScrollView(developer, i++);
		}
	}
	
	/**
	 * Populate the data in each row of the table
	 */
	private void insertDataInScrollView(String devName, int arrayIndex) {
		//Get the layout inflater service
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//Use the inflater to inflate a row 
		View newRow = inflater.inflate(R.layout.activity_about_us_row, null);
		//Create the text view fields for the scroll view row
		developerNameText = (TextView) newRow.findViewById(R.id.developerNameText);
		//Add the name to the text view fields
		developerNameText.setText(devName);
		//Add the new components for the sighting to the table layout
		developerLayout.addView(newRow, arrayIndex);
	}
	
	/**
	 * Insert a heading row in the table
	 */
	private void insertHeadingInScrollView(String devName, int arrayIndex) {
		//Get the layout inflater service
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//Use the inflater to inflate a row 
		View newRow = inflater.inflate(R.layout.activity_about_us_row, null);
		//Create the text view fields for the scroll view row
		developerNameText = (TextView) newRow.findViewById(R.id.developerNameText);
		//Add the name to the text view fields
		developerNameText.setText(devName);
		developerNameText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
		developerNameText.setPadding(0, 16, 16, 16);
		//Add the new components for the sighting to the table layout
		developerLayout.addView(newRow, arrayIndex);
	}

}
