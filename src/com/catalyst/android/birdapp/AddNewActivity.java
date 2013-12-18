package com.catalyst.android.birdapp;

import com.catalyst.android.birdapp.database.DatabaseHandler;
import com.catalyst.android.birdapp.utilities.FormValidationUtilities;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class AddNewActivity extends Activity {

	private DatabaseHandler dbHandler = DatabaseHandler.getInstance(this);
	private Button saveButton;
	private CheckBox addAnotherCheckBox;
	private EditText activityName;
	private String activityNameValue;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_new);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		saveButton = (Button)findViewById(R.id.saveButton);
		addAnotherCheckBox = (CheckBox)findViewById(R.id.add_another_button);
		activityName = (EditText)findViewById(R.id.activityName);
		
		saveButton.setOnClickListener(new View.OnClickListener() {
             public void onClick(View view) {
            	 activityNameValue = activityName.getText().toString();
            	 validateActivity();
             }
         });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_new, menu);
		return true;
	}
	
	/**
	 * Saves the inputed activity name to the database
	 */
	public void saveActivity(){
		dbHandler.saveActivity(activityNameValue);
	}
	
	/**
	 * Validates the input in the activity edit text.
	 * Does not let the user enter duplicate activities.
	 * Alpha characters only
	 */
	private void validateActivity() {
		FormValidationUtilities fvu = new FormValidationUtilities();
		if(activityNameValue.length() == 0){
			activityName.setError(getString(R.string.add_activity_empty));
		} else if(!fvu.isFieldValueFormattedAlphaOnly(activityNameValue)){
			activityName.setError(getString(R.string.activity_alpha_error));
		} else if(dbHandler.isDuplicateActivity(activityNameValue)){
			activityName.setError(getString(R.string.activity_already_exists_error));
		} else {
			saveActivity();
			checkBox();
		}
	}

	/**
	 * checks the "add another" checkbox to see if it is selected. If it is then the fields are 
	 * reset otherwise it returns to the bird sighting form.
	 * 
	 * @param view
	 */
	public void checkBox(){
		if(addAnotherCheckBox.isChecked()){
			activityName.setText("");
			addAnotherCheckBox.setChecked(false);
		}else {
			finish();
		}
	}
	
	/**
	 * Opens the application settings menu option
	 */
	public void openApplicationSettings(MenuItem menuItem) {
		Intent intent = new Intent(AddNewActivity.this, ApplicationSettingsActivity.class);
		startActivity(intent);
	}

}
