package com.marouenhammami.weather.model;

import android.graphics.Bitmap;

/**
 * Created by maouenhammami on 10/06/15.
 */
public class Weather {

    public com.marouenhammami.weather.model.Location location;
    public CurrentCondition currentCondition;
    public Temperature temperature;
    public Wind wind;
    public Rain rain;
    public Snow snow;
    public Clouds clouds;

    public Bitmap iconData;

    public Weather() {
        currentCondition = new CurrentCondition();
        temperature = new Temperature();
        wind = new Wind();
        rain = new Rain();
        snow = new Snow();
        clouds = new Clouds();
    }
}
