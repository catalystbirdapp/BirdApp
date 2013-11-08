package com.catalyst.android.birdapp;

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
                File image = new File(imageUri).getAbsoluteFile();
                FileInputStream fis = null;
                try {
                        fis = new FileInputStream(image);
                } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }
                Bitmap bmp = BitmapFactory.decodeStream(fis);
                ImageView img = (ImageView) findViewById(R.id.image);
                img.setImageBitmap(bmp);
        }

        public void deleteImage(View view) {

                File file = new File(imageUri).getAbsoluteFile();
                if (file.exists()) {
                        file.delete();
                }
                if (!file.exists()) {
                        Intent intent = new Intent(PictureConfirmationActivity.this,
                                        CameraActivity.class);
                        startActivity(intent);
                }

        }

        public void saveImage(View view) {
                DatabaseHandler db = DatabaseHandler.getInstance(this);
                Picture picture = new Picture();
                picture.setPicturePath(imageUri);
                db.insertBirdPictureData(picture);
                List<Picture> pictures = db.getAllBirdPictures();
                int i = 0;
                String bobcat = "sfs";
                Intent intent = new Intent(PictureConfirmationActivity.this,
                                CameraActivity.class);
                startActivity(intent);

        }
}