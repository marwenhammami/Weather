package com.marouenhammami.weather.client.connection;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by marouen hammami on 09/06/15.
 */
public class WeatherHttpClient {

    private static final String CURRENT_WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric&mode=json";
    private static final String CURRENT_WEATHER_LOCATION_URL = "http://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&units=metric&mode=json";
    private static final String IMG_URL = "http://openweathermap.org/img/w/";

    private static String BASE_FORECAST_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?mode=json&q=";

    private String iconExtension=".png";

    public static String loadCurrentWeather(String city) {
        return load(CURRENT_WEATHER_URL, city);
    }

    private static String load(String currentWeatherUrl, String city) {
        String data = null;
        BufferedReader reader = null;
        InputStream is = null;
        HttpURLConnection connection = null;

        try {
            URL url = new URL(String.format(currentWeatherUrl, city));
            Log.d("debug", "URL is " + url.toString());

            connection = (HttpURLConnection) url.openConnection();
            is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is));

            StringBuffer json = new StringBuffer();
            String temp = null;

            while ((temp = reader.readLine()) != null) {
                json.append(temp).append("\n");
            }
            data = json.toString();
            return data;
        } catch (Exception ex) {

        } finally {
            try {
                is.close();
            } catch (Throwable t) {
            }

            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {

                }
            }
            try {
                connection.disconnect();
            } catch (Throwable t) {
            }
        }
        return null;
    }

    public String getForecastWeatherData(String location, String lang, String sForecastDayNum) {
        HttpURLConnection con = null;
        InputStream is = null;
        int forecastDayNum = Integer.parseInt(sForecastDayNum);

        try {

            // Forecast
            String url = BASE_FORECAST_URL + location;
            if (lang != null)
                url = url + "&lang=" + lang;

            url = url + "&cnt=" + forecastDayNum;
            url = url + "&units=metric&mode=json";
            con = (HttpURLConnection) (new URL(url)).openConnection();
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            // Let's read the response
            StringBuffer buffer1 = new StringBuffer();
            is = con.getInputStream();
            BufferedReader br1 = new BufferedReader(new InputStreamReader(is));
            String line1 = null;
            while ((line1 = br1.readLine()) != null)
                buffer1.append(line1 + "\r\n");

            is.close();
            con.disconnect();

            System.out.println("Buffer [" + buffer1.toString() + "]");
            return buffer1.toString();
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (Throwable t) {
            }
            try {
                con.disconnect();
            } catch (Throwable t) {
            }
        }

        return null;

    }


    public Bitmap getImage(String code) {
        String url = IMG_URL + code + iconExtension;
        BitmapFactory.Options bmOptions;
        bmOptions = new BitmapFactory.Options();
        bmOptions.inSampleSize = 1;
        return loadBitmap(url, bmOptions);
    }

    public static Bitmap loadBitmap(String URL, BitmapFactory.Options options) {
        Bitmap bitmap = null;
        InputStream in;
        try {
            in = OpenHttpConnection(URL);
            bitmap = BitmapFactory.decodeStream(in, null, options);
            in.close();
        } catch (IOException e1) {
        }
        return bitmap;
    }

    private static InputStream OpenHttpConnection(String strURL)
            throws IOException {
        InputStream inputStream = null;
        URL url = new URL(strURL);
        URLConnection conn = url.openConnection();

        try {
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setRequestMethod("GET");
            httpConn.connect();

            if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = httpConn.getInputStream();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return inputStream;
    }

    public static String loadCurrentWeatherLocation(Context context, Location location) {
        return load(context, CURRENT_WEATHER_LOCATION_URL, location);
    }

    private static String load(Context context, String currentWeatherUrl, Location location) {
        return null;
    }
}
