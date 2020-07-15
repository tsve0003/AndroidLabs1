package com.example.androidlabs1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class WeatherForecast extends AppCompatActivity {

  private ImageView weatherImage;
  private TextView currentTemp;
  private TextView maxTemp;
  private TextView minTemp;
  private TextView uvRating;
  private ProgressBar bar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);
        weatherImage = (ImageView)findViewById(R.id.weatherImageView);
        currentTemp = (TextView) findViewById(R.id.currentTempTextView);
        maxTemp= (TextView)findViewById(R.id.maxTempTextView);
        minTemp = (TextView)findViewById(R.id.minTempTextView);
        uvRating = (TextView) findViewById(R.id.UV_rating);
        bar = (ProgressBar) findViewById(R.id.progressBar);


    }
}