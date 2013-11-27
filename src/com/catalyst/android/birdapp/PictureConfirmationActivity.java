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
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;

public class PictureConfirmationActivity extends Activity {
        private String imageUri;
        private String birdName;
        private Bundle bundle;
        private Bitmap bmp;

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
                bmp = BitmapFactory.decodeStream(fis);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                //Rotates the image so that it comes out on the screen in the same orientation that is was taken.
                rotateImage();
                ImageView img = (ImageView) findViewById(R.id.image);
                img.setImageBitmap(bmp);
        }
        
        /**
         * Rotates the picture back to the orientation it was when the picture was taken.
         */
        private void rotateImage(){
            int width = bmp.getWidth();
            int height = bmp.getHeight();
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            bmp = Bitmap.createBitmap(bmp, 0, 0,width, height, matrix, true);
        }
        
        /**
         *  Deletes image from phone.
         */
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

        /** 
         * Saves image to Database.
         */
        public void saveImage(View view) {
                DatabaseHandler db = DatabaseHandler.getInstance(this);
                Intent intent = new Intent(PictureConfirmationActivity.this,
                                BirdFormActivity.class);
                bundle.putString("fileName", imageUri);
                intent.putExtras(bundle);
                //Recycle the bitmap to avoid memory space errors
                bmp.recycle();
                startActivity(intent);

        }
        
        /**
         * Keeps the user from pressing the back button, otherwise it throws an exception.  The user does not need to use the back button from this screen.
         */
        @Override
        public void onBackPressed() {
        }
}