package com.catalyst.android.birdapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

public class PictureConfirmationActivity extends Activity {
	private String imageUri;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent i = getIntent();
		setContentView(R.layout.activity_picture_confirmation);
		Bundle bundle = i.getExtras();
		imageUri = bundle.get("fileName").toString();
		/*
		 * File location = new File(Environment.getDownloadCacheDirectory()
		 * .getAbsolutePath() + "/images"); File dest = new File(location,
		 * imageUri + ".JPG");
		 */
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(imageUri);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Bitmap bmp = BitmapFactory.decodeStream(fis);
		String string = "bobcat";
	}
}
