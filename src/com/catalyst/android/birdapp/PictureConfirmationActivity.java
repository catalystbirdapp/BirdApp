package com.catalyst.android.birdapp;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class PictureConfirmationActivity extends Activity {
        private String imageUri;

        @Override
        public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                requestWindowFeature(Window.FEATURE_NO_TITLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
                Intent i = getIntent();
                setContentView(R.layout.activity_picture_confirmation);
                imageUri = i.getStringExtra("fileName");

                File imgFile = new  File(imageUri);
				if(imgFile.exists()){
					//Checks the size of the photo
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inJustDecodeBounds = true;
					BitmapFactory.decodeFile(imgFile.getAbsolutePath(), options);
					float srcHeight = options.outHeight;
					//Gets the screen size
					Display display = getWindowManager().getDefaultDisplay();
					Point size = new Point();
					display.getSize(size);
					int width = size.x;
					int height = size.y;
					//Sets the sample size so that the app can load a sampled down version of the photo to conserve memory
					int sampleSize = Math.round(srcHeight / height);
					options = new BitmapFactory.Options();
					options.inSampleSize = sampleSize;
					//Gets the image and places it in the map info window at a scaled size for conformity
				    Bitmap bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(imgFile.getAbsolutePath(), options), width, height, false);
				    ImageView img = (ImageView) findViewById(R.id.image);
	                img.setImageBitmap(bitmap);
				}           
        }

        // Deletes image from phone.
        public void deleteImage(View view) {

                File file = new File(imageUri).getAbsoluteFile();
                if (file.exists()) {
                    file.delete();
                }
                if (!file.exists()) {
                	setResult(Activity.RESULT_CANCELED);
                	finish();
                }

        }

        // Saves image to Database.
        public void saveImage(View view) {
            Intent intent = new Intent();
            intent.putExtra("fileName", imageUri);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
}