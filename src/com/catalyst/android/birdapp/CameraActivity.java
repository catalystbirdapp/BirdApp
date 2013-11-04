package com.catalyst.android.birdapp;

import java.util.ArrayList;
import java.util.Locale;
import com.catalyst.android.birdapp.camera.CameraPreview;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;

public class CameraActivity extends Activity {

	private Camera mCamera;
	private CameraPreview mCameraPreview;
	private FrameLayout preview;
	private boolean click = true;
	private RelativeLayout relativeLayoutControls;
	private View view;
	private View buttonView;
	private Spinner zoomSpinner;
	private Spinner resolutionSpinner;
	private Spinner pictureSizeSpinner;
	private Spinner whiteBalanceSpinner;
	private Button saveButton;
	private Button captureButton;
	private ImageButton settingsButton;
    private ImageButton settingsButtonView;
	private String DEFAULT_VALUE = "None";
	private ArrayList<String> zoomLevel;
	private ArrayList<String> supportedWhiteBalance;
	
    
   
	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera_layout);
		mCamera = getCameraInstance();
		mCameraPreview = new CameraPreview(this, mCamera);
		
		getCameraZoom();
		preview = (FrameLayout) findViewById(R.id.camera_preview);
		preview.addView(mCameraPreview); // calls CameraPreview class which
											// starts the preview(aka the camera
											// display)
		relativeLayoutControls = (RelativeLayout) findViewById(R.id.controls_layout);
		relativeLayoutControls.bringToFront(); // used to bring the capture
												// button the front so that it
												// overlays the preview display
		captureButton = (Button) findViewById(R.id.button_capture);
		settingsButton = (ImageButton) findViewById(R.id.settings_button);
		view = getLayoutInflater().inflate(R.layout.activity_camera_settings,
				null);
		buttonView = getLayoutInflater().inflate(R.layout.camera_settings_button, null);

		settingsButton.setOnClickListener(new View.OnClickListener() { // sets
																		// on
																		// click
																		// listener
																		// for
																		// settings
																		// button

					 @Override
                        public void onClick(View v) {
                                 //on click adds layout to preview
                                if(click){
                                		captureButton.setVisibility(View.GONE);
                                        preview.addView(view);
                                        preview.addView(buttonView);
                                        setSaveButton();
                                        setSettingsButton();
                                        //populates all the spinners for the menu
                                        populateZoomSpinner();
                                        populateResolutionSpinner();
                                        populatePictureSizeSpinner();
                                        populateWhiteBalanceSpinner();
                                        click = false;
                                }else{
                                        preview.removeView(view); //removes preview on click and resumes camera preview
                                        preview.removeView(buttonView);
                                        captureButton.setVisibility(View.VISIBLE); //removes capture button on setting screen
                    click = true;
                                }
                        }
                
        });
        //sets on click listener for capture button
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             
            }
        });
    }

    /**
     * sets the save button on click listener on the dynamic settings view
     */
	
	public void setSaveButton() {

		saveButton = (Button) findViewById(R.id.save_button);
		saveButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				//Gets all the choices selected from the all the spinners and saves them to shared preferences
				String zoom = zoomSpinner.getSelectedItem().toString();
				String resolution = resolutionSpinner.getSelectedItem()
						.toString();
				String pictureSize = pictureSizeSpinner.getSelectedItem()
						.toString();
				String whiteBalance = whiteBalanceSpinner.getSelectedItem()
						.toString();

				Editor preferencesEditor = getPreferences(MODE_PRIVATE).edit();
				preferencesEditor.putString("ZoomPreference", zoom)
						.putString("ResolutionPreference", resolution)
						.putString("PictureSizePreference", pictureSize)
						.putString("WhiteBalancePreference", whiteBalance)
						.commit();
				
				setCameraZoom();
				
				if (!click) {
					preview.removeView(view);
					preview.removeView(buttonView);
                    captureButton.setVisibility(View.VISIBLE); //un-hides capture button when setting screen is cleared
                   
					click = true;
				}

			}
		});
	}
    
    /**
     * sets the settings button when the setting screen comes up
     */
    public void setSettingsButton(){
        //TODO save functionality will go here
        settingsButtonView = (ImageButton)findViewById(R.id.camera_settings_button_2);
        settingsButtonView.setOnClickListener(new View.OnClickListener() {
                    
                    @Override
                    public void onClick(View v) {
                            if(!click){
                                    preview.removeView(view);//removes setting screen on click
                                    preview.removeView(buttonView);
                                    captureButton.setVisibility(View.VISIBLE);//un-hides capture button when setting screen is cleared
                click = true;
                            }
                            
                    }
            });
}

    /**
     * Helper method to access the camera returns null if it cannot get the
     * camera or does not exist
     * 
     * @return
     */
    private Camera getCameraInstance() {
        Camera camera = null;
        try {
            camera = Camera.open();
        } catch (Exception e) {
            
        }
        return camera;
    }
	
	public void getSupportedWhiteBalanceSettings(){
		Parameters parameters = mCamera.getParameters();
		supportedWhiteBalance = (ArrayList<String>) parameters.getSupportedWhiteBalance();
		for(int i = 0; i<supportedWhiteBalance.size(); i ++){
			String word = supportedWhiteBalance.get(i).substring(0, 1).toUpperCase(Locale.US);
			String fullWord = word + supportedWhiteBalance.get(i).substring(1, supportedWhiteBalance.get(i).length());
			if(fullWord.contains("-")){
				String[] parts = fullWord.split("-");
				String secondWord = parts[1];
				String firstWord = parts[0];
				String firstLetter = secondWord.substring(0,1).toUpperCase(Locale.US);
				String upperWord = firstLetter + secondWord.substring(1,supportedWhiteBalance.get(i).length());
				fullWord = firstWord + "-"+ upperWord;
				
			}
			supportedWhiteBalance.set(i, fullWord);
		}
		
		
	}
	public void getCameraZoom(){
		Parameters parameters = mCamera.getParameters();
		int maxZoom = parameters.getMaxZoom() + 1;
		int count = 0;
		zoomLevel = new ArrayList<String>();
		
		for(int i = 10; i<=maxZoom; i=i+10){
			count++;
			String test = String.valueOf(count).concat(".0x");
			zoomLevel.add(String.valueOf(test));
		}
		
		
		
	}

	@SuppressWarnings("unchecked")
	public void populatePreferences() {
		SharedPreferences preferences = getPreferences(MODE_PRIVATE);
		String zoomPreference = preferences.getString("ZoomPreference",
				DEFAULT_VALUE);
		String resolutionPreference = preferences.getString(
				"ResolutionPreference", DEFAULT_VALUE);
		String pictureSizePreference = preferences.getString(
				"PictureSizePreference", DEFAULT_VALUE);
		String whiteBalancePreference = preferences.getString(
				"WhiteBalancePreference", DEFAULT_VALUE);

		ArrayAdapter<String> zoomAdapter = (ArrayAdapter<String>) zoomSpinner
				.getAdapter();
		ArrayAdapter<String> resolutionAdapter = (ArrayAdapter<String>) resolutionSpinner
				.getAdapter();
		ArrayAdapter<String> pictureSizeAdapter = (ArrayAdapter<String>) pictureSizeSpinner
				.getAdapter();
		ArrayAdapter<String> whiteBalanceAdapter = (ArrayAdapter<String>) whiteBalanceSpinner
				.getAdapter();

		int zoomPosition = zoomAdapter.getPosition(zoomPreference);
		int resolutionPosition = resolutionAdapter
				.getPosition(resolutionPreference);
		int pictureSizePosition = pictureSizeAdapter
				.getPosition(pictureSizePreference);
		int whiteBalancePosition = whiteBalanceAdapter
				.getPosition(whiteBalancePreference);

		zoomSpinner.setSelection(zoomPosition);
		resolutionSpinner.setSelection(resolutionPosition);
		pictureSizeSpinner.setSelection(pictureSizePosition);
		whiteBalanceSpinner.setSelection(whiteBalancePosition);

		setPreferences();
	}

	public void setPreferences() {

	}

		
	/**
	 * Sets the zoom of the camera when the user hits the save button in the 
	 * settings menu. 
	 */
	public void setCameraZoom(){
		SharedPreferences preferences =	getSharedPreferences("CameraActivity", Context.MODE_PRIVATE);
		Parameters parameters = mCamera.getParameters();
		int zoomNumber = 0;
		supportedWhiteBalance = new ArrayList<String>();
		//Checks to see if the camera supports zoom and smooth zoom
		if(parameters.isZoomSupported()){
			String savedZoom = preferences.getString("ZoomPreference", "1");
			String zoomLevel = savedZoom.substring(0, 1);
			zoomNumber = (Integer.parseInt(zoomLevel) * 10);
			/*
			 * Too avoid having the app crash, if user selects the max
			 * zoom size from the drop down list, we set zoom to getMaxZoom.
			 */
			if(zoomNumber ==  (parameters.getMaxZoom() + 1)){
				zoomNumber = (parameters.getMaxZoom());
			
			}
		}
		
			String whiteBalance = preferences.getString("WhiteBalancePreference", "Daylight").toLowerCase(Locale.US);
			parameters.setWhiteBalance(whiteBalance);
			
//			if(whiteBalance.equals("Auto")){
//				parameters.setWhiteBalance("auto");
//			}
//			else if(whiteBalance.equals("Daylight")){
//				parameters.setWhiteBalance("daylight");
//			}
//			else if(whiteBalance.equals("Cloudy-daylight")){
//				
//				parameters.setWhiteBalance("cloudy-daylight");
//			}
//			else if(whiteBalance.equals("Fluorescent")){
//				parameters.setWhiteBalance("fluorescent");
//			}
//			else if(whiteBalance.equals("Warm-Fluorescent")){
//				parameters.setWhiteBalance("warm-fluorescent");
//			}
//			else if(whiteBalance.equals("Incadescent")){
//				parameters.setWhiteBalance("incadescent");
//			}
//			else if(whiteBalance.equals("Twilight")){
//				parameters.setWhiteBalance("twilight");
//			}
//			else if(whiteBalance.equals("Shade")){
//				parameters.setWhiteBalance("shade");
//			}
		
			
			//sets the zoom of the camera
			parameters.setZoom(zoomNumber);
			mCamera.setParameters(parameters);
		}
	
	
	public void setCameraResolution(){
		
	}
	
	public void setPictureSize(){
		
	}
	
	public void setWhiteBalance(){
		
		SharedPreferences preferences =	getSharedPreferences("CameraActivity", Context.MODE_PRIVATE);
		Parameters parameters = mCamera.getParameters();
		
		
	}
	
	@Override
	public void onResume(){
		super.onResume();
		
	}

