package com.catalyst.android.birdapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import com.catalyst.android.birdapp.camera.CameraPreview;
import com.catalyst.android.birdapp.utilities.CameraUtilities;
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
	private CameraUtilities cameraUtilities;
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
	private List<String> zoomLevel;
	private List<String> supportedWhiteBalance;
	private List<String>resolutions;
	private List<String>previewSizes;
    private Parameters parameters;
   
	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera_layout);
		mCamera = getCameraInstance();
		cameraUtilities = new CameraUtilities();
		mCameraPreview = new CameraPreview(this, mCamera);
		parameters = mCamera.getParameters();
		preview = (FrameLayout) findViewById(R.id.camera_preview);
		preview.addView(mCameraPreview); 
		relativeLayoutControls = (RelativeLayout) findViewById(R.id.controls_layout);
		relativeLayoutControls.bringToFront(); 
		captureButton = (Button) findViewById(R.id.button_capture);
		settingsButton = (ImageButton) findViewById(R.id.settings_button);
		view = getLayoutInflater().inflate(R.layout.activity_camera_settings,
				null);
		buttonView = getLayoutInflater().inflate(R.layout.camera_settings_button, null);
		setCameraSettings();
		
		
		
		settingsButton.setOnClickListener(new View.OnClickListener() { 

					 @Override
                        public void onClick(View v) {
                                 //on click adds layout to preview
                                if(click){
                                	
                                		captureButton.setVisibility(View.GONE);
                                        preview.addView(view);
                                        preview.addView(buttonView);
                                        setSaveButton();
                                        setSettingsButton();
                                        zoomLevel = cameraUtilities.getSupportedCameraZoom(parameters);
                                        supportedWhiteBalance = cameraUtilities.getSupportedWhiteBalanceSettings(parameters);
                                        resolutions = cameraUtilities.getSupportedCameraResolution(parameters);
                                        previewSizes = cameraUtilities.getSupportedPreviewSize(parameters);
                                        populateZoomSpinner();
                                        populateResolutionSpinner();
                                        populatePictureSizeSpinner();
                                        populateWhiteBalanceSpinner();
                                        populatePreferences();
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
				String resolution = resolutionSpinner.getSelectedItem().toString();
				String previewSize = pictureSizeSpinner.getSelectedItem().toString();
				String whiteBalance = whiteBalanceSpinner.getSelectedItem().toString();
				String [] sizes = cameraUtilities.getResolutionAndPreviewSize(resolution, previewSize);
			
				//sets the new camera settings to shared preferences
				Editor preferencesEditor = getPreferences(MODE_PRIVATE).edit();
				preferencesEditor.putString("ZoomPreference", zoom)
						.putString("ResolutionPreference", resolution)
						.putString("PictureSizePreference", previewSize)
						.putString("ResolutionPreferenceHeight", sizes[0])
						.putString("ResolutionPreferenceWidth", sizes[1])
						.putString("PictureSizePreferenceHeight", sizes[2])
						.putString("PictureSizePreferenceWidth", sizes[3])
						.putString("WhiteBalancePreference", whiteBalance)
						.commit();
				
				setCameraSettings(); //sets the new camera settings to the camera parameters
				
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

	


	/**
	 * Gets the stored preferences for camera settings and applies them to the camera view and the settings page when the
	 * camera is opened. 
	 */
	
	@SuppressWarnings("unchecked")
	public void populatePreferences() {
		SharedPreferences preferences = getPreferences(MODE_PRIVATE);
		String zoomPreference = preferences.getString("ZoomPreference",DEFAULT_VALUE);
		String resolutionPreference = preferences.getString("ResolutionPreference", DEFAULT_VALUE);
		String pictureSizePreference = preferences.getString("PictureSizePreference", DEFAULT_VALUE);
		String whiteBalancePreference = preferences.getString("WhiteBalancePreference", DEFAULT_VALUE);

		ArrayAdapter<String> zoomAdapter = (ArrayAdapter<String>) zoomSpinner.getAdapter();
		ArrayAdapter<String> resolutionAdapter = (ArrayAdapter<String>) resolutionSpinner.getAdapter();
		ArrayAdapter<String> pictureSizeAdapter = (ArrayAdapter<String>) pictureSizeSpinner.getAdapter();
		ArrayAdapter<String> whiteBalanceAdapter = (ArrayAdapter<String>) whiteBalanceSpinner.getAdapter();

		int zoomPosition = zoomAdapter.getPosition(zoomPreference);
		int resolutionPosition = resolutionAdapter.getPosition(resolutionPreference);
		int pictureSizePosition = pictureSizeAdapter.getPosition(pictureSizePreference);
		int whiteBalancePosition = whiteBalanceAdapter.getPosition(whiteBalancePreference);

		zoomSpinner.setSelection(zoomPosition);
		resolutionSpinner.setSelection(resolutionPosition);
		pictureSizeSpinner.setSelection(pictureSizePosition);
		whiteBalanceSpinner.setSelection(whiteBalancePosition);

	}

		
	/**
	 * Takes the new camera settings and sets them to the camera parameters.
	 * 
	 */
	public void setCameraSettings(){
		SharedPreferences preferences =	getSharedPreferences("CameraActivity", Context.MODE_PRIVATE);
		
		String savedZoom = preferences.getString("ZoomPreference", "1");
		supportedWhiteBalance = new ArrayList<String>();
		zoomLevel = new ArrayList<String>();
		//Checks to see if the camera supports zoom and smooth zoom
		int zoomNumber = cameraUtilities.getCameraZoom(parameters, savedZoom);
		
		
			//gets the whitebalance preference and sets the camera parameters to that preference
			String whiteBalance = preferences.getString("WhiteBalancePreference", "Auto").toLowerCase(Locale.US);
			int resolutionHeight = Integer.parseInt(preferences.getString("ResolutionPreferenceHeight", "480"));
			int resolutionWidth = Integer.parseInt(preferences.getString("ResolutionPreferenceWidth", "640"));
			int previewSizeHeight = Integer.parseInt(preferences.getString("PictureSizePreferenceHeight", "480"));
			int previewSizeWidth = Integer.parseInt(preferences.getString("PictureSizePreferenceWidth", "640"));
			
			mCamera.stopPreview();
			parameters.setPictureSize(resolutionWidth, resolutionHeight);
			parameters.setPreviewSize(previewSizeWidth, previewSizeHeight);
			parameters.setWhiteBalance(whiteBalance);
			parameters.setZoom(zoomNumber);
			mCamera.startPreview();
			mCamera.setParameters(parameters);
		
			
		}

/**
 * on resume, reset the camera settings
 */

	@Override
	public void onResume(){
		super.onResume();
		setCameraSettings();
	}

/**
 * sets zoom spinner
 */
    public void populateZoomSpinner(){
             zoomSpinner = (Spinner)findViewById(R.id.zoom_spinner);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, R.id.spinnertextview, zoomLevel);
                zoomSpinner.setAdapter(adapter);
            
    }
    /**
     * sets resolution spinner
     */
    public void populateResolutionSpinner(){
             resolutionSpinner = (Spinner)findViewById(R.id.resolution_spinner);
             ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, R.id.spinnertextview, resolutions);
             resolutionSpinner.setAdapter(adapter);
    }
    /**
     * sets picture size spinner
     */
    public void populatePictureSizeSpinner(){
             pictureSizeSpinner = (Spinner)findViewById(R.id.picture_size_spinner);
             ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, R.id.spinnertextview, previewSizes);
             
             pictureSizeSpinner.setAdapter(adapter);
    }

    /**
     * sets white balance spinner
     */
    public void populateWhiteBalanceSpinner(){
            whiteBalanceSpinner = (Spinner)findViewById(R.id.white_balance_spinner);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, R.id.spinnertextview, supportedWhiteBalance);
            whiteBalanceSpinner.setAdapter(adapter);
    }

}