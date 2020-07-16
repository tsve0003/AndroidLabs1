package com.example.androidlabs1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class WeatherForecast extends AppCompatActivity {

    private ImageView weatherImageView;
    private TextView currentTextView;
    private TextView minTextView;
    private TextView  maxTextView;
    private TextView uvRating;
    private ProgressBar bar;
    protected static final String URL_STRING = "http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=7e943c97096a9784391a981c4d878b22&mode=xml&units=metric";
    //protected static final String URL_IMAGE = "http://openweathermap.org/img/w/";
    protected static final String ACTIVITY_NAME = "WeatherForecast";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);
        weatherImageView = (ImageView) findViewById(R.id.weatherImageView);
        currentTextView = (TextView) findViewById(R.id.currentTempTextView);
        maxTextView = (TextView) findViewById(R.id.maxTempTextView);
        minTextView = (TextView) findViewById(R.id.minTempTextView);
        uvRating = (TextView) findViewById(R.id.UV_rating);
        bar = (ProgressBar) findViewById(R.id.progressBar);
        bar.setVisibility(View.VISIBLE);
        bar.setMax(100);

        ForecastQuery currentWeatherreq = new ForecastQuery();
        currentWeatherreq.execute("http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=7e943c97096a9784391a981c4d878b22&mode=xml&units=metric");  //Type 1

        ForecastQuery reqUV = new ForecastQuery();
        reqUV.execute("http://api.openweathermap.org/data/2.5/uvi?appid=7e943c97096a9784391a981c4d878b22&lat=45.348945&lon=-75.759389");


    }



    // a subclass of AsyncTask                  Type1    Type2    Type3
    private class ForecastQuery extends AsyncTask<String, Integer, String> {
        private String currentTemp = null;
        private String minTemp = null;
        private String maxTemp = null;
        private String iconFilename = null;
        private Bitmap weatherImage = null;
        private final String urlString = "http://openweathermap.org/img/w/";
        Bitmap image = null;

        @Override
        protected String doInBackground(String... params) {

            try {
                URL url = new URL(URL_STRING);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                InputStream response = conn.getInputStream();
                // Starts the query
                conn.connect();

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput( response  , "UTF-8");

               String parameter = null;

                int eventType = xpp.getEventType();
                boolean set = false;
                eventType = XmlPullParser.START_TAG;
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        if (xpp.getName().equals("temperature")) {
                            String tempValue = xpp.getAttributeValue(null, "value");
                            Log.i("AsyncTask", "Found value message: " + tempValue);
                            publishProgress(25);
                            Thread.sleep(500); //pause for 2000 milliseconds to watch the progress
                            String tempMin = xpp.getAttributeValue(null, "min");
                            Log.e("AsyncTask", "Found min message: " + tempMin);
                            publishProgress(50);
                            Thread.sleep(500); //pause for 2000 milliseconds to watch the progress bar spin
                            String tempMax = xpp.getAttributeValue(null, "max");
                            Log.e("AsyncTask", "Found max message: " + tempMax);
                            publishProgress(75);
                        }
                        else if (xpp.getName().equals("weather")) {
                            String weatherIcon = xpp.getAttributeValue(null, "icon");
                            String fileName = weatherIcon + ".png";
                            Log.e("AsyncTask", "Found icon name: " + weatherIcon);
                            URL urlImage = new URL(urlString);
                            conn = (HttpURLConnection) urlImage.openConnection();
                            conn.connect();
                            int responseCode = conn.getResponseCode();
                            if (responseCode == 200) {
                                image = BitmapFactory.decodeStream(conn.getInputStream());
                                FileOutputStream iconFilename = openFileOutput(weatherIcon + ".png", Context.MODE_PRIVATE);

                                image.compress(Bitmap.CompressFormat.PNG, 80, iconFilename);
                                iconFilename.flush();
                                iconFilename.close();
                             } else if(fileExistance(iconFilename)) {
                                FileInputStream fis = null;
                                try {
                                    fis = new FileInputStream(getBaseContext().getFileStreamPath(iconFilename));
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                Bitmap bm = BitmapFactory.decodeStream(fis);
                             }

                        }


                            publishProgress(100);

                        }

                    }
                xpp.next();


        } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return "done";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            bar.setProgress(values[0]);
            if (values[0] == 100) {

            }
        }
        public boolean fileExistance(String fname){
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();   }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            currentTextView.setText("Current " + String.format("%.1f", Double.parseDouble(currentTemp)) + "\u00b0");
            minTextView.setText("Min " + String.format("%.1f", Double.parseDouble(minTemp)) + "\u00b0");
            maxTextView.setText("Max " + String.format("%.1f", Double.parseDouble(maxTemp)) + "\u00b0");
            weatherImageView.setImageBitmap(weatherImage);
            bar.setVisibility(View.INVISIBLE);
        }

//        private void saveImage(String fname) {
//            HttpURLConnection connection = null;
//            try {
//                URL url = new URL(URL_IMAGE + fname);
//                connection = (HttpURLConnection) url.openConnection();
//                connection.connect();
//                int responseCode = connection.getResponseCode();
//                if (responseCode == 200) {
//                    weatherImage = BitmapFactory.decodeStream(connection.getInputStream());
//                    FileOutputStream outputStream = openFileOutput(fname, Context.MODE_PRIVATE);
//                    weatherImage.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
//                    outputStream.flush();
//                    outputStream.close();
//                    Log.i(ACTIVITY_NAME, "Weather icon, " + fname + " is downloaded and displayed.");
//                } else
//                    Log.i(ACTIVITY_NAME, "Can't connect to the weather icon for downloading.");
//            } catch (Exception e) {
//                Log.i(ACTIVITY_NAME, "weather icon download error: " + e.getMessage());
//            } finally {
//                if (connection != null) {
//                    connection.disconnect();
//                }
//            }
//        }
    }
    }