/**
 * sets zoom spinner
 */
    public void populateZoomSpinner(){
             zoomSpinner = (Spinner)findViewById(R.id.zoom_spinner);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, R.id.spinnertextview, getResources().getStringArray(R.array.zoom_settings));
                zoomSpinner.setAdapter(adapter);
            
    }
    /**
     * sets resolution spinner
     */
    public void populateResolutionSpinner(){
             resolutionSpinner = (Spinner)findViewById(R.id.resolution_spinner);
             ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, R.id.spinnertextview, getResources().getStringArray(R.array.resolution_settings));
             resolutionSpinner.setAdapter(adapter);
    }
    /**
     * sets picture size spinner
     */
    public void populatePictureSizeSpinner(){
             pictureSizeSpinner = (Spinner)findViewById(R.id.picture_size_spinner);
             ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, R.id.spinnertextview, getResources().getStringArray(R.array.picture_size_settings));
             pictureSizeSpinner.setAdapter(adapter);
    }

    /**
     * sets white balance spinner
     */
    public void populateWhiteBalanceSpinner(){
            whiteBalanceSpinner = (Spinner)findViewById(R.id.white_balance_spinner);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, R.id.spinnertextview, getResources().getStringArray(R.array.whitebalance_settings));
            whiteBalanceSpinner.setAdapter(adapter);
    }

}