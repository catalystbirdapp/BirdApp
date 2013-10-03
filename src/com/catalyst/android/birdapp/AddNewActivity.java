package com.catalyst.android.birdapp;

import com.catalyst.android.birdapp.database.DatabaseHandler;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
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
		saveButton = (Button)findViewById(R.id.saveButton);
		addAnotherCheckBox = (CheckBox)findViewById(R.id.add_another_button);
		activityName = (EditText)findViewById(R.id.activityName);
		
		saveButton.setOnClickListener(new View.OnClickListener() {
             public void onClick(View view) {
            	 saveActivity();
                 checkBox(view);
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
		activityNameValue = activityName.getText().toString();
		dbHandler.saveActivity(activityNameValue);
	}
	/**
	 * checks the "add another" checkbox to see if it is selected. If it is then the fields are 
	 * reset otherwise it returns to the bird sighting form.
	 * 
	 * @param view
	 */
	public void checkBox(View view){
		if(addAnotherCheckBox.isChecked()){
			activityName.setText("");
			addAnotherCheckBox.setChecked(false);
		}else {
			finish();
		}
	}

}
