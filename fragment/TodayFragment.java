package com.marouenhammami.weather.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.marouenhammami.weather.activity.R;
import com.marouenhammami.weather.client.connection.WeatherHttpClient;
import com.marouenhammami.weather.client.parser.JSONWeatherParser;
import com.marouenhammami.weather.geolocation.GPSTracker;
import com.marouenhammami.weather.model.ImageContener;
import com.marouenhammami.weather.model.Weather;
import com.marouenhammami.weather.utility.FlickrManager;
import com.marouenhammami.weather.utility.Utilities;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by marouen hammami on 09/06/15.
 */
public class TodayFragment extends Fragment {

    private TextView humidityVal;
    private TextView precipitationVal;
    private TextView pressureVal;
    private TextView windSpeed;
    private TextView directionVal;

    private TextView cityNameLabel;
    private TextView temperature;
    private TextView description;
    private ImageView weatherState;

    private RelativeLayout weatherBackground;

    private String cityName = "";

    private GPSTracker gps;
    private Typeface weatherFont;

    private TextView[] fields;

    private ArrayList<ImageContener> imageList;
    private Drawable im;
    private AnimationDrawable animation;

    private String delimiters = "\\),\\(|\\)|\\(";

    private ProgressDialog progressDialog;
    private PostCityNameCallback mCallback;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.today_fragment, container, false);
        weatherFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Quicksand.otf");

        initComponents(view);

        getMyCity();

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        for (TextView tv : fields) {
            tv.setTypeface(weatherFont);
            tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
        }
    }

    private void initComponents(View view) {
        humidityVal = (TextView) view.findViewById(R.id.humidity_txt);
        precipitationVal = (TextView) view.findViewById(R.id.precipitation_txt);
        pressureVal = (TextView) view.findViewById(R.id.pressure_txt);
        windSpeed = (TextView) view.findViewById(R.id.wind_txt);
        directionVal = (TextView) view.findViewById(R.id.direction_txt);

        cityNameLabel = (TextView) view.findViewById(R.id.city_name);
        temperature = (TextView) view.findViewById(R.id.temperature_txt);
        description = (TextView) view.findViewById(R.id.weather_desc);
        weatherState = (ImageView) view.findViewById(R.id.ic_weather_state);

        weatherBackground = (RelativeLayout) view.findViewById(R.id.weather_background);

        fields = new TextView[]{humidityVal, precipitationVal, pressureVal, windSpeed, directionVal, cityNameLabel, temperature, description};

        animation = new AnimationDrawable();
    }

    private void getMyCity() {
        gps = new GPSTracker(getActivity());

        // check if GPS enabled
        if (gps.canGetLocation()) {

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            Log.d("debug", "latitude is " + latitude);
            Log.d("debug", "longitude is " + longitude);

            cityName = Utilities.getCityName(longitude, latitude, getActivity()).replaceAll("\\s", getString(R.string.space_code));

            cityName = (cityName.contains("-")) ? (cityName.split("-"))[0] : cityName;

            mCallback.onPostData(cityName);

            JSONWeatherTask task = new JSONWeatherTask();

            task.execute(new String[]{cityName});
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
    }


    private class JSONWeatherTask extends AsyncTask<String, Void, Weather> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(getString(R.string.loading));
            progressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            progressDialog.setMessage(String.format(getString(R.string.loading), values[0], values[1]));
        }

        @Override
        protected Weather doInBackground(String... params) {
            Weather weather = new Weather();
            String data = ((new WeatherHttpClient()).loadCurrentWeather(params[0]));
            try {
                weather = JSONWeatherParser.getWeather(data, getActivity().getApplicationContext());
                weather.iconData = ((new WeatherHttpClient()).getImage(weather.currentCondition.getIcon()));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return weather;

        }


        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);
            int temp = Math.round((weather.temperature.getTemp()));
            cityNameLabel.setText(weather.location.getCity());
            description.setText(weather.currentCondition.getCondition() + getString(R.string.left_par) + weather.currentCondition.getDescr() + getString(R.string.right_par));
            temperature.setText(((temp <= 50) ? temp : (temp - 275)) + getString(R.string.cel));
            humidityVal.setText(weather.currentCondition.getHumidity() + getActivity().getString(R.string.pourcentage));
            pressureVal.setText(Math.round(weather.currentCondition.getPressure()) + getActivity().getString(R.string.press));
            windSpeed.setText(weather.wind.getSpeed() + getActivity().getString(R.string.mps));
            directionVal.setText(getString(Utilities.headingToString(weather.wind.getDeg())));
            precipitationVal.setText(String.valueOf(weather.clouds.getPerc()) + getActivity().getString(R.string.mm));

            weatherState.setImageBitmap(Bitmap.createScaledBitmap(weather.iconData, 120, 120, false));
            weatherState.setAdjustViewBounds(true);

            new Thread(getMetadata).start();

        }

    }

    /**
     * Runnable to get metadata from Flickr API
     */
    Runnable getMetadata = new Runnable() {
        @Override
        public void run() {
            String woe = FlickrManager.searchImagesByLocation(getActivity().getApplicationContext(), cityName);
            String[] tags = description.getText().toString().replaceAll("\\s", getString(R.string.space_code)).split(delimiters);
            String tag = tags[0];
            if (tag != null && tag.length() >= 3)
                imageList = FlickrManager.searchImagesByTag(getActivity().getApplicationContext(), tag, woe);

            setAnimation(imageList);
            setBackground(im);
            progressDialog.dismiss();

        }
    };


    private void setBackground(final Drawable im) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                weatherBackground.setBackgroundDrawable(animation);
                weatherBackground.post(new Starter());

            }
        });
    }

    private void setAnimation(ArrayList<ImageContener> imageList) {

        for (int i = 0; i < imageList.size(); i++) {
            animation.addFrame(new BitmapDrawable(FlickrManager.getImage(imageList.get(i))), 5000);
        }
        animation.setOneShot(false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (PostCityNameCallback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + getString(R.string.interface_error));
        }
    }

    class Starter implements Runnable {
        public void run() {
            animation.stop();
            animation.start();
        }
    }

    public interface PostCityNameCallback {
        public void onPostData(String city);
    }

}
