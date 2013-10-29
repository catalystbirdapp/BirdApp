package com.catalyst.android.birdapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.catalyst.android.birdapp.camera.CameraPreview;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
<<<<<<< HEAD
import android.view.View.OnClickListener;
=======
import android.widget.ArrayAdapter;
>>>>>>> origin/master
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;


public class CameraActivity extends Activity {

	private Camera mCamera;
<<<<<<< HEAD
	private CameraPreview mCameraPreview;

	public static int count = 0;
	int TAKE_PHOTO_CODE = 0;
	private Bitmap mPhoto;
	static EditText bird;

	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera_layout);

		mCamera = getCameraInstance();
		mCameraPreview = new CameraPreview(this, mCamera);
		FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
		preview.addView(mCameraPreview); // calls CameraPreview class which
											// starts the preview(aka the camera
											// display)
		RelativeLayout relativeLayoutControls = (RelativeLayout) findViewById(R.id.controls_layout);
		relativeLayoutControls.bringToFront(); // used to bring the capture
												// button the front so that it
												// overlays the preview display

		Button captureButton = (Button) findViewById(R.id.button_capture);
		captureButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mCamera.takePicture(null, null, mPicture);
			}
		});
	}

	PictureCallback mPicture = new PictureCallback() {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			File pictureFile = getOutputMediaFile();
			if (pictureFile == null) {
				return;
			}
			try {
				FileOutputStream fos = new FileOutputStream(pictureFile);
				fos.write(data);
				fos.close();
			} catch (FileNotFoundException e) {

			} catch (IOException e) {

			}
		}
	};

	private File getOutputMediaFile() {

		File mediaStorageDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
						+ "/birdAppPictures/");
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d("birdAppPictures", "failed to create directory");
				return null;
			}
		}
		bird = (EditText) findViewById(R.id.common_name_edit_text);
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		File mediaFile;
		mediaFile = new File(mediaStorageDir.getPath() + bird.toString()
				+ timeStamp + ".jpg");
		return mediaFile;

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == TAKE_PHOTO_CODE && resultCode == RESULT_OK) {
			Log.d("Camera", "Pic saved");
		}
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
=======
    private CameraPreview mCameraPreview;
    private FrameLayout preview;
    private boolean click = true;
    private RelativeLayout relativeLayoutControls;
    private View view;
    private Spinner zoomSpinner;
    private Spinner resolutionSpinner;
    private Spinner pictureSizeSpinner;
    private Spinner whiteBalanceSpinner;
    private Button saveButton;
    
    /** Called when the activity is first created. */
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_layout);
        mCamera = getCameraInstance();
        mCameraPreview = new CameraPreview(this, mCamera);
        preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mCameraPreview); //calls CameraPreview class which starts the preview(aka the camera display)
        relativeLayoutControls = (RelativeLayout) findViewById(R.id.controls_layout);
        relativeLayoutControls.bringToFront(); //used to bring the capture button the front so that it overlays the preview display
        Button captureButton = (Button) findViewById(R.id.button_capture);
        ImageButton settingsButton = (ImageButton) findViewById(R.id.settings_button);  
        view = getLayoutInflater().inflate(R.layout.activity_camera_settings, null);

        settingsButton.setOnClickListener(new View.OnClickListener() { //sets on click listener for settings button

			@Override
			public void onClick(View v) {
				 //on click adds layout to preview
				if(click){
					preview.addView(view);
					setSaveButton(); 
					//populates all the spinners for the menu
					populateZoomSpinner();
					populateResolutionSpinner();
					populatePictureSizeSpinner();
					populateWhiteBalanceSpinner();
					click = false;
				}else{
					preview.removeView(view); //removes preview on click and resumes camera preview
                    click = true;
				}
			}
        	
        });
        //sets on click listener for capture button
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //TODO implement saving photo on click here
            }
        });
    }

    /**
     * sets the save button on click listener on the dynamic settings view
     */
    
    public void setSaveButton(){
    	//TODO save functionality will go here
    	saveButton = (Button)findViewById(R.id.save_button);
		saveButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!click){
					preview.removeView(view);
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

>>>>>>> origin/master
}
