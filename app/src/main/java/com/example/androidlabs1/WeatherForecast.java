package com.example.androidlabs1;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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
        currentWeatherreq.execute("http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=7e943c97096a9784391a981c4d878b22&mode=xml&units=metric");

 //Type 1    //    ForecastQuery reqUV = new ForecastQuery();
    //    reqUV.execute("http://api.openweathermap.org/data/2.5/uvi?appid=7e943c97096a9784391a981c4d878b22&lat=45.348945&lon=-75.759389");


    }
        // a subclass of AsyncTask                  Type1    Type2    Type3

        private class ForecastQuery extends AsyncTask <String, Integer, String> {
            String tempValue, min, max, uv, weatherIcon;
            Bitmap image;
            private final String IMG_URL = "http://openweathermap.org/img/w/";

            @Override
            protected String doInBackground(String... params) {
                try {

                    //get the string url:
                    String myUrl = params[0];

                    //create the network connection:
                    URL url = new URL(myUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000 /* milliseconds */);
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    InputStream inStream = conn.getInputStream();

                    //create a pull parser:
                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(false);
                    XmlPullParser xpp = factory.newPullParser();
                    xpp.setInput(inStream, "UTF-8");

                    //now loop over the XML:
                    while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                        if (xpp.getEventType() == XmlPullParser.START_TAG) {
                            String tagName = xpp.getName(); //get the name of the starting tag: <tagName>
                            if (tagName.equals("temperature")) {
                                tempValue = xpp.getAttributeValue(null, "value");
                                Log.e("AsyncTask", "Found value message: " + tempValue);
                                publishProgress(25);
                                Thread.sleep(500); //pause for 2000 milliseconds to watch the progress bar spin
                                min = xpp.getAttributeValue(null, "min");
                                Log.e("AsyncTask", "Found min message: " + min);
                                publishProgress(50);
                                Thread.sleep(500); //pause for 2000 milliseconds to watch the progress bar spin
                                max = xpp.getAttributeValue(null, "max");
                                Log.e("AsyncTask", "Found max message: " + max);
                                publishProgress(75);

                            } else if (tagName.equals("weather")) {
                                weatherIcon = xpp.getAttributeValue(null, "icon");
                                String fileName = weatherIcon + ".png";
                                Log.e("AsyncTask", "Found icon name: " + weatherIcon);
                                if (fileExistance(fileName)) {
                                    FileInputStream fis = null;
                                    try {
                                        fis = new FileInputStream(getBaseContext().getFileStreamPath(fileName));
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                    image = BitmapFactory.decodeStream(fis);


                                } else {
                                    Log.e("AsyncTask", "Downloading image from the internet");
                                    //download image

                                    image = HTTPUtils.getImage(IMG_URL + fileName);
                                    FileOutputStream outputStream = null;
                                    try {
                                        outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
                                        image.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                                        outputStream.flush();
                                        outputStream.close();
                                    } catch (Exception e) {
                                        Log.e("AsyncTask", "Error");
                                    }

                                }
                                publishProgress(100);
                            }
                        }
                        xpp.next(); //advance to next XML event
                    }
                    //Start of JSON reading of UV factor:
                    //create the network connection:
                    URL UVurl = new URL("http://api.openweathermap.org/data/2.5/uvi?appid=7e943c97096a9784391a981c4d878b22&lat=45.348945&lon=-75.759389");
                    HttpURLConnection UVConnection = (HttpURLConnection) UVurl.openConnection();
                    inStream = UVConnection.getInputStream();

                    //create a JSON object from the response
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    String result = sb.toString();

                    //now a JSON table:
                    JSONObject jObject = new JSONObject(result);
                    uv = String.valueOf(jObject.getDouble("value"));
                    Log.e("AsyncTask", "Found UV: " + uv);
                } catch (Exception ex) {
                    Log.e("Crash!!", ex.getMessage());
                }
                return "Finished task";
            }

            public boolean fileExistance(String fname) {
                File file = getBaseContext().getFileStreamPath(fname);
                return file.exists();
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                bar.setVisibility(View.VISIBLE);
                bar.setProgress(values[0]);
            }

            @Override
            protected void onPostExecute(String s) {
                currentTextView.setText("Current temperature: " + tempValue + "°C");
                minTextView.setText("Min temperature: " + min + "°C");
                maxTextView.setText("Max temperature: " + max + "°C");
                uvRating.setText("UV Rating: " + uv);
                weatherImageView.setImageBitmap(image);
                bar.setVisibility(View.INVISIBLE);
            }
        }

        private static class HTTPUtils {
            public static Bitmap getImage(URL url) {
                HttpURLConnection connection = null;
                try {
                    connection = (HttpURLConnection) url.openConnection();
                    connection.connect();
                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        return BitmapFactory.decodeStream(connection.getInputStream());
                    } else
                        return null;
                } catch (Exception e) {
                    return null;
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }

            public static Bitmap getImage(String urlString) {
                try {
                    URL url = new URL(urlString);
                    return getImage(url);
                } catch (MalformedURLException e) {
                    return null;
                }
            }
        }
    }


