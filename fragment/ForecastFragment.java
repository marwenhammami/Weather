package com.marouenhammami.weather.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.marouenhammami.weather.activity.MainActivity;
import com.marouenhammami.weather.activity.R;
import com.marouenhammami.weather.adapter.ForecastListViewAdapter;
import com.marouenhammami.weather.client.connection.WeatherHttpClient;
import com.marouenhammami.weather.client.parser.JSONWeatherParser;
import com.marouenhammami.weather.item.EntryItem;
import com.marouenhammami.weather.model.Weather;
import com.marouenhammami.weather.model.WeatherForecast;
import com.marouenhammami.weather.utility.Utilities;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maouenhammami on 12/06/15.
 */
public class ForecastFragment extends Fragment {
    ListView listForecast;
    List<EntryItem> rowItems;
    int numDays = 7;
    String cityName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.forecast_fragment, container, false);

        listForecast = (ListView) view.findViewById(R.id.list);
        cityName = ((MainActivity) getActivity()).getCity();

        JSONForecastWeatherTask task1 = new JSONForecastWeatherTask();
        task1.execute(new String[]{cityName, getString(R.string.language), String.valueOf(numDays)});


        return view;
    }


    private class JSONForecastWeatherTask extends AsyncTask<String, Void, WeatherForecast> {
        private Bitmap[] icons;

        @Override
        protected WeatherForecast doInBackground(String... params) {
            numDays = Integer.valueOf(params[2]);
            icons = new Bitmap[numDays];
            String data = ((new WeatherHttpClient()).getForecastWeatherData(params[0], params[1], params[2]));
            WeatherForecast forecast = new WeatherForecast();
            try {
                forecast = JSONWeatherParser.getForecastWeather(data, getActivity());
                System.out.println("Weather [" + forecast + "]");
                for (int i = 0; i < numDays; i++) {
                    icons[i] = Bitmap.createScaledBitmap(((new WeatherHttpClient()).getImage(forecast.getForecast(i).weather.currentCondition.getIcon())), 120, 120, false);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return forecast;

        }


        @Override
        protected void onPostExecute(final WeatherForecast forecastWeather) {
            super.onPostExecute(forecastWeather);

            rowItems = new ArrayList<>();
            for (int i = 0; i < numDays; i++) {
                String temDesc = forecastWeather.getForecast(i).weather.currentCondition.getDescr() + getActivity().getString(R.string.on) + Utilities.fromTimeStampToDayConvertor(i);
                temDesc = temDesc.substring(0, 1).toUpperCase() + temDesc.substring(1);
                int maxTemp = Math.round(forecastWeather.getForecast(i).weather.temperature.getMaxTemp());
                int minTemp = Math.round(forecastWeather.getForecast(i).weather.temperature.getMaxTemp());
                String avTemp = String.valueOf((((maxTemp <= 50) ? maxTemp : (maxTemp - 275)) + ((minTemp <= 50) ? minTemp : (minTemp - 275))) / 2);
                EntryItem item = new EntryItem(icons[i], temDesc, avTemp);
                rowItems.add(item);
            }
            ForecastListViewAdapter forecastListViewAdapter = new ForecastListViewAdapter(getActivity().getBaseContext(), R.layout.list_item_entry, rowItems);
            listForecast.setAdapter(forecastListViewAdapter);
            listForecast.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    EntryItem item = (EntryItem) rowItems.get(position);
                    LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                    View promptView = layoutInflater.inflate(R.layout.forecast_detail_dialog, null);

                    createCustomDialog(getActivity().getApplicationContext(), promptView, forecastWeather.getForecast(position).weather);
                }
            });
        }

        public void createCustomDialog(Context context, View view, Weather weather) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
            alertDialogBuilder.setView(view);
            TextView humidityVal = (TextView) view.findViewById(R.id.humidity_txt_detail);
            TextView precipitationVal = (TextView) view.findViewById(R.id.precipitation_txt_detail);
            TextView pressureVal = (TextView) view.findViewById(R.id.pressure_txt_detail);
            TextView windSpeed = (TextView) view.findViewById(R.id.wind_txt_detail);
            TextView directionVal = (TextView) view.findViewById(R.id.direction_txt_detail);

            humidityVal.setText(weather.currentCondition.getHumidity() + context.getString(R.string.pourcentage));
            precipitationVal.setText(String.valueOf(weather.clouds.getPerc()) + getActivity().getString(R.string.mm));
            pressureVal.setText(Math.round(weather.currentCondition.getPressure()) + context.getString(R.string.press));
            windSpeed.setText(weather.wind.getSpeed() + context.getString(R.string.mps));
            directionVal.setText(getString(Utilities.headingToString(weather.wind.getDeg())));


            final AlertDialog alert = alertDialogBuilder.create();
            // Setting OK Button
            alert.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    alert.dismiss();
                }
            });

            alert.show();
        }

    }
}
