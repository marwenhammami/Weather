package com.marouenhammami.weather.utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.marouenhammami.weather.WeatherConfig;
import com.marouenhammami.weather.client.connection.URLConnector;
import com.marouenhammami.weather.model.ImageContener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by maouenhammami on 21/06/15.
 */
public class FlickrManager {
    // String to create Flickr API urls


    public static final int PHOTO_LARGE = 222;


    private static String createURL(int methodId, String parameter, String id) {
        String method_type = "";
        String url = null;
        switch (methodId) {
            case WeatherConfig.FLICKR_PHOTOS_SEARCH_ID:
                method_type = WeatherConfig.FLICKR_PHOTOS_SEARCH_STRING;
                url = WeatherConfig.FLICKR_BASE_URL + method_type + WeatherConfig.APIKEY_SEARCH_STRING + WeatherConfig.WOEID_STRING + id + WeatherConfig.TAGS_STRING + parameter + WeatherConfig.FORMAT_STRING + "&per_page=" + WeatherConfig.NUMBER_OF_PHOTOS + "&media=photos";
                break;
            case WeatherConfig.FLICKR_GET_SIZES_ID:
                method_type = WeatherConfig.FLICKR_GET_SIZES_STRING;
                url = WeatherConfig.FLICKR_BASE_URL + method_type + WeatherConfig.PHOTO_ID_STRING + parameter + WeatherConfig.APIKEY_SEARCH_STRING + WeatherConfig.FORMAT_STRING;
                break;
            case WeatherConfig.FLICKR_PLACES_SEARCH_ID:
                method_type = WeatherConfig.FLICKR_PLACE_SEARCH_STRING;
                url = WeatherConfig.FLICKR_BASE_URL + method_type + WeatherConfig.APIKEY_SEARCH_STRING + WeatherConfig.QUERY_STRING + parameter + WeatherConfig.FORMAT_STRING;
        }
        return url;
    }

    public static Bitmap getImage(ImageContener imgCon) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(imgCon.getLargeURL());
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (Exception e) {
            Log.e("FlickrManager", e.getMessage());
        }
        return bm;
    }

    public static ArrayList<ImageContener> searchImagesByTag(Context ctx, String tag, String woeId) {
        String url = createURL(WeatherConfig.FLICKR_PHOTOS_SEARCH_ID, tag, woeId);
        ArrayList<ImageContener> tmp = new ArrayList<ImageContener>();
        String jsonString = null;
        try {
            if (URLConnector.isOnline(ctx)) {
                ByteArrayOutputStream baos = URLConnector.readBytes(url);
                jsonString = baos.toString();
            }
            try {
                JSONObject root = new JSONObject(jsonString.replace("jsonFlickrApi(", "").replace(")", ""));
                JSONObject photos = root.getJSONObject("photos");
                JSONArray imageJSONArray = photos.getJSONArray("photo");
                for (int i = 0; i < imageJSONArray.length(); i++) {
                    JSONObject item = imageJSONArray.getJSONObject(i);
                    ImageContener imgCon = new ImageContener(item.getString("id"), item.getString("owner"), item.getString("secret"), item.getString("server"),
                            item.getString("farm"));
                    imgCon.setPosition(i);
                    tmp.add(imgCon);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (NullPointerException nue) {
            nue.printStackTrace();
        }

        return tmp;
    }

    public static String searchImagesByLocation(Context ctx, String place) {
        String url = createURL(WeatherConfig.FLICKR_PLACES_SEARCH_ID, place, "");
        String jsonString = null;
        String woeid = null;
        try {
            if (URLConnector.isOnline(ctx)) {
                ByteArrayOutputStream baos = URLConnector.readBytes(url);
                jsonString = baos.toString();
            }
            try {
                JSONObject root = new JSONObject(jsonString.replace("jsonFlickrApi(", "").replace(")", ""));
                JSONObject photos = root.getJSONObject("places");
                JSONArray imageJSONArray = photos.getJSONArray("place");
                JSONObject item = imageJSONArray.getJSONObject(0);
                woeid = item.getString("woeid");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (NullPointerException nue) {
            nue.printStackTrace();
        }

        return woeid;
    }
}


