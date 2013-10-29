package com.catalyst.android.birdapp;

import java.io.File;
import java.io.IOException;

import com.catalyst.android.birdapp.camera.CameraPreview;
import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public class CameraActivity extends Activity {

	private Camera mCamera;
	private CameraPreview mCameraPreview;
	String bird = "Bird";
	public static int count = 0;
	int TAKE_PHOTO_CODE = 0;

	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera_layout);
		final String dir = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
				+ "/birdAppPictures/";
		File newdir = new File(dir);
		newdir.mkdirs();
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
				// TODO implement saving photo on click here
				count++;
				String file = dir + bird + count + ".jpg";
				File newFile = new File(file);
				try {
					newFile.createNewFile();
				} catch (IOException e) {
				}
				Uri outputFileUri = Uri.fromFile(newFile);
				Intent cameraIntent = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);
				cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
				startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);

			}
		});
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

}
