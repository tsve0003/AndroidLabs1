package com.example.androidlabs1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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
        weatherImage = (ImageView) findViewById(R.id.weatherImageView);
        currentTemp = (TextView) findViewById(R.id.currentTempTextView);
        maxTemp = (TextView) findViewById(R.id.maxTempTextView);
        minTemp = (TextView) findViewById(R.id.minTempTextView);
        uvRating = (TextView) findViewById(R.id.UV_rating);
        bar = (ProgressBar) findViewById(R.id.progressBar);
        bar.setVisibility(View.VISIBLE);
        bar.setMax(100);

        ForecastQuery reqCityWeather = new ForecastQuery();
        reqCityWeather.execute("http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=7e943c97096a9784391a981c4d878b22&mode=xml&units=metric");  //Type 1

        ForecastQuery reqUV = new ForecastQuery();
        reqUV.execute("http://api.openweathermap.org/data/2.5/uvi?appid=7e943c97096a9784391a981c4d878b22&lat=45.348945&lon=-75.759389");


    }

    //Type1     Type2   Type3
    private class ForecastQuery extends AsyncTask<String, Integer, String> {

        Bitmap image = null;
        private final String url_image = "http://openweathermap.org/img/w/";
        protected static final String ACTIVITY_NAME = "WeatherForecast";

        @Override
        protected String doInBackground(String... strings) {
            {
                InputStream inputStream = null;
                try {

                    //create a URL object of what server to contact:
                    URL url = new URL(strings[0]);
                    inputStream = null;

                    //open the connection
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                    urlConnection.setReadTimeout(10000 /* milliseconds */);
                    urlConnection.setConnectTimeout(15000 /* milliseconds */);
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setDoInput(true);


                    //wait for data:
                    InputStream response = urlConnection.getInputStream();


                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(false);
                    XmlPullParser xpp = factory.newPullParser();
                    xpp.setInput(response, "UTF-8");



                    String parameter = null;
                    boolean set = false;

                    int eventType = xpp.getEventType(); //The parser is currently at START_DOCUMENT

                    while (eventType != XmlPullParser.END_DOCUMENT) {

                        if (eventType == XmlPullParser.START_TAG) {
                            //If you get here, then you are pointing at a start tag
                            if (xpp.getName().equals("temperature")) {
                                //If you get here, then you are pointing to a <temperature> start tag
                                String tempValue = xpp.getAttributeValue(null, "value");
                                Log.i("AsyncTask", "Temperature value found: " + tempValue);
                                publishProgress(25);

                                Thread.sleep(500); //pause for 2000 milliseconds to watch the progress bar spin
                                String tempMin = xpp.getAttributeValue(null, "min");
                                Log.i("AsyncTask", "Minimum temperature  found: " + tempMin);
                                publishProgress(50);

                                Thread.sleep(500); //pause for 2000 milliseconds to watch the progress bar spin
                                String tempMax = xpp.getAttributeValue(null, "max");
                                Log.i("AsyncTask", "Maximum temperature value found: " + tempMax);
                                publishProgress(75);

                                Thread.sleep(500); //pause for 2000 milliseconds to watch the progress bar spin

                            } else if (xpp.getName().equals("weather")) {

                                String iconName = xpp.getAttributeValue(null, "icon"); // this will run for <weather icon="icon" >
                                String fileName = iconName + ".png";
                                File file = getBaseContext().getFileStreamPath(fileName);
                                if (!file.exists()) {
                                    saveImage(fileName);

                                } else {
                                    Log.i(ACTIVITY_NAME, "Saved icon, " + fileName + " is displayed.");
                                    try {
                                        FileInputStream in = new FileInputStream(file);
                                        image = BitmapFactory.decodeStream(in);
                                    } catch (FileNotFoundException e) {
                                        Log.i(ACTIVITY_NAME, "Saved icon, " + fileName + " is not found.");
                                    }
                                }
                                publishProgress(100);

                            }
                        } else if (eventType == XmlPullParser.END_TAG) {
                            if (xpp.getName().equalsIgnoreCase("current"))
                                set = false;
                        }
                        eventType = xpp.next();
                    }

                } catch (IOException e) {
                    Log.i(ACTIVITY_NAME, "IOException: " + e.getMessage());
                } catch (XmlPullParserException e) {
                    Log.i(ACTIVITY_NAME, "XmlPullParserException: " + e.getMessage());
                } finally {
                    if (inputStream != null)
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            Log.i(ACTIVITY_NAME, "IOException: " + e.getMessage());
                        }
                    return null;
                }
            }
        }

        private void saveImage(String fname) {
            HttpURLConnection connection = null;
            try {
                URL url = new URL(url_image + fname);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    image = BitmapFactory.decodeStream(connection.getInputStream());
                    FileOutputStream outputStream = openFileOutput(fname, Context.MODE_PRIVATE);
                    image.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                    outputStream.flush();
                    outputStream.close();
                    Log.i(ACTIVITY_NAME, "Weather icon, " + fname + " is downloaded and displayed.");
                } else
                    Log.i(ACTIVITY_NAME, "Can't connect to the weather icon for downloading.");
            } catch (Exception e) {
                Log.i(ACTIVITY_NAME, "weather icon download error: " + e.getMessage());
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }
    }
}


