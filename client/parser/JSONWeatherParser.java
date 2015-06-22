package com.marouenhammami.weather.client.parser;


import android.content.Context;

import com.marouenhammami.weather.activity.R;
import com.marouenhammami.weather.model.DayForecast;
import com.marouenhammami.weather.model.Location;
import com.marouenhammami.weather.model.Weather;
import com.marouenhammami.weather.model.WeatherForecast;
import com.marouenhammami.weather.utility.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by maouenhammami on 10/06/15.
 */

public class JSONWeatherParser {
    public static Weather getWeather(String data, Context context) throws JSONException {
        Weather weather = new Weather();

        // We create out JSONObject from the data
        JSONObject jObj = new JSONObject(data);

        // We start extracting the info
        Location loc = new Location();

        JSONObject coordObj = Utilities.getObject(context.getString(R.string.coord), jObj);
        loc.setLatitude(Utilities.getFloat(context.getString(R.string.lat), coordObj));
        loc.setLongitude(Utilities.getFloat(context.getString(R.string.lon), coordObj));

        JSONObject sysObj = Utilities.getObject(context.getString(R.string.sys), jObj);
        loc.setCountry(Utilities.getString(context.getString(R.string.country), sysObj));
        loc.setSunrise(Utilities.getInt(context.getString(R.string.sunrise), sysObj));
        loc.setSunset(Utilities.getInt(context.getString(R.string.sunset), sysObj));
        loc.setCity(Utilities.getString(context.getString(R.string.name), jObj));
        weather.location=loc;

        // We get weather info (This is an array)
        JSONArray jArr = jObj.getJSONArray(context.getString(R.string.weather));

        // We use only the first value
        JSONObject JSONWeather = jArr.getJSONObject(0);
        weather.currentCondition.setWeatherId(Utilities.getInt(context.getString(R.string.id), JSONWeather));
        weather.currentCondition.setDescr(Utilities.getString(context.getString(R.string.desc), JSONWeather));
        weather.currentCondition.setCondition(Utilities.getString(context.getString(R.string.main), JSONWeather));
        weather.currentCondition.setIcon(Utilities.getString(context.getString(R.string.icon), JSONWeather));

        JSONObject mainObj = Utilities.getObject(context.getString(R.string.main), jObj);
        weather.currentCondition.setHumidity(Utilities.getInt(context.getString(R.string.humidity), mainObj));
        weather.currentCondition.setPressure(Utilities.getInt(context.getString(R.string.pressure), mainObj));
        weather.temperature.setMaxTemp(Utilities.getFloat(context.getString(R.string.temp_max), mainObj));
        weather.temperature.setMinTemp(Utilities.getFloat(context.getString(R.string.temp_min), mainObj));
        weather.temperature.setTemp(Utilities.getFloat(context.getString(R.string.temp), mainObj));

        // Wind
        JSONObject wObj = Utilities.getObject(context.getString(R.string.wind), jObj);
        weather.wind.setSpeed(Utilities.getFloat(context.getString(R.string.speed), wObj));
        weather.wind.setDeg(Utilities.getFloat(context.getString(R.string.deg), wObj));

        // Clouds
        JSONObject cObj = Utilities.getObject(context.getString(R.string.clouds), jObj);
        weather.clouds.setPerc(Utilities.getInt(context.getString(R.string.all), cObj));

        return weather;
    }

    public static WeatherForecast getForecastWeather(String data, Context context) throws JSONException  {

        WeatherForecast forecast = new WeatherForecast();

        // We create out JSONObject from the data
        JSONObject jObj = new JSONObject(data);

        JSONArray jArr = jObj.getJSONArray(context.getString(R.string.list)); // Here we have the forecast for every day

        // We traverse all the array and parse the data
        for (int i=0; i < jArr.length(); i++) {
            JSONObject jDayForecast = jArr.getJSONObject(i);

            // Now we have the json object so we can extract the data
            DayForecast df = new DayForecast();

            // We retrieve the timestamp (dt)
            df.timestamp = jDayForecast.getLong(context.getString(R.string.dt));

            // Temp is an object
            JSONObject jTempObj = jDayForecast.getJSONObject(context.getString(R.string.temp));

            df.weather.temperature.setDay(Utilities.getFloat(context.getString(R.string.day), jTempObj));
            df.weather.temperature.setMinTemp(Utilities.getFloat(context.getString(R.string.min), jTempObj));
            df.weather.temperature.setMaxTemp(Utilities.getFloat(context.getString(R.string.max), jTempObj));
            df.weather.temperature.setNight(Utilities.getFloat(context.getString(R.string.night), jTempObj));
            df.weather.temperature.setEve(Utilities.getFloat(context.getString(R.string.eve), jTempObj));
            df.weather.temperature.setMorning(Utilities.getFloat(context.getString(R.string.morn), jTempObj));

            // Pressure & Humidity
            df.weather.currentCondition.setPressure((float) jDayForecast.getDouble(context.getString(R.string.pressure)));
            df.weather.currentCondition.setHumidity((float) jDayForecast.getDouble(context.getString(R.string.humidity)));

            //Wind
            df.weather.wind.setDeg((float)jDayForecast.getDouble(context.getString(R.string.deg)));
            df.weather.wind.setSpeed((float)jDayForecast.getDouble(context.getString(R.string.speed)));

            // ...and now the weather
            JSONArray jWeatherArr = jDayForecast.getJSONArray(context.getString(R.string.weather));
            JSONObject jWeatherObj = jWeatherArr.getJSONObject(0);
            df.weather.currentCondition.setWeatherId(Utilities.getInt(context.getString(R.string.id), jWeatherObj));
            df.weather.currentCondition.setDescr(Utilities.getString(context.getString(R.string.desc), jWeatherObj));
            df.weather.currentCondition.setCondition(Utilities.getString(context.getString(R.string.main), jWeatherObj));
            df.weather.currentCondition.setIcon(Utilities.getString(context.getString(R.string.icon), jWeatherObj));

            forecast.addForecast(df);
        }



        return forecast;
    }
}
