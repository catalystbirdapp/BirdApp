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
import android.widget.ArrayAdapter;

import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;

public class CameraActivity extends Activity {
	private Context context = this;
	private Camera mCamera;
	private CameraPreview mCameraPreview;

	public static int count = 0;
	int TAKE_PHOTO_CODE = 0;
	private Bitmap mPhoto;
	static String bird;

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
			File file = null;
			try {
				String fileName = Uri.parse(data.toString())
						.getLastPathSegment();
				file = File.createTempFile(fileName, ".jpg",
						context.getCacheDir());
			} catch (IOException e) {

			}
			Intent intent = new Intent(CameraActivity.this,
					PictureConfirmationActivity.class);
			String ap = file.getAbsolutePath();
			Uri fileName = Uri.fromFile(file);
			intent.putExtra("fileName", ap);
			startActivity(intent);
		}
	};

	// Creates directory birdAppPictures, if it doesn't exist.

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
}

/**
 * sets the save button on click listener on the dynamic settings view
 */

