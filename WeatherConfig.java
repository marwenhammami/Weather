package com.marouenhammami.weather;

/**
 * Created by maouenhammami on 22/06/15.
 */
public class WeatherConfig {

    public static final String FLICKR_BASE_URL = "https://api.flickr.com/services/rest/?method=";
    public static final String FLICKR_PHOTOS_SEARCH_STRING = "flickr.photos.search";
    public static final String FLICKR_GET_SIZES_STRING = "flickr.photos.getSizes";
    public static final int FLICKR_PHOTOS_SEARCH_ID = 1;
    public static final int FLICKR_GET_SIZES_ID = 2;
    public static final int NUMBER_OF_PHOTOS = 10;

    public static final String FLICKR_PLACE_SEARCH_STRING = "flickr.places.find";
    public static final int FLICKR_PLACES_SEARCH_ID = 3;

    public static final String QUERY_STRING = "&query=";


    public static final String APIKEY_SEARCH_STRING = "&api_key=9ab47aad4c97e03d0d4c3b610ce8ce80";

    public static final String TAGS_STRING = "&tags=";
    public static final String PHOTO_ID_STRING = "&photo_id=";
    public static final String FORMAT_STRING = "&format=json";
    public static final String WOEID_STRING = "&woe_id=";
}
