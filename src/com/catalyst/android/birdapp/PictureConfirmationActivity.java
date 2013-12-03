package com.catalyst.android.birdapp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import com.catalyst.android.birdapp.database.DatabaseHandler;
import com.catalyst.android.birdapp.entities.BirdSighting;

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

        @Override
        public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                Intent i = getIntent();
                setContentView(R.layout.activity_picture_confirmation);
                imageUri = i.getStringExtra("fileName");

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