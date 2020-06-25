package com.example.androidlabs1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class ProfileActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageButton takePictureBtn;
    public static final String ACTIVITY_NAME = "PROFILE_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // get the intent that got us here
        Intent fromMain = getIntent();

        String emailTyped = fromMain.getStringExtra("EMAIL");

        //Put the string that was sent from FirstActivity into the edit text:
        EditText enterText = (EditText) findViewById(R.id.enterEmail);
        enterText.setText(emailTyped);


        takePictureBtn = (ImageButton) findViewById(R.id.ImageButton);
        takePictureBtn.setOnClickListener(c -> {


            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }

        });
        Button btnCht = (Button)findViewById (R.id.chatBtn);
        btnCht.setOnClickListener(c ->  {
            Intent goToChatPage = new Intent(ProfileActivity.this, ChatRoomActivity.class);
            goToChatPage.putExtra("name", "Eric");
            startActivity(goToChatPage);

        });

            Log.e(ACTIVITY_NAME, "In function: onCreate()");

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                takePictureBtn.setImageBitmap(imageBitmap);
            } else if (resultCode == RESULT_CANCELED) {
                takePictureBtn.setImageResource(R.drawable.ic_rose);
            }
            Log.e(ACTIVITY_NAME, "In function: onActivityResult()");
        }
    }



    @Override
    protected void onStart() {
        super.onStart();
        Log.d(ACTIVITY_NAME, "In function: onStart()");
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.e(ACTIVITY_NAME, "In function: onResume()");
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.e(ACTIVITY_NAME, "In function: onPause()");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.e(ACTIVITY_NAME, "In function: onStop()");
    }
        @Override
        protected void onDestroy() {
            super.onDestroy();
        Log.e(ACTIVITY_NAME, "In function: onDestroy()");
    }
}