package com.marouenhammami.weather.utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;

import com.marouenhammami.weather.activity.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by marouen hammami on 09/06/15.
 */
public class Utilities {

    private final static SimpleDateFormat sdf = new SimpleDateFormat("EEEE");

    public Utilities() {
    }

    /**
     * This method show the About dialog.
     *
     * @param context
     */
    public static void createAboutDialog(Context context, String message) {
        final AlertDialog alertDialog = new AlertDialog.Builder(
                context).create();

        alertDialog.setTitle(context.getString(R.string.dialog_title));
        alertDialog.setMessage(message);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    /**
     * Get the JSONObject from a tag name.
     * @param tagName
     * @param jObj
     * @return JSON Object
     * @throws JSONException
     */
    public static JSONObject getObject(String tagName, JSONObject jObj) throws JSONException {
        JSONObject subObj = jObj.getJSONObject(tagName);
        return subObj;
    }

    /**
     * Get data from JSONObject by tagName.
     * @param tagName
     * @param jObj
     * @return
     * @throws JSONException
     */
    public static String getString(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getString(tagName);
    }

    /**
     *
     * @param tagName
     * @param jObj
     * @return Float
     * @throws JSONException
     */
    public static float getFloat(String tagName, JSONObject jObj) throws JSONException {
        return (float) jObj.getDouble(tagName);
    }

    /**
     *
     * @param tagName
     * @param jObj
     * @return int
     * @throws JSONException
     */
    public static int getInt(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getInt(tagName);
    }

    /**
     * Converts wind degrees to direction.
     * @param x
     * @return direction
     */
    public static int headingToString(double x) {
        int directions[] = {R.string.north, R.string.north_east, R.string.east, R.string.south_east, R.string.south, R.string.south_west, R.string.west, R.string.north_west};
        return directions[(int) Math.round((((double) x % 360) / 45)) % 8];
    }

    /**
     * Returns the name of the day using the day position.
     * @param position
     * @return Name of the day
     */
    public static String fromTimeStampToDayConvertor(int position) {
        Date date = Calendar.getInstance().getTime();
        Calendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.add(GregorianCalendar.DAY_OF_WEEK, position + 1);

        return sdf.format(gc.getTime());


    }

    /**
     * Finds the city name from the longitude and latitude of a location.
     * @param longitude
     * @param latitude
     * @param activity
     * @return cityName
     */
    public static String getCityName(double longitude, double latitude, Activity activity) {
        String cityName = null;
        Geocoder gcd = new Geocoder(activity,
                Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0)
                cityName = addresses.get(0).getLocality();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cityName;
    }
}
