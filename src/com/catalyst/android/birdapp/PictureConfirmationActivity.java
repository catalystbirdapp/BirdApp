package com.catalyst.android.birdapp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import com.catalyst.android.birdapp.database.DatabaseHandler;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;

public class PictureConfirmationActivity extends Activity {
	private String imageUri;
	private String birdName;
	private Bundle bundle;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent i = getIntent();
		setContentView(R.layout.activity_picture_confirmation);
		bundle = i.getExtras();
		imageUri = bundle.get("fileName").toString();
		birdName = bundle.get("birdName").toString();

		File image = new File(imageUri).getAbsoluteFile();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(image);
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
		Bitmap bmp = BitmapFactory.decodeStream(fis);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		ImageView img = (ImageView) findViewById(R.id.image);
		img.setImageBitmap(bmp);
	}

	// Deletes image from phone.
	public void deleteImage(View view) {

		File file = new File(imageUri).getAbsoluteFile();
		if (file.exists()) {
			file.delete();
		}
		if (!file.exists()) {
			Intent intent = new Intent(PictureConfirmationActivity.this,
					CameraActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
		}

	}

	// Saves image to Database.
	public void saveImage(View view) {
		DatabaseHandler db = DatabaseHandler.getInstance(this);
		Intent intent = new Intent(PictureConfirmationActivity.this,
				BirdFormActivity.class);
		bundle.putString("fileName", imageUri);
		intent.putExtras(bundle);
		startActivity(intent);

	}
}
